const express = require('express');
const router = express.Router();
const controller = require('../controllers/avisos.controller');

router.get('/', controller.listar);
router.get('/:id', controller.obtener);
router.get('/referencia/:tipoReferencia/:idReferencia', controller.porReferencia);
router.get('/estado/:estadoAviso', controller.porEstado);
router.post('/', controller.crear);
router.patch('/:id/estado', controller.actualizarEstado);
router.patch('/:id/marcar-enviado', controller.marcarEnviado);
router.patch('/:id/marcar-fallido', controller.marcarFallido);

module.exports = router;
