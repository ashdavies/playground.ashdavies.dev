// Test: examples/get-value.js
// Get value example test
'use strict';

require('should')
var _      = require('lodash')
  , exec   = require('child_process').exec
;
describe('example', function() {
  this.timeout(30000)
  it('Get value', function(done) {
  exec('node examples/get-key.js', function (error, stdout, stderr) {
    if (error) throw error
      _.trim(stdout).should.equal('Tokyo')
      _.trim(stderr).should.equal('')
      done()
    })
  })
})
