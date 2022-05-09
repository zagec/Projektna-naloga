var express = require('express');
var router = express.Router();
var locationController = require('../controllers/locationController.js');

/*
 * GET
 */
router.get('/', locationController.list);

/*
 * GET
 */
router.get('/:id', locationController.show);

/*
 * POST
 */
router.post('/', locationController.create);

/*
 * PUT
 */
router.put('/:id', locationController.update);

/*
 * DELETE
 */
router.delete('/:id', locationController.remove);

module.exports = router;
