const express = require('express');
const router = express.Router();
const Projeto = require('../models/Projeto');

router.post('/', async (req, res) => {
  try {
    const projeto = new Projeto(req.body);
    await projeto.save();
    res.status(201).json(projeto);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

module.exports = router;
