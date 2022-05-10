var FoodModel = require('../models/foodModel.js');

/**
 * foodController.js
 *
 * @description :: Server-side logic for managing foods.
 */
module.exports = {

    /**
     * foodController.list()
     */
    list: function (req, res) {
        FoodModel.find(function (err, foods) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting food.',
                    error: err
                });
            }

            return res.json(foods);
        });
    },

    /**
     * foodController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        FoodModel.findOne({_id: id}, function (err, food) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting food.',
                    error: err
                });
            }

            if (!food) {
                return res.status(404).json({
                    message: 'No such food'
                });
            }

            return res.json(food);
        });
    },

    /**
     * foodController.create()
     */
    create: function (req, res) {
        var food = new FoodModel({
			name : req.body.name,
			price : req.body.price,
			rating : req.body.rating
        });

        food.save(function (err, food) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating food',
                    error: err
                });
            }

            return res.status(201).json(food);
        });
    },

    /**
     * foodController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        FoodModel.findOne({_id: id}, function (err, food) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting food',
                    error: err
                });
            }

            if (!food) {
                return res.status(404).json({
                    message: 'No such food'
                });
            }

            food.name = req.body.name ? req.body.name : food.name;
			food.price = req.body.price ? req.body.price : food.price;
			food.rating = req.body.rating ? req.body.rating : food.rating;
			
            food.save(function (err, food) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating food.',
                        error: err
                    });
                }

                return res.json(food);
            });
        });
    },

    /**
     * foodController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        FoodModel.findByIdAndRemove(id, function (err, food) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the food.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
