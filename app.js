var express = require('express');
var app = express();

app.use(express.static('resources/public'));

app.listen(3000, function() {
  console.log('stats-tracker listening on 3000');
});
