const express = require('express');
const router = express.Router();
const Artefato = require('../models/Artefato');

router.post('/', async (req, res) => {
  try {
    const artefato = new Artefato(req.body);
    await artefato.save();
    res.status(201).json(artefato);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

module.exports = router;
