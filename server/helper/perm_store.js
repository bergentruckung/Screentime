var mysql = require('mysql');
var connection;

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

exports.get_customer_phoneno = function(c_id) {
    if(c_id == 'sanid123'){
        return '918289903935';
    }else if(c_id == 'sreeraj123'){
        return '918086084339';        
    }
};

exports.verify_pin = function(c_id, pin) {
    console.log("cid: " + c_id);
    console.log("pin: " + pin);
    if(c_id == 'sanid123' && pin == '44433'){
       return true;
    }else if(c_id == 'sreeraj123' && pin == '44444'){
        return true;
    }else{
        return false;
    }
    
}
