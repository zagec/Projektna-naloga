var JourneyModel = require('../models/journeyModel.js');

/**
 * journeyController.js
 *
 * @description :: Server-side logic for managing journeys.
 */
module.exports = {

    /**
     * journeyController.list()
     */
    list: function (req, res) {
        JourneyModel.find(function (err, journeys) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting journey.',
                    error: err
                });
            }

            return res.json(journeys);
        });
    },

    /**
     * journeyController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        JourneyModel.findOne({_id: id}, function (err, journey) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting journey.',
                    error: err
                });
            }

            if (!journey) {
                return res.status(404).json({
                    message: 'No such journey'
                });
            }

            return res.json(journey);
        });
    },

    /**
     * journeyController.create()
     */
    create: function (req, res) {
        var journey = new JourneyModel({
			start : req.body.start,
			end : req.body.end,
			time : req.body.time,
			name : req.body.name
        });

        journey.save(function (err, journey) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating journey',
                    error: err
                });
            }

            return res.status(201).json(journey);
        });
    },

    /**
     * journeyController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        JourneyModel.findOne({_id: id}, function (err, journey) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting journey',
                    error: err
                });
            }

            if (!journey) {
                return res.status(404).json({
                    message: 'No such journey'
                });
            }

            journey.start = req.body.start ? req.body.start : journey.start;
			journey.end = req.body.end ? req.body.end : journey.end;
			journey.time = req.body.time ? req.body.time : journey.time;
			journey.name = req.body.name ? req.body.name : journey.name;
			
            journey.save(function (err, journey) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating journey.',
                        error: err
                    });
                }

                return res.json(journey);
            });
        });
    },

    /**
     * journeyController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        JourneyModel.findByIdAndRemove(id, function (err, journey) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the journey.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
