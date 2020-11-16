// examples/set-value.js
// Set value
'use strict';

var Firestore = require('../index')

new Firestore({ name: 'pub' }, function firestoreInitialized(err, firestore) {
  if (err) {
    console.log('ERROR', err)
    process.exit(1)
  }
  firestore.set('somewhere', 'value', function(err) {
    if (!err) console.log('unexpected')
    process.exit(0)
  })
})
