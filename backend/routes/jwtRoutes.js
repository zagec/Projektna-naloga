var express = require('express');
var router = express.Router();
var jwtController = require('../controllers/jwtController.js');

/*
 * GET
 */
router.get('/', jwtController.list);

/*
 * GET
 */
router.get('/:id', jwtController.show);

/*
 * POST
 */
router.post('/', jwtController.create);

/*
 * PUT
 */
router.put('/:id', jwtController.update);

/*
 * DELETE
 */
router.delete('/:id', jwtController.remove);

module.exports = router;
