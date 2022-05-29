var RestaurantratingModel = require('../models/restaurantRatingModel.js');

/**
 * restaurantRatingController.js
 *
 * @description :: Server-side logic for managing restaurantRatings.
 */
module.exports = {

    /**
     * restaurantRatingController.list()
     */
    list: function (req, res) {
        RestaurantratingModel.find(function (err, restaurantRatings) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting restaurantRating.',
                    error: err
                });
            }

            return res.json(restaurantRatings);
        });
    },

    /**
     * restaurantRatingController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        RestaurantratingModel.findOne({_id: id}, function (err, restaurantRating) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting restaurantRating.',
                    error: err
                });
            }

            if (!restaurantRating) {
                return res.status(404).json({
                    message: 'No such restaurantRating'
                });
            }

            return res.json(restaurantRating);
        });
    },

    /**
     * restaurantRatingController.create()
     */
    create: function (req, res) {
        var restaurantRating = new RestaurantratingModel({
			starRating : req.body.starRating,
			opinion : req.body.opinion,
			user_tk : req.body.user_tk,
			restaurant_tk : req.body.restaurant_tk
        });

        restaurantRating.save(function (err, restaurantRating) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating restaurantRating',
                    error: err
                });
            }

            return res.status(201).json(restaurantRating);
        });
    },

    /**
     * restaurantRatingController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        RestaurantratingModel.findOne({_id: id}, function (err, restaurantRating) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting restaurantRating',
                    error: err
                });
            }

            if (!restaurantRating) {
                return res.status(404).json({
                    message: 'No such restaurantRating'
                });
            }

            restaurantRating.starRating = req.body.starRating ? req.body.starRating : restaurantRating.starRating;
			restaurantRating.opinion = req.body.opinion ? req.body.opinion : restaurantRating.opinion;
			restaurantRating.user_tk = req.body.user_tk ? req.body.user_tk : restaurantRating.user_tk;
			restaurantRating.restaurant_tk = req.body.restaurant_tk ? req.body.restaurant_tk : restaurantRating.restaurant_tk;
			
            restaurantRating.save(function (err, restaurantRating) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating restaurantRating.',
                        error: err
                    });
                }

                return res.json(restaurantRating);
            });
        });
    },

    /**
     * restaurantRatingController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        RestaurantratingModel.findByIdAndRemove(id, function (err, restaurantRating) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the restaurantRating.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    },

    getRestaurantsRatings: function(req, res){
        restId = req.params.id
        
        RestaurantratingModel.find({ 'restaurant_tk' : restId }).exec(function (err, restaurantRatings) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting restaurantRating with id ' + restId,
                    error: err
                });
            }
            return res.json(restaurantRatings);
        });
    },

    getRestaurantsRatings2: function(req, res, id){
        restId = req.params.id
        RestaurantratingModel.find({ 'restaurant_tk' : restId }).exec(function (err, restaurantRatings) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting restaurantRating.',
                    error: err
                });
            }

            return restaurantRatings;
        });
    }
};
