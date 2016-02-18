var express = require('express');
var app = express();
var yaml = require("js-yaml");
var fs = require("fs");
var bodyParser = require('body-parser');
var path = require('path');

var perm_store = require('./helper/perm_store.js');
var hash_code = require('./helper/hash.js');

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

app.post('/api/android/signup/', function(req, res) {
    var user_id = req.body.user_id;
    var user_name = req.body.user_name;
    var pass = req.body.pass;

    perm_store.checkLogin(user_id, pass, function(info) {
        if (info == 'failed') {
            perm_store.signupUser(user_name, user_id, pass);
            res.send('done');
        } else {
            res.send('User with same mail id already exist');
        }
    });
});

app.post('/api/login', function(req, res) {
    var user_id = req.body.user_id;
    var dev_id = req.body.device_id;
    var pass = req.body.pass;
    perm_store.checkLogin(user_id, pass, function(info) {
        if (info == "success") {
            perm_store.checkDeviceRegistration(user_id, dev_id, function(info) {
                if (info == 'failed') {
                    var hashValue = hash_code.getHash(user_id + ":" + dev_id);
                    perm_store.insertDevice(user_id, dev_id, hashValue, function(info) {
                        res.send(hashValue);
                    });
                } else {
                    res.send(info);
                }
            });
        } else {
            res.send(info);
        }
    });
});

app.post('/api/device/session/', function(req, res) {
	// console.log(req.body);
    var hash = req.body.hash;
    var data = req.body.session_data;

    for (var i = 0; i < data.length; i++) {
        var type = data[i].type;
        var start = data[i].start;
        var stop = data[i].stop;

        perm_store.insertSessionData(hash, type, start, stop, function(info) {});
    };
    res.send('updated');
});

app.post('/api/windows/session/', function(req ,res){
    var hash = req.body.hash;
    var data = req.body.session_data;

    for (var i = 0; i < data.length; i++) {
        var type = data[i].type;
        var start = data[i].start;
        var stop = data[i].stop;

        //perm_store.insertSessionData(hash, type, start, stop, function(info) {});
    };
    res.send('updated');
});

app.post('/api/getDeviceDetails/', function(req, res) {
    perm_store.getDeviceData(req.body.hash, function(info) {
        res.send(info);
    });
});

app.post('/api/getUserDetails/', function(req, res) {
    perm_store.getUserData(req.body.email, function(info) {
    	console.log("data in server");
    	console.log(info);
        res.send(info);
    });
});

var server_port = process.env.OPENSHIFT_NODEJS_PORT || 8080
var server_ip_address = process.env.OPENSHIFT_NODEJS_IP || '192.168.43.135'

app.listen(server_port, server_ip_address, function() {
    console.log("Listening on " + server_ip_address + "," + server_port)
});
