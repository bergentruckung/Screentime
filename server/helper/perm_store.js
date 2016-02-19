var mysql = require('mysql');
var connection;

var LOGINTABLE = 'login';
var DEVICETABLE = 'device';
var SESSIONTABLE = 'session';

exports.initialize = function(db_host, db_user, db_pass, db_database) {
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

exports.signupUser = function(username, email, password) {
    query = 'insert into ' + LOGINTABLE + ' (name,email,password) values ("' + username + '", "' + email + '",  "' + password + '")';
    connection.query(query,
        function selectCb(err, results, fields) {
            if (err) throw err;
            else console.log('success');
        });
};

exports.addDevice = function(emailid, deviceid) {
    query = 'insert into ' + DEVICETABLE + ' (email,deviceid) values ("' + emailid + '",  "' + deviceid + '")';
    connection.query(query,
        function selectCb(err, results, fields) {
            if (err) throw err;
            else console.log('success');
        });
};

exports.checkLogin = function(emailid, password, callback) {
    query = 'select count(*) as solution from ' + LOGINTABLE + ' WHERE email = "' + emailid + '"';
    connection.query(query,
    function selectCb(err, results, fields) {
        if (err) throw err;
        else {
            if (results[0].solution == '0') {
                callback('not_registered');
            } else {
                query = 'select count(*) as solution from ' + LOGINTABLE + ' WHERE email = "' + emailid + '" AND PASSWORD = "' + password + '"';
                connection.query(query,
                    function selectCb(err, results, fields) {
                        if (err) throw err;
                        else {
                            if (results[0].solution == '0') {
                                callback('wrong_password');
                            } else {
                                callback('success');
                            }
                        }
                    });
            }
        }
    });
};


exports.checkDeviceRegistration = function(email, deviceid, callback) {
    query = 'select hash as solution from ' + DEVICETABLE + ' WHERE email = "' + email + '" AND deviceid = "' + deviceid + '"';
    connection.query(query,
        function selectCb(err, results, fields) {
            if (err) throw err;
            else {
                if (results.length == 0) {
                    callback('failed');
                } else {
                    callback(results[0].solution);
                }
            }
        });
};

exports.insertDevice = function(email, deviceid, hash, callback) {

    query = "insert into " + DEVICETABLE + " values( '" + email + "' , '" + deviceid + "','" + hash + "')";
    connection.query(query,
        function selectCb(err, results, fields) {
            if (err) {
                console.log(query);
                throw err;
            } else {
                callback('success');
            }
        });
};

exports.insertSessionData = function(hash, type, start, stop, callback) {
    query = "insert into " + SESSIONTABLE + " values ( '" + hash + "', '" + type + "',FROM_UNIXTIME('" + start + "'),FROM_UNIXTIME('" + stop + "'))";
    console.log(query);
    connection.query(query,
        function selectCb(err, results, fields) {
            if (err) {
                console.log(query);
                throw err;
            } else {
                callback('success');
            }
        });

};

exports.getDeviceData = function(hash, callback) {
    query = "select type , start as START, stop AS STOP from " + SESSIONTABLE + " where hash = '" + hash.trim() + "'";
    console.log(query);
    connection.query(query,
        function selectCb(err, results, fields) {

            if (err) {
                console.log(query);
                throw err;
            } else {
                callback({ 'data': results });
            }
        });
};

exports.getAllProcess = function(callback){
    query = "select * from session order by type asc";
    connection.query(query,
        function selectCb(err, results, fields) {
            if (err) {
                console.log(query);
                throw err;
            } else {
                callback(results);
            }
        });
};

exports.getDeviceDataFull = function(hash, deviceid, callback) {
    query = "select type , start as START, stop AS STOP from " + SESSIONTABLE + " where hash = '" + hash + "'";
    connection.query(query,
        function selectCb(err, results, fields) {
            if (err) {
                console.log(query);
                throw err;
            } else {
                callback({ "hash":hash , "deviceID" : deviceid,  "data": results });
            }
        });
};

exports.getUserData = function(email, callback) {
    query = "select deviceid ,hash from " + DEVICETABLE + ' where email="' + email + '"';
    connection.query(query,
        function selectCb(err, results, fields) {
            if (err) {
                console.log(query);
                throw err;
            } else {
                var dataItems = [];
                console.log("results: " + results.length);
                for (var i = 0; i < results.length; i++) {
                    exports.getDeviceDataFull(results[i].hash, results[i].deviceid , function(info) {
                        dataItems.push(info);
                        if(dataItems.length == results.length){
                            callback(dataItems);
                        }
                    });
                }
            }
        });
};
