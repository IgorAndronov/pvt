<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>IdeaLogic - Кредитные системы</title>
    <link href="./resources/css/bootstra  p.css" rel="stylesheet">
    <link href="./resources/css/font-awesome.css" rel="stylesheet">
    <link href="./resources/css/jquery-ui.css" rel="stylesheet">
    <link rel="stylesheet" href="./resources/css/jquery-ui-slider-pips.css">
    <link href="./resources/css/style.css" rel="stylesheet">
    <script src="./resources/js/template.js"></script>
</head>
<body>
<!-- header -->
<header>
    <div class="container hidden-xs">
        <div class="row">
            <!-- Logo -->
            <div class="col-lg-4 col-md-4 col-sm-4">
                <div class="well logo">
                    <a href="index.html">
                        CREDITS <span>ideaLogic</span>
                    </a>
                    <div>Система online кредитования</div>
                </div>
            </div>
            <!-- End Logo -->

            <!-- Search Form -->
            <div class="col-lg-5 col-md-5 col-sm-4">
                <div class="well">
                    <form action="">
                        <div class="input-group">
                            <input type="text" class="form-control input-search" placeholder="Я ищю...">
                                        <span class="input-group-btn">
                                            <button class="btn btn-default no-border-left" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                                        </span>
                        </div>
                    </form>
                </div>
            </div>
            <!-- End Search Form -->

            <!-- Registration -->
            <div class="col-lg-3 col-md-3 col-sm-4">
                <div class="well">
                    <div class="navbar-right btn-group">
                        <a href="#" class="btn btn-success" type="button"><i class="glyphicon glyphicon-user"></i> Регистрация</a>
                        <a href="#" class="btn btn-primary" type="submit"><i class="glyphicon glyphicon-log-in"></i> Вход</a>
                    </div>
                </div>
            </div>
            <!-- End Registration -->
        </div>
    </div>
</header>

<!-- navigation -->
<nav class="navbar navbar-inverse" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <!-- text logo on mobile view -->
            <a class="navbar-brand visible-xs" href="index.html">CREDITS <span>ideaLogic</span></a>
        </div>
        <div class="navbar-collapse navbar-ex1-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="#" class="hidden-xs active"><i class="glyphicon glyphicon-home" style="font-size: 17px;"></i> </a> </li>
                <li class="nav-dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">О компании <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Наша история</a></li>
                        <li><a href="#">Конфиденциальность</a></li>
                        <li><a href="#">Реквизиты компании</a></li>
                    </ul>
                </li>
                <li><a href="#">Как работает система</a></li>
                <li><a href="#">Контакты</a></li>
                <li>
                    <div class="btn-group visible-xs btn-group-justified">
                        <a href="#" class="btn btn-success" type="button"><i class="glyphicon glyphicon-user"></i> Регистрация</a>
                        <a href="login.html" class="btn btn-primary" type="submit"><i class="glyphicon glyphicon-log-in"></i> Вход</a>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container card-container">
    <div class="card card-container">
         <img id="profile-img" class="profile-img-card" src="//ssl.gstatic.com/accounts/ui/avatar_2x.png" />
        <p id="profile-name" class="profile-name-card"></p>

        <form class="form-signin" action="/credit-app-1.0/LoginController" id="formname" method="post">
            <span style="color: red">${errorMsg}</span>
            <span id="reauth-email" class="reauth-email"></span>
            <div class="input-group"><span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
                <input type="email" id="inputEmail" name="login" class="form-control" placeholder="Email" required autofocus >
            </div>
            <div class="input-group"><span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Пароль" required >
            </div>
            <div id="remember" class="checkbox">
                <label>
                    <input type="checkbox" value="remember-me"> Запомнить меня
                </label>
            </div>
            <button class="btn btn-primary btn-block btn-signin" type="submit">Войти</button>
        </form>

        <a href="#" class="forgot-password">
            Забыли пароль?
        </a>
    </div>
</div>


<!-- Footer -->
<footer id="footer">
    <div class="container">
        <div class="row">
            <div class="col-lg-4 col-md-4 col-sm-4">
                <div class="column">
                    <h4>Информация</h4>
                    <ul>
                        <li><a href="#">Наша история</a></li>
                        <li><a href="#">Кофиденциальность</a></li>
                        <li><a href="#">Terms and Conditions</a></li>
                        <li><a href="#">Shipping Methods</a></li>
                    </ul>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-4">
                <div class="column">
                    <h4>Поддержка пользователей</h4>
                    <ul>
                        <li><i class="fa fa-skype"></i><a href="#">idealogic.helpdesk.credits</a></li>
                        <li><i class="fa fa-phone"></i><a href="#">380 (50) 442-5019</a></li>
                        <li><i class="fa fa-envelope"></i><a href="#">helpdesk@idealogic.com.ua</a></li>
                    </ul>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-4 right">
                <div class="column">
                    <div class="contact">+380 (93) 791-7878</div>
                    <div class="company-info">
                        <ul>
                            <li>ООО «ИдеяЛоджик»</li>
                            <li>ЄГРПОУ 12312411</li>
                            <li>р/с 26501616712</li>
                            <li>Банк ПАО «ПУМБ»</li>
                            <li>МФО 351533</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="social-network">
                <a href="#"><i class="fa fa-google-plus"></i></a>
                <a href="#"><i class="fa fa-facebook"></i></a>
                <a href="#"><i class="fa fa-twitter"></i></a>
                <a href="#"><i class="fa fa-linkedin"></i></a>
                <a href="#"><i class="fa fa-youtube"></i></a>
                <a href="#"><i class="fa fa-vk"></i></a>
            </div>
            <div class="navbar-inverse text-center copyright">
                Все права защищены © 2015 ideaLogic
            </div>
        </div>
    </div>
</footer>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="./resources/js/bootstrap.min.js"></script>
<!-- include the jQuery and jQuery UI scripts -->
<script src="https://code.jquery.com/jquery-2.1.1.js"></script>
<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
<script src="./resources/js/jquery-ui-slider-pips.js"></script>

<script>
    $(".slider-money").slider({
        range: "min",
        min: 500,
        max: 2000,
        value: 1000,
        step: 10,
        slide: function( event, ui ) {
            $("#amount").val(ui.value);
        }
    }).slider("pips", {
        rest: "label",
        step: 30
    }).slider('float');

    $( "#amount" ).val( $( ".slider-money" ).slider( "value" ) );

    $(".slider-period").slider({
        range: "min",
        min: 5,
        max: 40,
        value: 10,
        step: 1,
        slide: function( event, ui ) {
            $("#period").val(ui.value);
        }
    })
            .slider("pips", {
                step: 5,
                rest: "label"
            }).slider('float');

    $( "#period" ).val( $( ".slider-period" ).slider( "value" ) );



</script>
</html>