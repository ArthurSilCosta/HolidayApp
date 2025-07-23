const express = require('express');
const router = express.Router();
const GIS = require('../models/GIS');

router.post('/', async (req, res) => {
  try {
    const gis = new GIS(req.body);
    await gis.save();
    res.status(201).json(gis);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

module.exports = router;
