const express = require('express');
const router = express.Router();
const Sitio = require('../models/Sitio');

router.post('/', async (req, res) => {
  try {
    const sitio = new Sitio(req.body);
    await sitio.save();
    res.status(201).json(sitio);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

module.exports = router;
