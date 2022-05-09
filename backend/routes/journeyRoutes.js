var express = require('express');
var router = express.Router();
var journeyController = require('../controllers/journeyController.js');

/*
 * GET
 */
router.get('/', journeyController.list);

/*
 * GET
 */
router.get('/:id', journeyController.show);

/*
 * POST
 */
router.post('/', journeyController.create);

/*
 * PUT
 */
router.put('/:id', journeyController.update);

/*
 * DELETE
 */
router.delete('/:id', journeyController.remove);

module.exports = router;
