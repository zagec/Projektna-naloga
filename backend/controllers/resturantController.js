var ResturantModel = require('../models/resturantModel.js');

/**
 * resturantController.js
 *
 * @description :: Server-side logic for managing resturants.
 */
module.exports = {

    /**
     * resturantController.list()
     */
    list: function (req, res) {
        ResturantModel.find(function (err, resturants) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting resturant.',
                    error: err
                });
            }

            return res.json(resturants);
        });
    },

    showResturantsWithLocations: async function (req, res) {
        let resturants = await ResturantModel.aggregate([
            {
                "$lookup": {
                    "from": "location",
                    "localField": "location_id",
                    "foreignField": "id",
                    "as": "lokacija"
                }
            }

        ]).exec()
        return res.json(resturants)
    },

    showResturantLocation: async function (req, res) {
        let id = req.params.id
        let resturant = await ResturantModel.aggregate([
            {"$match": {"id": id}},
            {
                "$lookup": {
                    "from": "location",
                    "localField": "location_id",
                    "foreignField": "id",
                    "as": "lokacija"
                }
            }
        ]).exec()
        return res.json(resturant)
    },

    showResturantMenu: async function (req, res) {
        let id = req.params.id
        let resturant = await ResturantModel.aggregate([
            {"$match": {"id": id}},
            {
                "$lookup": {
                    "from": "resturantMenu",
                    "localField": "menu",
                    "foreignField": "id",
                    "as": "meni"
                }
            },
            {"$unwind": "meni"},
            {"$unwind": "meni.foods"},
            {
                "$lookup": {
                    "from": "food",
                    "localField": "foods",
                    "foreignField": "id",
                    "as": "food"
                }
            }
        ]).exec()
        return res.json(resturant)
    },

    showResturantsInRadius: function () {
        //TODO: function implementation
    },

    showOnlyStudentCuponResturants: function(req,res){
        let resturants = {}
        ResturantModel.find({"student_cupons": true}).then((data)=>{
            resturants = data
        })
        return res.json(resturants)
    },

    /**
     * resturantController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        ResturantModel.findOne({_id: id}, function (err, resturant) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting resturant.',
                    error: err
                });
            }

            if (!resturant) {
                return res.status(404).json({
                    message: 'No such resturant'
                });
            }

            return res.json(resturant);
        });
    },

    /**
     * resturantController.create()
     */
    create: function (req, res) {
        var resturant = new ResturantModel({
            name: req.body.name,
            location_id: req.body.location_id,
            description: req.body.description,
            student_cupons: req.body.student_cupons,
            working_hours: req.body.working_hours
        });

        resturant.save(function (err, resturant) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating resturant',
                    error: err
                });
            }

            return res.status(201).json(resturant);
        });
    },

    /**
     * resturantController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        ResturantModel.findOne({_id: id}, function (err, resturant) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting resturant',
                    error: err
                });
            }

            if (!resturant) {
                return res.status(404).json({
                    message: 'No such resturant'
                });
            }

            resturant.name = req.body.name ? req.body.name : resturant.name;
            resturant.location_id = req.body.location_id ? req.body.location_id : resturant.location_id;
            resturant.description = req.body.description ? req.body.description : resturant.description;
            resturant.student_cupons = req.body.student_cupons ? req.body.student_cupons : resturant.student_cupons;
            resturant.working_hours = req.body.working_hours ? req.body.working_hours : resturant.working_hours;

            resturant.save(function (err, resturant) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating resturant.',
                        error: err
                    });
                }

                return res.json(resturant);
            });
        });
    },

    /**
     * resturantController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        ResturantModel.findByIdAndRemove(id, function (err, resturant) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the resturant.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
