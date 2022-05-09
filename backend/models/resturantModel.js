var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var resturantSchema = new Schema({
	'name' : String,
	'location_id' : {
	 	type: Schema.Types.ObjectId,
	 	ref: 'location'
	},
	'description' : String,
	'meni' : String, //svoj model
	'student_cupons' : Boolean,
	'working_hours' : Array
	// ocene jedi v restavracijah
});

module.exports = mongoose.model('resturant', resturantSchema);
