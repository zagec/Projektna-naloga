var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var foodSchema = new Schema({
	'name' : String,
	'price' : Number,
	'rating' : Number
});

module.exports = mongoose.model('food', foodSchema);
