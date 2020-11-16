// examples/del-key.js
// Delete key
'use strict';

var Firestore = require('../index')

new Firestore({ name: 'pub' }, function firestoreInitialized(err, firestore) {
  if (err) {
    console.log('ERROR', err)
    process.exit(1)
  }
  firestore.del('somewhere', function(err) {
    if (!err) console.log('unexpected')
    process.exit(0)
  })
})
