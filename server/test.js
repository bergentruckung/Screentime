var express = require('express');
var testnexmo = require('./helper/nexmo_test_helper');
var app = express();
var yaml = require("js-yaml");
var fs = require("fs");
var bodyParser = require('body-parser');

app.set('views', './views');
app.set('view engine', 'jade');
//config file data 
var config = yaml.load(fs.readFileSync("config.yml"));
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded


app.get('/', function(req, res) {
    res.render('home', {
        title: 'Welcome'
    });
});

app.get('/callback', function(req, res) {
    console.log('call_id:' + req.query.call_id);
    console.log('to:' + req.query.to);
    console.log('status:' + req.query.status);
    console.log('enterd digits:' + req.query.digits);
    console.log('call price:' + req.query.call_price);
    console.log('call rate:' + req.query.call_rate);
});

console.log('Going Listen to 3000');
app.listen(3000)
console.log('listening...')

testnexmo.testfunction(config);
