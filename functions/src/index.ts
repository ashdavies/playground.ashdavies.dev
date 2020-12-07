"use strict"

import axios, { AxiosResponse } from "axios"
import yaml, { Document } from "yaml"

import * as admin from "firebase-admin"
import * as functions from "firebase-functions"

import type {
  CollectionReference,
  DocumentData,
  DocumentReference,
  QueryDocumentSnapshot,
  QuerySnapshot,
  WriteResult,
} from "@google-cloud/firestore"

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

    const items: Item[] = await fetchItems(startAt, perPage)

    items.forEach(async element => {
      if (element.sha in documents) {
        return
      }

      const entry: DocumentData | void = await fetchDocument<DocumentData>(element.download_url)
        .then((document: DocumentData) => setDocument(document.sha, document))
        .catch((error) => console.log(error))

      if (entry === undefined) {
        return
      }

      documents[entry.sha] = entry
    })

    response.send(documents)
  })

function getDocuments<T = DocumentData>(startAt: number, perPage: number): Promise<{ [key: string]: T }> {
  return collection
    .orderBy("dateStart")
    .startAt(startAt)
    .limit(perPage)
    .get()
    .then((snapshot: QuerySnapshot) => collate<T>(snapshot.docs))
}

function collate<T = DocumentData>(documents: QueryDocumentSnapshot[], into: { [key: string]: T } = {}): { [key: string]: T } {
  documents.forEach((document: QueryDocumentSnapshot) => { into[document.id] = document.data() as T })
  return into
}

// TODO: Generify response to remove Item type (use only sha and download_url)
function fetchItems(startAt: number, perPage: number): Promise<Item[]> {
  return axios
    .get("https://api.github.com/repos/AndroidStudyGroup/conferences/contents/_conferences")
    .then(async (response: AxiosResponse<Item[]>) => response.data.slice(startAt, startAt + perPage))
}

function fetchDocument<T = DocumentData>(url: string): Promise<T> {
  return axios
    .get(url)
    .then((response: AxiosResponse<string>) => parseDocument<T>(response.data))
}

// TODO Automatically convert snake case to lower camel case
function parseDocument<T>(data: string): T {
  const documents: Document.Parsed[] = yaml
    .parseAllDocuments(data)
    .filter((document) => document.contents != null)

  if (documents.length != 0) {
    throw new Error(`Parsing failed for ${JSON.stringify(data)}`)
  }

  return documents[0].toJSON() as T
}

function toCamelCase<T>(data: T): T {

}

function toCamelCase(value: string): string {
  return value
    .split("_")
}

const snakeToCamel = (str) => str.replace(
    /([-_][a-z])/g,
    (group) => group.toUpperCase()
                    .replace('-', '')
                    .replace('_', '')
);

snakeToCamel('my-snake-string'); // mySnakeString

function setDocument<T = DocumentData>(id: string, data: T): Promise<T> {
  return collection
    .doc(id)
    .set(data)
    .then((result: WriteResult) => data)
}