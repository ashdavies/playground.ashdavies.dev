// firestore - Firebase implementation of a simple key/value store - Copyright (C) 2015 by yieme - All Rights Reserved - License: MIT

;(function() {
  'use strict';

  var _                      = require('lodash')
    , Firebase               = require('firebase')
    , FirebaseTokenGenerator = require('firebase-token-generator')

  function Firestore(options, callback) { // TODO: co upgrade
    if(!_.isFunction(callback)) {
      return _.partial(Firestore, options)
    }

    var firestore = this
    options       = options || {}
    options.child = options.child || 'kv'
    options.path  = options.path || options.child
    if (options.name) options.url = 'https://' + options.name + '.firebaseio.com/'
    if (options.url && options.url.substr(-1) != '/') options.url += '/'
    var Ref = options.ref || (options.url && new Firebase(options.url)) || false
    if (!Ref) {
      callback(new Error("Firestore: ref or url required"))
      return
    }

    function complete() {
      callback(null, firestore)
    }

    if (options.path) {
      Ref = Ref.child(options.path)
    } else {
      var path = '/' + Ref.toString().split('.com/')[1]
      Ref = Ref.root().child(path)  // generate new firebase reference
    }

    firestore.get = function (key, callback) { // get key value
      if(!_.isFunction(callback)) {
        return _.partial(Firestore.get, key)
      }

      var ref = Ref.child(key)
      ref.once('value', function (snap) {
        var val = snap.val()
        callback(null, val)
      }, callback);
    }



    firestore.set = function (key, value, callback) { // set key/value
      if(callback && !_.isFunction(callback)) {
        return _.partial(Firestore.set, key, value)
      }

      try {
        Ref.child(key).set(value, callback)
      } catch (err) {
        if (callback) {
          callback(err)
        } else {
          throw err
        }
      }
    }



    firestore.del = function (key, callback) { // clear key
      if(callback && !_.isFunction(callback)) {
        return _.partial(Firestore.del, key)
      }

      Ref.child(key).remove(callback);
    }



    if (options.token) {
      var token      = options.token
      if (options.auth) {
        var tokenGen = new FirebaseTokenGenerator(options.token)
        token        = tokenGen.createToken(options.auth)
      }
      try {
        Ref.authWithCustomToken(token, complete)
      } catch (err) { callback(err) }
    } else {
      complete()
    }

    if (options.autoRemove) {
      Ref.onDisconnect().remove()
    }
  }



  if (typeof exports === 'object') module.exports = Firestore // support CommonJS
  // else if (typeof define === 'function' && define.amd) define(function() { return Firestore }) // support AMD
  // else this.Firestore = Firestore // support browser
})();
