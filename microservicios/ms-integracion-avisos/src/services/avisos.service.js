const { getPool } = require('../config/db');

async function listar() {
  const pool = getPool();
  const [rows] = await pool.query('SELECT * FROM avisos ORDER BY fecha_creacion DESC');
  return rows;
}

async function obtener(id) {
  const pool = getPool();
  const [rows] = await pool.query('SELECT * FROM avisos WHERE id = ?', [id]);
  return rows[0];
}

async function porReferencia(tipoReferencia, idReferencia) {
  const pool = getPool();
  const [rows] = await pool.query('SELECT * FROM avisos WHERE tipo_referencia = ? AND id_referencia = ?', [tipoReferencia, idReferencia]);
  return rows;
}

async function porEstado(estado) {
  const pool = getPool();
  const [rows] = await pool.query('SELECT * FROM avisos WHERE estado_aviso = ?', [estado]);
  return rows;
}

async function crear(aviso) {
  const pool = getPool();
  const estado = aviso.estadoAviso || 'PENDIENTE';
  const fechaEnvio = null;
  const [result] = await pool.query(
    `INSERT INTO avisos (tipo_aviso, canal, destinatario, asunto, mensaje, estado_aviso, id_referencia, tipo_referencia, fecha_envio, activo)
     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
    [aviso.tipoAviso, aviso.canal, aviso.destinatario, aviso.asunto, aviso.mensaje, estado, aviso.idReferencia || null, aviso.tipoReferencia || null, fechaEnvio, true]
  );
  return obtener(result.insertId);
}

async function actualizarEstado(id, estado) {
  const pool = getPool();
  await pool.query('UPDATE avisos SET estado_aviso = ? WHERE id = ?', [estado, id]);
  return obtener(id);
}

async function marcarEnviado(id) {
  const pool = getPool();
  await pool.query('UPDATE avisos SET estado_aviso = ?, fecha_envio = ? WHERE id = ?', ['ENVIADO', new Date(), id]);
  return obtener(id);
}

async function marcarFallido(id) {
  const pool = getPool();
  await pool.query('UPDATE avisos SET estado_aviso = ? WHERE id = ?', ['FALLIDO', id]);
  return obtener(id);
}

module.exports = { listar, obtener, porReferencia, porEstado, crear, actualizarEstado, marcarEnviado, marcarFallido };
