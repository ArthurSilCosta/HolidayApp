const mongoose = require('mongoose');
const GISSchema = new mongoose.Schema({
  _id: String,
  idProjeto: String,
  coordenadas: String,
  observacoes: String,
  data: Date
});
module.exports = mongoose.model('GIS', GISSchema);
