var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var locationSchema = new Schema({
	'name' : String,
	'longitude' : Number, //geojson format za kordinate
	'latitude' : Number, //geojson format za kordinate
});

module.exports = mongoose.model('location', locationSchema);
