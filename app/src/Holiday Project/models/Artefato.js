const mongoose = require('mongoose');
const ArtefatoSchema = new mongoose.Schema({
  _id: String,
  idProjeto: String,
  tipo: String,
  descricao: String,
  data: Date
});
module.exports = mongoose.model('Artefato', ArtefatoSchema);
