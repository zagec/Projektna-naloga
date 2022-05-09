var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var locationSchema = new Schema({
	'name' : String,
	'cordinates' : String, //geojson format za kordinate
	'description' : String
});

module.exports = mongoose.model('location', locationSchema);
