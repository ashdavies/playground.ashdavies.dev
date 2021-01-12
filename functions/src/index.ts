"use strict"

import admin from "firebase-admin"
import { camelizeKeys } from "humps"
import * as functions from "firebase-functions"
import { graphql } from "@octokit/graphql"
import yaml, { Document } from "yaml"

/* eslint-disable no-duplicate-imports */
import type {
  FunctionBuilder,
  HttpsFunction,
  Response,
} from "firebase-functions"
/* eslint-enable no-duplicate-imports */

import type {
  CollectionReference,
  QuerySnapshot,
  Query,
} from "@google-cloud/firestore"

import type {
  Request,
} from "firebase-functions/lib/providers/https"

admin.initializeApp()

/**
 * TODO Use function global variables to reuse objects for expensive object caching
 * https://cloud.google.com/functions/docs/bestpractices/tips#use_global_variables_to_reuse_objects_in_future_invocations
 *
 * TODO
 *  - Store index of conference elements with tree SHA
 *  - Retrieve all documents via GraphQL API endpoint
 *  - Perform diff against conference index
 *  - Update Firestore database from diff
 *  - Respond with paginated request
 */
const region: FunctionBuilder = functions.region("europe-west1")

const graphQlQuery: string = `query Conferences {
  repository(owner: "AndroidStudyGroup", name: "conferences") {
    conferences: object(expression: "HEAD:_conferences") {
      ... on Tree {
        oid
        entries {
          oid
          name
          data: object {
            ... on Blob {
              oid
              text
            }
          }
        }
      }
    }
  }
}`

// TODO Extract request query into parsing class
export const conferences: HttpsFunction = region.https.onRequest(async (request: Request, response: Response) => {
  const limit: number = request.query.limit !== undefined ? +request.query.limit : 10
  const startAt: string | undefined = request.query.startAt as string | undefined

  // User provided token required for GitHub GraphQL queries
  // TODO Regsiter application access token in env secrets
  const token: string | undefined = request.query.token as string | undefined
  if (token === undefined) {
    response.sendStatus(400)
    return
  }

  // Retrieve Firestore conferences collection
  // TODO Use .withConverter() to convert String to Date object
  const collection: CollectionReference = admin
    .firestore()
    .collection("conferences")

  // Apply request parameters to collection query
  let query: Query = collection
    .orderBy("dateStart", "desc")
    .limit(limit)

  // Apply startAt parameter to query
  // TODO Check for changes against HEAD if page is not first
  // TODO Store last seen id to compare against
  if (startAt !== undefined) {
    query = query.startAt(startAt)
  }

  // Retrieve snapshot from query
  const snapshot: QuerySnapshot = await collection.get()
  console.log(`?orderBy=dateStart&startAt=${startAt}&limit=${limit}`)

  // Use Record because Map doesn't serialize
  const documents: Record<string, Conference> = {}
  let length: number = 0

  // Populate records from snapshot
  for (const document of snapshot.docs) {
    const data: Conference = document.data() as Conference
    if (startAt !== undefined) {
      const startAtDate = new Date(startAt)
      console.log(`Asserting that ${data.dateStart} (${typeof data.dateStart}) > ${startAtDate} (${typeof startAtDate})`)
      if (data.dateStart > startAtDate) {
        throw new Error(`Requested documents starting at ${startAt} but received ${data.dateStart} with oid ${document.id}`)
      }
    }

    documents[document.id] = document.data() as Conference
    length++
  }

  // Return eagerly if request query is already fulfilled
  // TODO Firestore will return the limit amount if available
  if (length >= limit) {
    console.log(`Request satisfied by Firestore query, requested ${limit}, got ${length}`)
    response.json(Object.values(documents))
    return
  }

  // Fetch all conference items from GitHub GraphQL API
  // TODO: Replace TreeEntry with Record<string, Blob>
  const result: Array<Blob & TreeEntry> = await graphql<Repository>(graphQlQuery, {
    headers: {
      authorization: `token ${token}`,
    },
  }).then((it: Repository) => it.repository.conferences.entries)

  // Parse yaml document contents converting keys to camel case
  const items: Array<Conference & TreeEntry> = result
    .filter((it: Blob & TreeEntry) => !(it.oid in documents))
    .map((it: Blob & TreeEntry) => {
      const parsed: Document.Parsed[] = yaml
        .parseAllDocuments(it.data.text)
        .filter((item: Document.Parsed) => item.contents !== null)

      if (parsed.length !== 1) {
        throw new Error(`Found ${parsed.length} documents, expected 1`)
      }

      const json: Record<string, any> = parsed[0].toJSON()

      return {
        ...camelizeKeys(json),
        oid: it.oid,
      } as Conference & TreeEntry
    })

  // Verify documents up-to-date
  for (const oid in documents) {
    if (oid in items) {
      continue
    }

    console.log(`Deleting document ${oid}`)

    collection
      .doc(oid)
      .delete()
      .catch((error: any) => console.log(`Failed to delete document ${oid}`, error))
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
      return new Date(it.dateStart) < startAtDate
    })
  }

  paginated = paginated.slice(0, limit)

  // Populate documents until request fulfilled
  paginated.forEach((it: Conference & TreeEntry) => {
    if (length >= limit) {
      return
    }

    documents[it.oid] = it
    length++
  })

  /**
   * Return successful result omitting keys
   * 
   * TODO Cache entries with response headers for matching keys
   * https://firebase.google.com/docs/hosting/manage-cache
   */
  response.json(Object.values(documents))

  // Store records in Firestore
  paginated.forEach(async (it: Conference & TreeEntry) => {
    await collection
      .doc(it.oid)
      .set(it)
  })
})
