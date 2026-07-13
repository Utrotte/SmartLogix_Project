require('dotenv').config();
const app = require('./app');
const { initDb } = require('./config/db');

const PORT = process.env.PORT || 8094;

(async () => {
  try {
    await initDb();
    app.listen(PORT, () => console.log(`ms-integracion-avisos listening on port ${PORT}`));
  } catch (err) {
    console.error('Failed to start server', err);
    process.exit(1);
  }
})();
