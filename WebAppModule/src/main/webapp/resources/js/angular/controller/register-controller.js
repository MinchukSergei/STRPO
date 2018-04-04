'use strict';

angular.module('authModule')
    .controller('registerController', [
        '$scope', 'User', function ($scope, User) {
            $scope.userData = {
                userLogin: '',
                userPassword: '',
                userFirstName: '',
                userLastName: '',
                userEmail: '',
                userPhone: '',
                userAddress: ''
            };

            $scope.register = function () {
                User.registerUser($scope.userData).then(
                    function (success) {
                        $scope.printSuccess('You have successfully registered.');
                    },
                    function (error) {
                        $scope.printError('User already exists.');
                    }
                )
            };
        }
    ]);
