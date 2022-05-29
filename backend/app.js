var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
const dotenv = require('dotenv')
const config = require("./auth.config");
const { default: mongoose } = require('mongoose');
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/userRoutes');
var foodRouter = require('./routes/foodRoutes');
var journeyRouter = require('./routes/journeyRoutes');
var jwtRouter = require('./routes/jwtRoutes');
var locationRouter = require('./routes/locationRoutes');
var restaurantRatingRouter = require('./routes/restaurantRatingRoutes');
var restaurantMenuRouter = require('./routes/resturantMenuRoutes');
var restaurantsRouter = require('./routes/resturantRoutes');
const jwt = require("jsonwebtoken");


// get config vars
//dotenv.config();
// access config var
//process.env.TOKEN_SECRET;


const { MongoClient, ServerApiVersion } = require('mongodb');

process.env.DB_USERNAME = config.databaseUsername;
process.env.DB_PASSWORD = config.databasePassword;

const mongoDB = 'mongodb+srv://'+ process.env.DB_USERNAME+':'+process.env.DB_PASSWORD+'@cluster0.urwjc.mongodb.net/Prikazatelj_Restavracij';

mongoose.connect(mongoDB).then(() => {
  
}).catch((err) => console.log('not connected'))

mongoose.Promise = global.Promise;
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'MongoDB connection error:'));
var app = express();

var cors = require('cors');
var allowedOrigins = ['http://localhost:3000', 'http://localhost:3001'];
app.use(cors({
  credentials: true,
  origin: function(origin, callback){
    // Allow requests with no origin (mobile apps, curl)
    if(!origin) return callback(null, true);
    if(allowedOrigins.indexOf(origin)===-1){
      var msg = "The CORS policy does not allow access from the specified Origin.";
      return callback(new Error(msg), false);
    }
    return callback(null, true);
  }
}));

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

function authenticateToken(req, res, next) {
    // const authHeader = req.headers['authorization']
    // const token = authHeader && authHeader.split(' ')[1]
    // if (token == null) return res.sendStatus(403)

    // jwt.verify(token, process.env.TOKEN_SECRET, function(err, user) {
    //     console.log(err)

    //     if (err) return res.sendStatus(403)

    //     req.user = user

    //     next()
    // })
}

// app.use((req,res,next)=>{
//     authenticateToken(req,res,next)
// })

var session = require('express-session');
var MongoStore = require('connect-mongo');
app.use(session({
  secret: 'work hard',
  resave: true,
  saveUninitialized: false,
  store: MongoStore.create({mongoUrl: mongoDB})
}));
//Shranimo sejne spremenljivke v locals
//Tako lahko do njih dostopamo v vseh view-ih (glej layout.hbs)
app.use(function (req, res, next) {
  res.locals.session = req.session;
  next();
});

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/food', foodRouter);
app.use('/journey', journeyRouter);
app.use('/jwt', jwtRouter);
app.use('/location', locationRouter);
app.use('/restaurantRating', restaurantRatingRouter);
app.use('/restaurantMenu', restaurantMenuRouter);
app.use('/restaurants', restaurantsRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;
