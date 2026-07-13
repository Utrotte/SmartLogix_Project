const service = require('../services/avisos.service');

async function listar(req, res) {
  try {
    const data = await service.listar();
    res.json(data);
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Error interno' });
  }
}

async function obtener(req, res) {
  try {
    const id = req.params.id;
    const data = await service.obtener(id);
    if (!data) return res.status(404).json({ message: 'Aviso no encontrado' });
    res.json(data);
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Error interno' });
  }
}

async function porReferencia(req, res) {
  try {
    const { tipoReferencia, idReferencia } = req.params;
    const data = await service.porReferencia(tipoReferencia, idReferencia);
    res.json(data);
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Error interno' });
  }
}

async function porEstado(req, res) {
  try {
    const estado = req.params.estadoAviso;
    const data = await service.porEstado(estado);
    res.json(data);
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Error interno' });
  }
}

async function crear(req, res) {
  try {
    const body = req.body;
    // Basic validation
    const required = ['tipoAviso', 'canal', 'destinatario', 'asunto', 'mensaje'];
    for (const f of required) {
      if (!body[f]) return res.status(400).json({ message: `Falta campo ${f}` });
    }

    const created = await service.crear(body);
    res.status(201).json(created);
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Error interno' });
  }
}

async function actualizarEstado(req, res) {
  try {
    const id = req.params.id;
    const { estadoAviso } = req.body;
    if (!estadoAviso) return res.status(400).json({ message: 'Falta estadoAviso' });
    const updated = await service.actualizarEstado(id, estadoAviso);
    res.json(updated);
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Error interno' });
  }
}

async function marcarEnviado(req, res) {
  try {
    const id = req.params.id;
    const updated = await service.marcarEnviado(id);
    res.json(updated);
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Error interno' });
  }
}

async function marcarFallido(req, res) {
  try {
    const id = req.params.id;
    const updated = await service.marcarFallido(id);
    res.json(updated);
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Error interno' });
  }
}

module.exports = { listar, obtener, porReferencia, porEstado, crear, actualizarEstado, marcarEnviado, marcarFallido };
