const express = require('express');
const router = express.Router();
const Croqui = require('../models/Croqui');

router.post('/', async (req, res) => {
  try {
    const croqui = new Croqui(req.body);
    await croqui.save();
    res.status(201).json(croqui);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

module.exports = router;
