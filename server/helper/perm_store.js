var mysql = require('mysql');
var connection;

var LOGINTABLE = 'login';
var DEVICETABLE = 'device';

exports.initialize = function(db_host,db_user,db_pass,db_database) {
    connection = mysql.createConnection({
        host: db_host,
        user: db_user,
        password: db_pass,
        database: db_database
    });

    connection.connect(function(err) {
        if (err) {
            console.log('Error connecting to mysql Db');
            return;
        }
        console.log('Connection established for mysql db');
    });
};

exports.signupUser = function(username , email , password) {
    query = 'insert into '+ LOGINTABLE +' (name,email,password) values ("' + username + '", "' + email + '",  "'+  password +  '")';
    connection.query(query,
    function selectCb(err, results, fields) {
        if (err) throw err;
        else console.log('success');
    });
};

exports.addDevice = function(email , deviceID) {
    query = 'insert into '+ LOGINTABLE +' (name,email,password) values ("' + username + '", "' + email + '",  "'+  password +  '")';
    connection.query(query,
    function selectCb(err, results, fields) {
        if (err) throw err;
        else console.log('success');
    });
};

exports.addDevice = function(emailid , deviceid){
    query = 'insert into '+ DEVICETABLE +' (email,deviceid) values ("' + emailid  + '",  "'+  deviceid +  '")';
    connection.query(query,
    function selectCb(err, results, fields) {
        if (err) throw err;
        else console.log('success');
    });
};

exports.checkLogin = function(emailid , password , callback){
    query = 'select count(*) as solution from '+ LOGINTABLE + ' WHERE email = "' + emailid + '" AND PASSWORD = "' + password + '"';
    console.log(query);
    connection.query(query,
    function selectCb(err, results, fields) {
        if (err) throw err;
        else {
            console.log("present : " + results[0].solution);
            if(results[0].solution == '0'){
                callback('failed');
            }else{
                callback('success');
            } 
        }
    });
};

exports.checkDeviceRegistration = function(email , deviceid , callback){
    query = 'select hash as solution from '+ DEVICETABLE + ' WHERE email = "' + email + '" AND deviceid = "' + deviceid + '"';
    connection.query(query,
    function selectCb(err, results, fields) {
        if (err) throw err;
        else {
            if(results.length == 0){
                callback('failed');
            }else{
                console.log("presented hash : " + results[0].solution);
                callback(results[0].solution);
            }
        }
    });
};

exports.insertDevice = function(email , deviceid ,hash ,  callback){

    query = "insert into " + DEVICETABLE + " values( '" + email + "' , '"+ deviceid +"','" +hash + "')";
    console.log(query);
    connection.query(query,
    function selectCb(err, results, fields) {
        if (err) throw err;
        else {
            callback('success');
        }
    });
};



