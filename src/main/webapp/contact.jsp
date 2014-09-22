<%-- 
    Document   : home
    Created on : 18 Sep 2014, 6:05:38 PM
    Author     : tmatsela
--%>


<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Justified Nav Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/justified-nav.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="js/ie-emulation-modes-warning.js"></script>

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>

    <link rel="stylesheet" href="vender/intl-tel-input/css/intlTelInput.css">
    
     
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="container">

      <div class="masthead">
          <img alt="thumb" src="images/thumb.png" style="width: 200px; height: 100px;"/>
        <ul class="nav nav-justified">
          <li ><a href="#">Home</a></li>
          <li><a href="#">Create SQL</a></li>
          <li><a href="#">SQL Tips</a></li>
          <li><a href="#">Downloads</a></li>
          <li class="active"><a href="#">Contact</a></li>
          <li><a href="#">Profile</a></li>
        </ul>
      </div>

      <!-- Jumbotron -->
      <div class="jumbotron">
       
          <p><img alt="SQLhome" src="images/SQLhome.png"/></p>
          
      </div>
<hr>
      <!-- Example row of columns -->
      <div class="container" style=" width: 800px">
      <div id="contact_form" class="row">
        <div class="col-12 col-sm-12 col-lg-12">
          <h2>Tell Us What You Think...</h2>
          <p>We appreciate any feedback about your overall experience on our site or how to make it even better.  Please fill in the below form with any comments and we will get back to you.</p>
          <form role="form" id="feedbackForm">
            <div class="form-group has-feedback">
              <label class="control-label" for="name">Name</label>
              <input type="text" class="form-control input-sm" id="name" name="name" placeholder="Enter your name" />
              <span class="help-block" style="display: none;">Please enter your name.</span>
            </div>
            <!-- UNCOMMENT HERE IF YOU WANT TITLE, COMPANY AND WEBSITE FIELDS - you must also uncomment values in $fields_req in sendmail.php
            <div class="form-group">
              <label class="control-label" for="title">Title</label>
              <input type="text" class="form-control input-sm optional" id="title" name="title" />
            </div>
            <div class="form-group">
              <label class="control-label" for="company">Company</label>
              <input type="text" class="form-control input-sm optional" id="company" name="company" />
            </div>
            <div class="form-group">
              <label class="control-label" for="website">Website</label>
              <input type="url" class="form-control input-sm optional" id="website" name="website" />
            </div>
            -->
            <div class="form-group has-feedback">
              <label class="control-label" for="phone">Phone</label>
              <input type="tel" class="form-control input-sm optional" id="phone" name="phone" placeholder="Enter your phone (Optional)"/>
              <span class="help-block" style="display: none;">Please enter a valid phone number.</span>
            </div>
            <div class="form-group has-feedback">
              <label class="control-label" for="email">Email Address</label>
              <input type="email" class="form-control input-sm" id="email" name="email" placeholder="Enter your email" />
              <span class="help-block" style="display: none;">Please enter a valid e-mail address.</span>
            </div>
            <div class="form-group has-feedback">
              <label class="control-label" for="message">Message*</label>
              <textarea rows="5" cols="30" class="form-control input-sm" id="message" name="message" placeholder="Enter your message" ></textarea>
              <span class="help-block" style="display: none;">Please enter a message.</span>
            </div>
            <img id="captcha" src="library/vender/securimage/securimage_show.php" alt="CAPTCHA Image" />
            <a href="#" onclick="document.getElementById('captcha').src = 'library/vender/securimage/securimage_show.php?' + Math.random(); return false" class="btn btn-info btn-sm">Show a Different Image</a><br/>
            <div class="form-group has-feedback" style="margin-top: 10px;">
              <label class="control-label" for="captcha_code">Text Within Image</label>
              <input type="text" class="form-control input-sm" name="captcha_code" id="captcha_code" placeholder="For security, please enter the code displayed in the box." />
              <span class="help-block" style="display: none;">Please enter the code displayed within the image.</span>
            </div>
            <span class="help-block" style="display: none;">Please enter a the security code.</span>
            <button type="submit" id="feedbackSubmit" class="btn btn-primary btn-lg" data-loading-text="Sending..." style="display: block; margin-top: 10px;">Send Feedback</button>
          </form>
        </div><!--/span-->
      </div><!--/row-->
      
    </div><!--/.container-->
    <!-- <======= UP TO HERE -->
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <script src="assets/vender/intl-tel-input/js/intlTelInput.min.js"></script>
    <script src="assets/js/contact-form.js"></script>

      <!-- Site footer -->
      <div class="footer">
        <p>&copy; Argility 2014</p>
      </div>

    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
  </body>
</html>
