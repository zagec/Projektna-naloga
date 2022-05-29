var express = require('express');
var router = express.Router();
var userController = require('../controllers/userController.js');

/*
 * GET
 */
router.get('/', userController.list);
router.get('/login', userController.showLogin);
router.get('/register', userController.showRegister);
router.get('/confirm/:code', userController.verifyMail);

/*
 * GET
 */
router.get('/:id', userController.show);

/*
 * POST
 */
router.post('/', userController.register);
router.post('/login', userController.login)

/*
 * PUT
 */
router.put('/:id', userController.update);

/*
 * DELETE
 */
router.delete('/:id', userController.remove);

module.exports = router;
