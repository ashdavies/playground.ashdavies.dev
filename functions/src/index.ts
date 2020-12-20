"use strict"

import { camelizeKeys } from "humps"
import { graphql } from "@octokit/graphql"
import yaml, { Document } from "yaml"

import * as admin from "firebase-admin"
import * as functions from "firebase-functions"

import type {
  CollectionReference,
  DocumentData,
  QueryDocumentSnapshot,
  QuerySnapshot,
} from "@google-cloud/firestore"


/**
 * - Firebase collections API will return documents each incurring a read charge sorted by key
 * - Pagination of GitHub response is not feasible as documents are returned unordered
 */

const query: string = `query Conferences {
  repository(owner: "AndroidStudyGroup", name: "conferences") {
    conferences: object(expression: "HEAD:_conferences") {
      ... on Tree {
        entries {
          oid
          
          data: object {
            ... on Blob {
              text
            }
          }
        }
      }
    }
  }
}`

admin.initializeApp()

const collection: CollectionReference = admin
  .firestore()
  .collection("conferences")

export const conferences = functions
  .region("europe-west1")
  .https
  .onRequest(async (request, response) => {
    const page: number = request.query.page !== undefined ? +request.query.page : 0
    const perPage: number = request.query.perPage !== undefined ? +request.query.perPage : 10
    const startAt: number = perPage * page

    const token: string | undefined = request.query.token as string | undefined
    if (token === undefined) {
      response.sendStatus(400)
      return
    }

    const documents: { [key: string]: DocumentData } | void = await getDocuments(startAt, perPage)
    if (documents === undefined) {
      response.sendStatus(404)
      return
    }

    const items: { [field: string]: any }[] = await fetchItems(token, startAt, perPage)
    await Promise.all<any>(items.map((element) => {
      if (element.oid in documents) {
        return Promise.resolve()
      }

      const document: DocumentData = parseDocument(element.data.text)
      documents[element.oid] = document

      return collection
        .doc(element.oid)
        .set(document)
    }))

    response.send(Object.values(documents))
  })

async function getDocuments<T extends DocumentData>(startAt: number, perPage: number): Promise<DocumentSet<T>> {
  const snapshot: QuerySnapshot = await collection
    .orderBy("dateStart")
    .startAt(startAt)
    .limit(perPage)
    .get()

  return collate<T>(snapshot.docs)
}

function collate<T extends DocumentData>(documents: QueryDocumentSnapshot[], output: DocumentSet<T> = {}): DocumentSet<T> {
  documents.forEach((document: QueryDocumentSnapshot) => { output[document.id] = document.data() as T })
  return output
}

async function fetchItems<T>(token: string, startAt: number, perPage: number): Promise<T[]> {
  const response: any = await graphql(query, {
    headers: {
      authorization: `token ${token}`
    }
  })

  const conferences: T[] = response
    .repository
    .conferences
    .entries

  return conferences.slice(startAt, startAt + perPage)
}

function parseDocument<T extends DocumentData>(data: string): T {
  const documents: Document.Parsed[] = yaml
    .parseAllDocuments(data)
    .filter((document) => document.contents != null)

  if (documents.length != 1) {
    throw new Error(`Found ${documents.length} documents, expected 1`)
  }

  const json: object = documents[0].toJSON()
  return camelizeKeys(json) as T
}

type DocumentSet<T extends DocumentData> = { [key: string]: T }