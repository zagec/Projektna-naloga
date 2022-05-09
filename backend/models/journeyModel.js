var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var journeySchema = new Schema({
	'start' : {
	 	type: Schema.Types.ObjectId,
	 	ref: 'location'
	},
	'end' : {
	 	type: Schema.Types.ObjectId,
	 	ref: 'location'
	},
	'startdate' : Date,
	'enddate' : Date,
	'name' : String
});

module.exports = mongoose.model('journey', journeySchema);
