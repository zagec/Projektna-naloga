var ResturantmenuModel = require('../models/resturantMenuModel.js');

/**
 * resturantMenuController.js
 *
 * @description :: Server-side logic for managing resturantMenus.
 */
module.exports = {

    /**
     * resturantMenuController.list()
     */
    list: function (req, res) {
        ResturantmenuModel.find(function (err, resturantMenus) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting resturantMenu.',
                    error: err
                });
            }

            return res.json(resturantMenus);
        });
    },

    /**
     * resturantMenuController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        ResturantmenuModel.findOne({_id: id}, function (err, resturantMenu) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting resturantMenu.',
                    error: err
                });
            }

            if (!resturantMenu) {
                return res.status(404).json({
                    message: 'No such resturantMenu'
                });
            }

            return res.json(resturantMenu);
        });
    },

    /**
     * resturantMenuController.create()
     */
    create: function (req, res) {
        var resturantMenu = new ResturantmenuModel({
			name : req.body.name,
			foods : req.body.foods
        });

        resturantMenu.save(function (err, resturantMenu) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating resturantMenu',
                    error: err
                });
            }

            return res.status(201).json(resturantMenu);
        });
    },

    /**
     * resturantMenuController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        ResturantmenuModel.findOne({_id: id}, function (err, resturantMenu) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting resturantMenu',
                    error: err
                });
            }

            if (!resturantMenu) {
                return res.status(404).json({
                    message: 'No such resturantMenu'
                });
            }

            resturantMenu.name = req.body.name ? req.body.name : resturantMenu.name;
			resturantMenu.foods = req.body.foods ? req.body.foods : resturantMenu.foods;
			
            resturantMenu.save(function (err, resturantMenu) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating resturantMenu.',
                        error: err
                    });
                }

                return res.json(resturantMenu);
            });
        });
    },

    /**
     * resturantMenuController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        ResturantmenuModel.findByIdAndRemove(id, function (err, resturantMenu) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the resturantMenu.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
