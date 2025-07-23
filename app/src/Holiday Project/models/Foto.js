const mongoose = require('mongoose');
const FotoSchema = new mongoose.Schema({
  _id: String,
  idProjeto: String,
  titulo: String,
  legenda: String,
  data: Date
});
module.exports = mongoose.model('Foto', FotoSchema);
