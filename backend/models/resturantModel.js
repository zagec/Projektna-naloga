var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var resturantSchema = new Schema({
	'name' : String,
	'location_id' : {
	 	type: Schema.Types.ObjectId,
	 	ref: 'location'
	},
	'description' : String,
	'menu' : {
		type: Schema.Types.ObjectId,
		ref: 'resturantMenu'
	},
	'student_cupons' : Boolean,
	'working_hours' : Array
});

module.exports = mongoose.model('resturant', resturantSchema);
