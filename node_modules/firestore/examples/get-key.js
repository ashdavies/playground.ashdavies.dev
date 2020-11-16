// examples/get-key.js
// Get value for a key
'use strict';

var Firestore = require('../index')

new Firestore({ name: 'pub', path: 'AWS/zone/ap-northeast-1' }, function firestoreInitialized(err, firestore) {
  if (err) {
    console.log('ERROR', err)
    process.exit(1)
  }
  firestore.get('city', function(err, value) {
    console.log((err) ? err.code : value)
    process.exit(0)
  })
})
