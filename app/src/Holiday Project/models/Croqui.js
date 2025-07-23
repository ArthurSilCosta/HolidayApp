const mongoose = require('mongoose');
const CroquiSchema = new mongoose.Schema({
  _id: String,
  idProjeto: String,
  titulo: String,
  anotacao: String,
  data: Date
});
module.exports = mongoose.model('Croqui', CroquiSchema);
