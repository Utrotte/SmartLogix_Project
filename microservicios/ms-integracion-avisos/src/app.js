const express = require('express');
const cors = require('cors');
const avisosRoutes = require('./routes/avisos.routes');

const app = express();

app.use(cors());
app.use(express.json());

app.use('/api/avisos', avisosRoutes);

app.get('/health', (req, res) => res.json('OK'));

module.exports = app;
