'use strict';

angular.module('authModule')
    .controller('authController', ['$scope', '$timeout', function ($scope, $timeout) {
        var self = this;
        this.SIGN_IN = 'SIGN_IN';
        this.SIGN_UP = 'SIGN_UP';
        this.EXTR_BTN_MORE = 'More';
        this.EXTR_BTN_LESS = 'Less';
        this.alertTimeout = 2500;

        $scope.selected = this.SIGN_IN;
        $scope.isSignIn = true;

        $scope.registerErrors = false;
        $scope.registerSucess = false;

        $scope.setSignIn = function () {
            $scope.isSignIn = true;
            $scope.selected = self.SIGN_IN;
        };
        $scope.setSignUp = function () {
            $scope.isSignIn = false;
            $scope.selected = self.SIGN_UP;
        };

        $scope.isExtraBtn = false;
        $scope.extraBtn = self.EXTR_BTN_MORE;

        $scope.switchExtraBtn = function () {
            $scope.isExtraBtn = !$scope.isExtraBtn;
            $scope.extraBtn = $scope.isExtraBtn ? self.EXTR_BTN_LESS : self.EXTR_BTN_MORE;
        };

        $scope.printError = function(errMsg) {
            $scope.authError = true;
            $scope.authErrorMsg = errMsg;

            $timeout(function () {
                $scope.authError = false;
                $scope.authErrorMsg = '';
            }, self.alertTimeout);
        };

        $scope.printSuccess = function (successMsg) {
            $scope.authSuccess = true;
            $scope.authSuccessMsg = successMsg;

            $timeout(function () {
                $scope.authSuccess = false;
                $scope.authSuccessMsg = '';
                $scope.setSignIn();
            }, self.alertTimeout);
        };


    }]);