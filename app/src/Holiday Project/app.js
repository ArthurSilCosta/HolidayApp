// app.js
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv').config();

const app = express();
app.use(cors());
app.use(express.json());
// Middleware para logar todas as requisições recebidas
app.use((req, res, next) => {
  console.log(`📥 Requisição: ${req.method} ${req.originalUrl}`);
  if (req.body && Object.keys(req.body).length > 0) {
    console.log('📦 Body recebido:', JSON.stringify(req.body, null, 2));
  }
  next();
});


mongoose.connect(process.env.MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true
}).then(() => console.log('MongoDB conectado'))
  .catch(err => console.error('Erro ao conectar no MongoDB:', err));

// Rotas
app.use('/api/projetos', require('./routes/projetos'));
app.use('/api/sitios', require('./routes/sitios'));
app.use('/api/artefatos', require('./routes/artefatos'));
app.use('/api/gis', require('./routes/gis'));
app.use('/api/anotacoes', require('./routes/anotacoes'));
app.use('/api/fotos', require('./routes/fotos'));
app.use('/api/croquis', require('./routes/croquis'));

const PORT = process.env.PORT || 3000;
app.listen(PORT,'0.0.0.0', () => console.log(`Servidor rodando na porta ${PORT}`));