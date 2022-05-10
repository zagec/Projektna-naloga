var express = require('express');
var router = express.Router();
var resturantMenuController = require('../controllers/resturantMenuController.js');

/*
 * GET
 */
router.get('/', resturantMenuController.list);

/*
 * GET
 */
router.get('/:id', resturantMenuController.show);

/*
 * POST
 */
router.post('/', resturantMenuController.create);

/*
 * PUT
 */
router.put('/:id', resturantMenuController.update);

/*
 * DELETE
 */
router.delete('/:id', resturantMenuController.remove);

module.exports = router;
