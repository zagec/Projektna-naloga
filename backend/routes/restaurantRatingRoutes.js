var express = require('express');
var router = express.Router();
var restaurantRatingController = require('../controllers/restaurantRatingController.js');

/*
 * GET
 */
router.get('/', restaurantRatingController.list);
router.get('/fromRestaurant/:id', restaurantRatingController.getRestaurantsRatings);

/*
 * GET
 */
router.get('/:id', restaurantRatingController.show);

/*
 * POST
 */
router.post('/', restaurantRatingController.create);

/*
 * PUT
 */
router.put('/:id', restaurantRatingController.update);

/*
 * DELETE
 */
router.delete('/:id', restaurantRatingController.remove);

module.exports = router;
