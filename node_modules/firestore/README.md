# firestore <img src="https://raw.githubusercontent.com/yieme/firestore/master/firestore.png" align="right" height="165">
Firebase implementation of a simple key/value store

## Install

```js
npm install firestore --save
```
<!-- EXAMPLES:BEGIN -->
## Examples

### [Del key](examples/del-key.js)

```js
var Firestore = require('firestore')

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
```

### [Get key](examples/get-key.js)

```js
var Firestore = require('firestore')

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
```

### [Set value](examples/set-value.js)

```js
var Firestore = require('firestore')

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
```
<!-- EXAMPLES:END -->
# Inspiriation

[callback free harmonious node](http://zef.me/6096/callback-free-harmonious-node-js/)

## License: MIT
