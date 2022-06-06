var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var restaurantRatingSchema = new Schema({
	// rating: rating,
    //             date: new Date(),
    //             user: userContext.user._id,
    //             photo: restId
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
	}, 
	'date': Date
});

module.exports = mongoose.model('restaurantRating', restaurantRatingSchema);
