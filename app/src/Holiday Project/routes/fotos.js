const express = require('express');
const router = express.Router();
const Foto = require('../models/Foto');

router.post('/', async (req, res) => {
  try {
    const foto = new Foto(req.body);
    await foto.save();
    res.status(201).json(foto);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

module.exports = router;
