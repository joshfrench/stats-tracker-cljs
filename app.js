var express = require('express');
var gzip = require('express-static-gzip');
var app = express();

var oneDay = 86400000;

app.use("/", gzip('resources/public', { ensureGzipedFiles: true, indexFromEmptyFile: true, maxAge: oneDay }));

app.listen(process.env.PORT || 3000, function() {
  console.log('stats-tracker listening on 3000');
});
