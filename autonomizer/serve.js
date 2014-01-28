var static = require('node-static');

//
// Create a node-static server instance to serve the './public' folder
//
var file = new static.Server('./www', { cache: 0 });
var port = 6969;
console.log("RUNNING ON PORT: " + port + ". Goto http://localhost:" + port + "/ in your web browser");
require('http').createServer(function (request, response) {
	request.addListener('end', function () {
		//
		// Serve files!
		//
		file.serve(request, response);
	    }).resume();
    }).listen(port);
var sys = require('sys')
    var exec = require('child_process').exec;
function puts(error, stdout, stderr) { sys.puts(stdout) }
exec("open http://localhost:" + port + "/", puts);