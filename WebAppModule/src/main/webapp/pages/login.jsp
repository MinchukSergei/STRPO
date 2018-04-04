<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html ng-app="authModule">
<head>
    <title>Page Title</title>
    <%@include file="util/include.jsp"%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/registration.css">

    <script type="text/javascript" src="webjars/angularjs/1.5.9/angular.js"></script>
    <script type="text/javascript" src="webjars/angularjs/1.5.9/angular-resource.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/app.auth.module.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/reg-input/reg-input.module.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/reg-input/reg-input.component.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/user/user.module.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/user/user.service.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/string-util/string-util.service.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/auth-controller.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/register-controller.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/login-controller.js"></script>

</head>

<body>

<div class="auth-form" ng-controller="authController">

    <div class="auth-alert alert-error" ng-show="authError">
        <span>{{authErrorMsg}}</span>
    </div>


    <div class="tab-group">
        <div ng-class="{'tab': true, 'active' : !isSignIn}" ng-click="setSignUp()">
            <div class="tab-name">SIGN UP</div>
        </div>
        <div ng-class="{'tab': true, 'active' : isSignIn}" ng-click="setSignIn()">
            <div class="tab-name">SIGN IN</div>
        </div>
    </div>

    <div class="auth-result-modal" ng-show="authSuccess">
        <div class="auth-result-content">
            <div class="auth-alert alert-success">
                <span>{{authSuccessMsg}}</span>
            </div>
        </div>
    </div>

    <div ng-switch on="selected">
        <div ng-switch-when="SIGN_UP">
            <div class="tab-content">
                <div class="tab-main-name">Join us!</div>

                <form name="registerForm" ng-controller="registerController" ng-submit="register()" novalidate>

                    <div class="field-row">
                        <reg-input model="userData.userLogin" name="login" val="Login" type="text"
                                   req="true"></reg-input>
                        <reg-input model="userData.userPassword" name="password" val="Password" type="password"
                                   req="true"></reg-input>
                    </div>

                    <reg-input model="userData.userFirstName" name="firstname" val="First Name" type="text"
                               req="true"></reg-input>
                    <reg-input model="userData.userLastName" name="lastname" val="Last Name" type="text"
                               req="true"></reg-input>

                    <div class="button-wrapper">
                        <button type="button" class="button-little" ng-click="switchExtraBtn()">{{extraBtn}}</button>
                    </div>

                    <div ng-show="isExtraBtn">
                        <reg-input model="userData.userEmail" name="email" val="Email" type="email"
                                   req="false"></reg-input>
                        <reg-input model="userData.userPhone" name="phone" val="Phone" type="text"
                                   req="false"></reg-input>
                        <reg-input model="userData.userAddress" name="address" val="Address" type="text"
                                   req="false"></reg-input>
                    </div>

                    <div class="button-wrapper">
                        <button type="submit" class="button-big"
                                ng-disabled="registerForm.login.$invalid || registerForm.password.$invalid || registerForm.firstname.$invalid ||registerForm.lastname.$invalid">
                            GET STARTED
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <div ng-switch-when="SIGN_IN">
            <div class="tab-content">
                <div class="tab-main-name">Welcome back!</div>

                <form name="loginForm" ng-controller="loginController" ng-submit="signIn()" novalidate>
                    <div class="auth-alert alert-error" ng-show="loginErrors">
                        <span>{{loginError}}</span>
                    </div>

                    <reg-input model="userData.j_username" name="login" val="Login" type="text" req="true"></reg-input>
                    <reg-input model="userData.j_password" name="password" val="Password" type="password"
                               req="true"></reg-input>


                    <div class="button-wrapper">
                        <button class="button-big"
                                ng-disabled="loginForm.login.$invalid || loginForm.password.$invalid">
                            LOG IN
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>

</html>