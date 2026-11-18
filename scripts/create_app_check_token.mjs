#!/usr/bin/env -S npx -y -p firebase-admin node
import { createRequire } from 'node:module'
import { delimiter } from 'node:path'

const require = createRequire(`${process.env.PATH.split(delimiter)[0]}/_`)
const { initializeApp } = require('firebase-admin/app')
const { getAppCheck } = require('firebase-admin/app-check')

initializeApp()

console.log((await getAppCheck().createToken(process.argv[2])).token)
