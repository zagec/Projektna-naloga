var express = require('express');
var router = express.Router();
var foodController = require('../controllers/foodController.js');

/*
 * GET
 */
router.get('/', foodController.list);

/*
 * GET
 */
router.get('/:id', foodController.show);

/*
 * POST
 */
router.post('/', foodController.create);

/*
 * PUT
 */
router.put('/:id', foodController.update);

/*
 * DELETE
 */
router.delete('/:id', foodController.remove);

module.exports = router;
