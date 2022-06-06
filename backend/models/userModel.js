var mongoose = require('mongoose');
const bcrypt = require("bcrypt");
var Schema   = mongoose.Schema;

var userSchema = new Schema({
	'username' : String,
	'password' : String,
	'email' : String,
	'date' : Date,
	'admin' : Boolean,
	'restaurantVisits': {
		type: Array,
		"default": []
	},
	status: {
		type: String, 
		enum: ['Pending', 'Active'],
		default: 'Pending'
	  },
	confirmationCode: { 
		type: String, 
		unique: true 
	  }
});

userSchema.pre('save', function (next) {
	var user = this;
	bcrypt.hash(user.password, 10, function (err, hash) {
		if (err) {
			return next(err);
		}
		user.password = hash;
		next();
	})
})

userSchema.statics.authenticate = function (username, password, callback) {
	User.findOne({username: username})
		.exec(function (err, user) {
			if (err) {
				var err = new Error("User not found.");
				err.status = 401;
				err.message = "error";
				return callback(err);
			} else if (!user) {
				var err = new Error("User not found.");
				err.status = 401;
				err.message = "User not found";
				return callback(err);
			}

			// ce user ni active, torej je pending vrne error ker se mora verifyjat mail
			if (user.status != "Active") {
				var err = new Error("User not found.");
				err.status = 401;
				err.message = "Please verify your email before logging in";
				return callback(err);
			}


			bcrypt.compare(password, user.password, function (err, result) {
				if (result === true) {
					return callback(null, user);
				} else {
					return callback();
				}
			});
		});
}


// preveri ce je username in email prost pri registraciji
userSchema.statics.usernameEmailExists = function(username, mail, callback){
	console.log(username, mail)
	User.findOne({$or: [
		{ username : username },
		{ email: mail }
	]}).exec(function(err, user){
		if(err) return callback(err);
		else if(!user){
			var err =  new Error("User with username " + username + " already exists");
			err.status = 401;
			return callback(err);
		} 
		else{
			return callback(null, user);
		}
	})
}

var User = mongoose.model('user', userSchema);
module.exports = User;
