const mongoose = require('mongoose');
const AnotacaoSchema = new mongoose.Schema({
  _id: String,
  idProjeto: String,
  titulo: String,
  texto: String,
  data: Date
});
module.exports = mongoose.model('Anotacao', AnotacaoSchema);
