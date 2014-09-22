<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Login</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    
    <link href="css/signin.css" rel="stylesheet">
    <link href="css/cover.css" rel="stylesheet"> 
    
     

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="../../assets/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
<div class="modal hide" id="pleaseWaitDialog" data-backdrop="static" data-keyboard="false">
    <div class="modal-content">
       
    </div>
        
    </div>
    <div class="site-wrapper">

      <div class="site-wrapper-inner">

        <div class="cover-container">

          <div class="masthead clearfix">
            <div class="inner">
              
              <ul class="nav masthead-nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="mailto:dba-support@argility.com?Subject=SQL Master%20Incident">Contact</a></li>
              </ul>
            </div>
          </div>

          <div class="inner cover">
            
              <p> <img alt="cover" src="images/thumb.png" style="width: 150px; height: 75px;"/> </p>
            <p class="lead">
              

            <form class="form-signin" role="form" name="loginform" action="Login" method="post">
        <h2 class="form-signin-heading">Please sign in</h2>
        <div class="col-md-12 col-md-offset-0">
            
           
            <input    name="username" type="username" class="form-control" placeholder="Username" required autofocus>
       
            </div>
        
        <div class="col-md-12 col-md-offset-0">
             
            
            <input  name="password" type="password" class="form-control" placeholder="Password" required>
       
            </div>
        
        
                <div class="row">
                    <div class="col-xs-6 text-left">
                    <div class="span2">
                        <input type="submit" value="Log in" class="btn btn-success btn-small" style="width: 130px" />
                    </div>
                        </div>
                        <div class="col-xs-6 text-right">
                    <div class="span2">
                        <a class="btn btn-primary btn-small" href="https://argilityrop.jira.com/login/forgot" style="margin-left: -26px" onclick="myApp.showPleaseWait();">Forgot Password</a>
                    </div>
                            </div>
                </div>

      </form>

    
            
          </div>

          <div class="mastfoot">
            <div class="inner">
              <p><a href="http://www.argility.com">SQL Master 2014</a>,Compiled and supported by <a href="mailto:dba-support@argility.com?Subject=SQL Master%20Incident" target="_top">
DBA Support</a>.</p>
            </div>
          </div>

        </div>

      </div>

    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="../../assets/js/docs.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>
    <script>
        
        var myApp;
myApp = myApp || (function () {
    var pleaseWaitDiv = $('<div class="modal hide" id="pleaseWaitDialog" data-backdrop="static" data-keyboard="false"><div class="modal-header"><h1>Processing...</h1></div><div class="modal-body"><div class="progress progress-striped active"><div class="bar" style="width: 100%;"></div></div></div></div>');
    return {
        showPleaseWait: function() {
            pleaseWaitDiv.modal();
        },
        hidePleaseWait: function () {
            pleaseWaitDiv.modal('hide');
        },

    };
})();
    </script>
        
        
        
  </body>
</html>