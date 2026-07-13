const mysql = require('mysql2/promise');

const host = process.env.DB_HOST || 'localhost';
const port = process.env.DB_PORT || 3308;
const user = process.env.DB_USER || 'root';
const password = process.env.DB_PASSWORD || '';
const database = process.env.DB_NAME || 'bd_integracion';

let pool;

async function initDb() {
  // Connect without database to ensure it exists
  const conn = await mysql.createConnection({ host, port, user, password });
  await conn.query(`CREATE DATABASE IF NOT EXISTS \`${database}\``);
  await conn.end();

  pool = mysql.createPool({ host, port, user, password, database, waitForConnections: true, connectionLimit: 10 });

  // Create table if not exists
  const createTableSql = `
    CREATE TABLE IF NOT EXISTS avisos (
      id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
      tipo_aviso VARCHAR(50) NOT NULL,
      canal VARCHAR(30) NOT NULL,
      destinatario VARCHAR(150) NOT NULL,
      asunto VARCHAR(150) NOT NULL,
      mensaje TEXT NOT NULL,
      estado_aviso VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
      id_referencia BIGINT NULL,
      tipo_referencia VARCHAR(50) NULL,
      fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
      fecha_envio DATETIME NULL,
      activo BOOLEAN NOT NULL DEFAULT TRUE
    ) ENGINE=InnoDB;
  `;

  await pool.query(createTableSql);

  // Seed initial data if empty
  const [rows] = await pool.query('SELECT COUNT(*) AS cnt FROM avisos');
  if (rows && rows[0] && rows[0].cnt === 0) {
    await pool.query(
      `INSERT INTO avisos (tipo_aviso, canal, destinatario, asunto, mensaje, id_referencia, tipo_referencia, estado_aviso) VALUES (?, ?, ?, ?, ?, ?, ?, ?)`,
      ['PEDIDO_CREADO', 'SISTEMA', 'admin@smartlogix.cl', 'Pedido creado', 'Se ha creado un pedido de prueba en SmartLogix.', 1, 'PEDIDO', 'PENDIENTE']
    );
  }

  return pool;
}

function getPool() {
  if (!pool) throw new Error('DB pool not initialized. Call initDb first.');
  return pool;
}

module.exports = { initDb, getPool };
