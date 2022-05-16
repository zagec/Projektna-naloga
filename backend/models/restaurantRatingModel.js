var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var restaurantRatingSchema = new Schema({
	'starRating' :{
		type: Number,
		min: 0,
		max: 5
	},
	'opinion' : String,
	'user_tk' : {
	 	type: Schema.Types.ObjectId,
	 	ref: 'user'
	},
	'restaurant_tk' : {
	 	type: Schema.Types.ObjectId,
	 	ref: 'resturant'
	}
});

module.exports = mongoose.model('restaurantRating', restaurantRatingSchema);
