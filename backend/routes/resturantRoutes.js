var express = require('express');
var router = express.Router();
var resturantController = require('../controllers/resturantController.js');

/*
 * GET
 */
router.get('/', resturantController.list);

/*
 * GET
 */
router.get('/:id', resturantController.show);

/*
 * POST
 */
router.post('/', resturantController.create);

/*
 * PUT
 */
router.put('/:id', resturantController.update);

/*
 * DELETE
 */
router.delete('/:id', resturantController.remove);

module.exports = router;
