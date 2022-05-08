var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var jwtSchema = new Schema({
	'token' : String,
	'user_id' : {
	 	type: Schema.Types.ObjectId,
	 	ref: 'user'
	},
	'date' : Date
});

module.exports = mongoose.model('jwt', jwtSchema);
