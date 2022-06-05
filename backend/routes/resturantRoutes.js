var express = require('express');
var router = express.Router();
var resturantController = require('../controllers/resturantController.js');

/*
 * GET
 */
router.get('/byAbeceda/down', resturantController.listByAbecedaDown);
router.get('/byAbeceda/up', resturantController.listByAbecedaUp);
// router.get('/', resturantController.list);
router.get('/byPrice/down', resturantController.listByPriceDown);
router.get('/byPrice/up', resturantController.listByPriceUp);
router.get('/removeAll', resturantController.removeAll);
router.get('/nearMe/:latitude/:longitude/:radius', resturantController.showResturantsInRadius);

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
