var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var locationSchema = new Schema({
	'name' : String,
	'loc' : Array
});

module.exports = mongoose.model('location', locationSchema);
