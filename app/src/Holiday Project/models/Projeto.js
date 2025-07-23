const mongoose = require('mongoose');
const ProjetoSchema = new mongoose.Schema({
  _id: String,
  nome: String,
  dataCriacao: Date
});
module.exports = mongoose.model('Projeto', ProjetoSchema);
