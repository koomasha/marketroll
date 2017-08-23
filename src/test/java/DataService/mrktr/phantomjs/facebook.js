var system = require('system');
var args = system.args;

var username = args[1];
var passwd = args[2]; 
var fb_url = args[3]; 

var page = require('webpage').create();

page.onConsoleMessage = function(msg) {
    //console.log(msg);
};


page.open(fb_url, function(status) {
    if ( status === "success" ) {
        page.evaluate(function(usr,pwd) {
              document.querySelector("input[name='email']").value = usr;
              document.querySelector("input[name='pass']").value = pwd;
              document.querySelector("#login_form").submit();
              console.log("username: \"" + document.querySelector("input[name='email']").value +"\""); 
              console.log("Login submitted!");
        },username,passwd);
        window.setTimeout(function () {
          //page.render('colorwheel.png');
				  var url = page.url;
				//  console.log('URL: ' + url);
				  var token = page.evaluate(function() {
									
						console.log('cookie: ' +  document.cookie);			
						var nameEQ = "mrktr-token" + "=";
						var ca = document.cookie.split(';');
						for(var i=0;i < ca.length;i++) {
							var c = ca[i];
							while (c.charAt(0)==' ') c = c.substring(1,c.length);
							if (c.indexOf(nameEQ) == 0) 
								return c.substring(nameEQ.length,c.length);
						}
				  });
				  console.log(token);
				  phantom.exit();
		}, 5000);
   }
});