const mongoose = require('mongoose');
const SitioSchema = new mongoose.Schema({
  _id: String,
  idProjeto: String,
  nome: String,
  localizacao: String,
  descricao: String,
  data: Date
});
module.exports = mongoose.model('Sitio', SitioSchema);
