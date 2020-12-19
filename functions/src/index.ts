"use strict"

import axios, { AxiosResponse } from "axios"
import { camelizeKeys } from "humps"
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
 * - GitHub API request will return summary of all conferences sorted by file name
 * - Firebase collections API will return documents each incurring a read charge sorted by key
 * - 
 */

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

    const documents: { [key: string]: DocumentData } | void = await getDocuments(startAt, perPage)
    if (documents === undefined) {
      response.sendStatus(404)
      return
    }

    const items: { [field: string]: any }[] = await fetchItems(startAt, perPage)
    await Promise.all(items.map((element) => {
      if (element.sha in documents) {
        return Promise.resolve()
      }

      return fetchDocument<DocumentData>(element.download_url)
        .then(async (document: DocumentData) => {
          documents[element.sha] = document
          console.log(document.dateStart) // Unordered
          
          return await collection
            .doc(element.sha)
            .set(document)
        })
        .catch((error) => console.log(error))
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

function fetchItems(startAt: number, perPage: number): Promise<{ [field: string]: any }[]> {
  return axios
    .get("https://api.github.com/repos/AndroidStudyGroup/conferences/contents/_conferences")
    .then(async (response: AxiosResponse<{ [field: string]: any }>) => response.data.slice(startAt, startAt + perPage))
}

async function fetchDocument<T extends DocumentData>(url: string): Promise<T> {
  const response = await axios.get(url)
  return parseDocument(response.data)
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