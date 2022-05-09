var JwtModel = require('../models/jwtModel.js');

/**
 * jwtController.js
 *
 * @description :: Server-side logic for managing jwts.
 */
module.exports = {

    /**
     * jwtController.list()
     */
    list: function (req, res) {
        JwtModel.find(function (err, jwts) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting jwt.',
                    error: err
                });
            }

            return res.json(jwts);
        });
    },

    /**
     * jwtController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        JwtModel.findOne({_id: id}, function (err, jwt) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting jwt.',
                    error: err
                });
            }

            if (!jwt) {
                return res.status(404).json({
                    message: 'No such jwt'
                });
            }

            return res.json(jwt);
        });
    },

    /**
     * jwtController.create()
     */
    create: function (req, res) {
        var jwt = new JwtModel({
			token : req.body.token,
			user_id : req.body.user_id,
			date : req.body.date
        });

        jwt.save(function (err, jwt) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating jwt',
                    error: err
                });
            }

            return res.status(201).json(jwt);
        });
    },

    /**
     * jwtController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        JwtModel.findOne({_id: id}, function (err, jwt) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting jwt',
                    error: err
                });
            }

            if (!jwt) {
                return res.status(404).json({
                    message: 'No such jwt'
                });
            }

            jwt.token = req.body.token ? req.body.token : jwt.token;
			jwt.user_id = req.body.user_id ? req.body.user_id : jwt.user_id;
			jwt.date = req.body.date ? req.body.date : jwt.date;
			
            jwt.save(function (err, jwt) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating jwt.',
                        error: err
                    });
                }

                return res.json(jwt);
            });
        });
    },

    /**
     * jwtController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        JwtModel.findByIdAndRemove(id, function (err, jwt) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the jwt.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
