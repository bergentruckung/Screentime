var md5 = require('md5');

exports.getHash = function(email , deviceid) {
	console.log(md5(email +':' + deviceid));
	return md5(email +':' + deviceid);
};