var ResturantModel = require('../models/resturantModel.js');
var restaurantRatingController = require('../controllers/restaurantRatingController.js');
const fetch = require("node-fetch");
/**
 * resturantController.js
 *
 * @description :: Server-side logic for managing resturants.
 */
module.exports = {

    /**
     * resturantController.list()
     */
     removeAll: function(req, res){
         console.log('asdd')
        ResturantModel.remove({}).exec(function (err, resturant) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the resturant.',
                    error: err
                });
            }

            return res.status(204).json();
        });
     },

    list: function (req, res) {
        ResturantModel.find(function (err, resturants) {

            if (err) {
                return res.status(500).json({
                    message: 'Error when getting resturant.',
                    error: err
                });
            }

            // resturants.map((rest) => {
            //     const getRestaurants = async function(){
            //         const res = await fetch('http://localhost:3001/restaurantRating/fromRestaurant/'+rest._id);
            //         const data = await res.json();
            //         // rest.ratings=data
            //         // console.log(rest._id + data.length)
            //     }
            //     getRestaurants()
            //     // const ratings = restaurantRatingController.getRestaurantsRatings2(rest._id)
            //     // rest.ratings.push(ratings)
            // });

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

    // vrne restavracije v okolici kroga z dolocenim polmerom, katerega doloci uporabnik 
    showResturantsInRadius: async function (req, res) {
        desiredRadius = req.params.radius
        latitude = req.params.latitude
        longitude = req.params.longitude
        console.log(latitude +  ' ' +longitude + ' ' + desiredRadius/3963.2)

            ResturantModel.find({
                        loc:  { $geoWithin: { $centerSphere: [ [latitude, longitude], desiredRadius/3963.2 ] } }    //The query converts the distance to radians by dividing by the approximate equatorial radius of the earth, 3963.2 miles
                
                }).exec(function (err, resturants) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when getting resturants.',
                        error: err
                    });
                }
    
                return res.json(resturants);
            })
        

        function error(error){
            var errorMsg = ""
            switch(error.code) {
                case error.PERMISSION_DENIED:
                    errorMsg = "User denied the request for Geolocation."
                    break;
                case error.POSITION_UNAVAILABLE:
                    errorMsg = "Location information is unavailable."
                    break;
                case error.UNKNOWN_ERROR:
                    errorMsg = "An unknown error occurred."
                    break;
                }

              return res.status(500).json({
                message: errorMsg,
                error: err
            });
        }
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
            
            // gettanje ratings iz restaurant ratings controllerja
            // var ratings = restaurantRatingController.getRestaurantsRatings(req, res);

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
    },

};
