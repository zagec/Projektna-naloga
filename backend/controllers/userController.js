var UserModel = require('../models/userModel.js');
const jwt = require("jsonwebtoken");
const JwtModel = require("../models/jwtModel");
const config = require("../auth.config");
const nodemailer = require("nodemailer");


function generateAccessToken(username) {
    return jwt.sign(username, process.env.TOKEN_SECRET, { expiresIn: '1800s' });
}

const transport = nodemailer.createTransport({
    service: "Gmail",
    auth: {
      user: config.user,
      pass: config.pass2,
    },
    tls: {
        rejectUnauthorized: false
    }
  });

function sendConfirmationEmail(name, email, confirmationCode){
    transport.sendMail({
        from: config.user,
        to: email,
        subject: "Potrdi svoj gmail naslov",
        html: `<h1>Gmail racun</h1>
            <h2>Zdravo ${name}</h2>
            <p>Za potrdilo stisni na spodnji link</p>
            <a href=http://localhost:3000/verifyUser/${confirmationCode}> Stisni me</a>
            </div>`,
    }).catch(err => console.log(err));
};


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

        const token = jwt.sign({email: req.body.email}, config.secret)

        var user = new UserModel({
			username : req.body.username,
			password : req.body.password,
			email : req.body.email,
			date : new Date(),
            restaurantVisits : [],
            confirmationCode: token
        });
        console.log(user)

        user.save(function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating user',
                    error: err
                });
            }

            res.send({
                message:
                  "User was registered successfully! Please check your email",
             });

            sendConfirmationEmail(user.username, user.email, user.confirmationCode);

        });
    },

    login: function (req,res,next) {
        UserModel.authenticate(req.body.username, req.body.password, function(err, user){
            if(err || !user){
                var err = new Error('Wrong username or paassword');
                err.status = 401;
                return next(err);
            }
            req.session.userId = user._id;
            //res.redirect('/users/profile');
            return res.json(user);
        });
        // UserModel.authenticate(req.body.username, req.body.password, function(err,user){
        //     if(err || !user){
        //         var err = new Error("Wrong username or password");
        //         err.status = 401;
        //         return next(err);
        //     }
        //     req.session.userId = user._id

        //     const token = generateAccessToken({ username: req.body.username });
        //     var jwt = new JwtModel({
        //         token : token,
        //         user_id : user._id,
        //         date : new Date()
        //     });
        //     jwt.save(function (err, jwt) {
        //         if (err) {
        //             return res.status(500).json({
        //                 message: 'Error when creating jwt',
        //                 error: err
        //             });
        //         }
        //         return res.status(201).json(jwt);
        //     });
        // })
    },

    register: function(req, res, next){
        if(req.body.password != req.body.repPassword){
            var err =  new Error("Password does not match");
            return res.status(500).json({
                message: 'Password does not match',
                error: err
            });
        }
        UserModel.usernameEmailExists(req.body.username, req.body.mail, function(error, user){
            if(error || !user){
                module.exports.create(req, res);
            } else {
                var err = new Error("Username or mail taken");
                err.status = 401;
                return next(err);
            }
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
            if(req.body.email !== "" && req.body.email !== undefined){
                user.email = req.body.email
            }
            if(req.body.username !== "" && req.body.username !== undefined){
                user.username = req.body.username
            }
            if(req.body.admin !== "" && req.body.admin !== undefined){
                user.admin = req.body.admin
            }
            //user.username = req.body.username ? req.body.username : user.username;
			//user.password = req.body.password ? req.body.password : user.password;
			//user.email = req.body.email ? req.body.email : user.email;
			//user.date = req.body.date ? req.body.date : user.date;
            //user.admin = req.body.admin ? req.body.admin : user.admin;
			
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
    },

    
    // funkcija za povecanje obiska v doloceni restevraciji pri uporabniku.
    // ce se restevracije ni obiskal (se je ni v arrayu "restaurantVisits") se doda nov objekt z imenom restavracije in enim obiskkom,
    // ce je restavracijo ze obisskal se stevilo obiskov povecja za eno
    userVisitedRestaurant: function(req, res) {
        var id = req.params.id;

        input = { "restaurantName" : req.body.name, "numberOfVisits" : 1}
        UserModel.updateOne( { _id: id },
            [ 
                { 
                    $set: { 
                        restaurantVisits: {
                            $reduce: {
                                input: { $ifNull: [ "$restaurantVisits", [] ] }, 
                                initialValue: { RestaurantVisits: [], update: false },
                                in: {
                                    $cond: [ { $eq: [ "$$this.restaurantName", input.restaurantName ] },
                                             { 
                                               RestaurantVisits: { 
                                                  $concatArrays: [
                                                      "$$value.RestaurantVisits",
                                                      [ { restaurantName: "$$this.restaurantName", numberOfVisits: { $add: [ "$$this.numberOfVisits", 1 ] } } ],
                                                  ] 
                                                }, 
                                                update: true
                                             },
                                             { 
                                                RestaurantVisits: { 
                                                   $concatArrays: [ "$$value.RestaurantVisits", [ "$$this" ] ] 
                                                }, 
                                                update: "$$value.update" 
                                             }
                                    ]
                                }
                            }
                        }
                    }
                },
                { 
                    $set: { 
                        restaurantVisits: { 
                            $cond: [ { $eq: [ "$restaurantVisits.update", false ] },
                                     { $concatArrays: [ "$restaurantVisits.RestaurantVisits", [ input ] ] },
                                     { $concatArrays: [ "$restaurantVisits.RestaurantVisits", [] ] }
                            ] 
                        }
                    }
                }
            ] 
          )
    },

    // user dobi po registraciji na gmail, mail v katerem je link oblike localhost/users/confirm/"koda", ob kliku na gmail je redirecan na to funkcijo,
    // ki changea userjev status na active iz pending in nato se lahko prijavi
    verifyMail: function(req, res, next){
        console.log("here")    
        UserModel.findOne({ confirmationCode: req.params.code }, function (err, user) {
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

        user.status = "Active";
        user.save((err) => {
            if (err) {
            res.status(500).send({ message: err });
            return;
            }
        });

        // window.location.href = 'http://localhost:3000';
        // redirect nekam nevem se kam
        return res.json(user);
    });
      }
};
