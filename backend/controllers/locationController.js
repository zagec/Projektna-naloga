var LocationModel = require('../models/locationModel.js');

/**
 * locationController.js
 *
 * @description :: Server-side logic for managing locations.
 */
module.exports = {

    /**
     * locationController.list()
     */
    list: function (req, res) {
        LocationModel.find(function (err, locations) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting location.',
                    error: err
                });
            }

            return res.json(locations);
        });
    },

    /**
     * locationController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        LocationModel.findOne({_id: id}, function (err, location) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting location.',
                    error: err
                });
            }

            if (!location) {
                return res.status(404).json({
                    message: 'No such location'
                });
            }

            return res.json(location);
        });
    },

    /**
     * locationController.create()
     */
    create: function (req, res) {
        var location = new LocationModel({
			name : req.body.name,
			cordinates : req.body.cordinates,
			description : req.body.description
        });

        location.save(function (err, location) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating location',
                    error: err
                });
            }

            return res.status(201).json(location);
        });
    },

    /**
     * locationController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        LocationModel.findOne({_id: id}, function (err, location) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting location',
                    error: err
                });
            }

            if (!location) {
                return res.status(404).json({
                    message: 'No such location'
                });
            }

            location.name = req.body.name ? req.body.name : location.name;
			location.cordinates = req.body.cordinates ? req.body.cordinates : location.cordinates;
			location.description = req.body.description ? req.body.description : location.description;
			
            location.save(function (err, location) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating location.',
                        error: err
                    });
                }

                return res.json(location);
            });
        });
    },

    /**
     * locationController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        LocationModel.findByIdAndRemove(id, function (err, location) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the location.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
