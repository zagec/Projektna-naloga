var UserModel = require('../models/userModel.js');
const jwt = require("jsonwebtoken");
const JwtModel = require("../models/jwtModel");


function generateAccessToken(username) {
    return jwt.sign(username, process.env.TOKEN_SECRET, { expiresIn: '1800s' });
}

/**
 * userController.js
 *
 * @description :: Server-side logic for managing users.
 */
module.exports = {

    /**
     * userController.list()
     */
    list: function (req, res) {
        UserModel.find(function (err, users) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user.',
                    error: err
                });
            }

            return res.json(users);
        });
    },

    showLogin: function(req,res){
        return res.json()
    },

    showRegister: function(req,res){
        return res.json()
    },

    /**
     * userController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        UserModel.findOne({_id: id}, function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user.',
                    error: err
                });
            }

            if (!user) {
                return res.status(404).json({
                    message: 'No such user'
                });
            }

            return res.json(user);
        });
    },

    /**
     * userController.create()
     */
    create: function (req, res) {
        var user = new UserModel({
			username : req.body.username,
			password : req.body.password,
			email : req.body.email,
			date : new Date()
        });

        user.save(function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating user',
                    error: err
                });
            }

            return res.status(201).json(user);
        });
    },
    login: function (req,res,next) {
        UserModel.authenticate(req.body.username, req.body.password, function(err,user){
            if(err || !user){
                var err = new Error("Wrong username or password");
                err.status = 401;
                return next(err);
            }
            req.session.userId = user._id

            const token = generateAccessToken({ username: req.body.username });
            var jwt = new JwtModel({
                token : token,
                user_id : user._id,
                date : new Date()
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
        })
    },

    /**
     * userController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        UserModel.findOne({_id: id}, function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user',
                    error: err
                });
            }

            if (!user) {
                return res.status(404).json({
                    message: 'No such user'
                });
            }

            user.username = req.body.username ? req.body.username : user.username;
			user.password = req.body.password ? req.body.password : user.password;
			user.email = req.body.email ? req.body.email : user.email;
			user.date = req.body.date ? req.body.date : user.date;
			
            user.save(function (err, user) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating user.',
                        error: err
                    });
                }

                return res.json(user);
            });
        });
    },

    /**
     * userController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        UserModel.findByIdAndRemove(id, function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the user.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
