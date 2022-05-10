var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var resturantMenuSchema = new Schema({
	'name' : String,
	'foods' : Array
});

module.exports = mongoose.model('resturantMenu', resturantMenuSchema);
