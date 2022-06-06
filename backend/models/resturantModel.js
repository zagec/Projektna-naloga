var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var resturantSchema = new Schema({
	'ime' : String,
	'lokacija' : String,
	'cenaSStudentskimBonom' : String,
	'cenaBrezStudentskegaBona' : String,
	'ponudbaPoVrstiHrane' : Array,
	'meni' : Array,
	'loc' : Array,
	'delovniCas' : Array
});

module.exports = mongoose.model('resturant', resturantSchema);
