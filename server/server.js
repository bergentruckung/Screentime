var express = require('express');
var app = express();
var yaml = require("js-yaml");
var fs = require("fs");
var bodyParser = require('body-parser');
var path = require('path');

var perm_store = require('./helper/perm_store.js');


app.set('views', './views');
app.use(express.static('./public'));
//config file data 
var config = yaml.load(fs.readFileSync("config.yml"));
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({
    extended: true
})); // for parsing application/x-www-form-urlencoded


perm_store.initialize(config.mysql_host, config.mysql_user, config.mysql_pass, config.mysql_db);

app.get('/', function(req, res) {
    res.sendFile(__dirname + '/views/home.html')
});


app.post('/api/android/sessiondata/', function(req , res){
    console.log('session from android');
});

var server_port = process.env.OPENSHIFT_NODEJS_PORT || 8080
var server_ip_address = process.env.OPENSHIFT_NODEJS_IP || '127.0.0.1'

app.listen(server_port, server_ip_address, function() {
    console.log("Listening on " + server_ip_address + "," + server_port)
});
