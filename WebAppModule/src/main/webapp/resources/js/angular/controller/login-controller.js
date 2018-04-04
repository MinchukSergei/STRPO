'use strict';

angular.module('authModule')
    .controller('loginController', [
        '$scope', 'User', '$window', function ($scope, User, $window) {
            $scope.userData = {
                j_username: '',
                j_password: ''
            };

            $scope.signIn = function () {
                User.loginUser($scope.userData).then(
                    function (success) {
                        $window.location.href = applicationContext + '/main';
                    },
                    function (error) {
                        $scope.printError('Invalid credentials.')
                    }
                )
            };
        }
    ]);
