"use strict"

import { camelizeKeys } from "humps"
import { graphql } from "@octokit/graphql"
import yaml, { Document } from "yaml"

import * as admin from "firebase-admin"
import * as functions from "firebase-functions"

import type {
  CollectionReference,
  QuerySnapshot,
} from "@google-cloud/firestore"

import type {
  FunctionBuilder,
  HttpsFunction,
  Response,
} from "firebase-functions"

import type {
  Request
} from "firebase-functions/lib/providers/https"

const EUROPE_WEST1: string = "europe-west1"
const region: FunctionBuilder = functions.region(EUROPE_WEST1)

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

export const conferences: HttpsFunction = region.https.onRequest(async (request: Request, response: Response) => {
  const limit: number = request.query.limit !== undefined ? +request.query.limit : 10
  const startAt: string | undefined = request.query.startAt as string | undefined

  // User provided token required for GitHub GraphQL queries
  const token: string | undefined = request.query.token as string | undefined
  if (token === undefined) {
    response.sendStatus(400)
    return
  }

  // Retrieve Firestore conferences collection
  const collection: CollectionReference = admin
    .firestore()
    .collection("conferences")

  // Apply request parameters to collection query
  console.log(`?orderBy=dateStart&startAt=${startAt}&limit=${limit}`)
  const snapshot: QuerySnapshot = await collection
    .orderBy("dateStart")
    .startAt(startAt)
    .limit(limit)
    .get()

  // Use Record because Map doesn't serialize
  const documents: Record<string, Conference> = {}
  let length: number = 0

  // Populate records from snapshot
  for (let document of snapshot.docs) {
    documents[document.id] = document.data() as Conference
    console.log(document.id)
    length++
  }

  // Return eagerly if request query is already fulfilled
  if (length >= limit) {
    console.log(`Request fulfilled with ${length}`)
    response.json(Object.values(documents))
    return
  }

  // Fetch all conference items from GitHub GraphQL API
  // TODO: Replace TreeEntry with Record<string, GitHubObject>
  const result: Array<GitHubObject & TreeEntry> = await graphql<Repository>(query, {
    headers: {
      authorization: `token ${token}`
    }
  }).then((it: Repository) => it.repository.conferences.entries)

  // Parse yaml document contents converting keys to camel case
  const items: Array<Conference & TreeEntry> = result
    .filter((it: GitHubObject & TreeEntry) => !(it.oid in documents))
    .map((it: GitHubObject & TreeEntry) => {
      const parsed: Document.Parsed[] = yaml
        .parseAllDocuments(it.data.text)
        .filter((it: Document.Parsed) => it.contents != null)

      if (parsed.length != 1) {
        throw new Error(`Found ${parsed.length} documents, expected 1`)
      }

      const json: Record<string, any> = parsed[0].toJSON()

      return {
        ...camelizeKeys(json),
        oid: it.oid,
      } as Conference & TreeEntry
    })

  // Verify documents up-to-date
  for (let oid in documents) {
    if (oid in items) {
      continue
    }

    collection
      .doc(oid)
      .delete()
  }

  // GitHub doesn't provide ordering or pagination on content APIs
  // TODO: Cache date parsing or store in Firestore as timestamp
  let paginated: Array<Conference & TreeEntry> = items.sort((lhs, rhs) => {
    const lhsds: Date = new Date(lhs.dateStart)
    const rhsds: Date = new Date(rhs.dateStart)
    return rhsds.getTime() - lhsds.getTime()
  })

  if (startAt !== undefined) {
    const startAtDate: Date = new Date(startAt)
    paginated = paginated.filter((it: Conference) => {
      return new Date(it.dateStart) > startAtDate
    })
  }

  // Populate documents until request fulfilled
  paginated.forEach((it: Conference & TreeEntry) => {
    if (length >= limit) {
      return
    }

    documents[it.oid] = it
    length++
  })

  // Return successful result omitting keys
  response.json(Object.values(documents))

  // Store records in Firestore
  paginated.forEach(async (it: Conference & TreeEntry) => {
    await collection
      .doc(it.oid)
      .set(it)
  })
})