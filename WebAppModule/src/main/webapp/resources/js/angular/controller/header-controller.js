'use strict';


angular.module('mainModule')
    .controller('headerController',
        ['$scope', 'User', '$window', '$timeout', 'UserImage',
            function ($scope, User, $window, $timeout, UserImage) {
                $scope.menuIsVisible = false;
                $scope.profileIsVisible = false;
                $scope.isPreviewImage = false;
                $scope.previewFile = null;

                $scope.showMenu = function () {
                    $scope.menuIsVisible = true;
                };

                $scope.hideMenu = function () {
                    $scope.menuIsVisible = false;
                };

                $scope.logout = function () {
                    User.logoutUser();
                };

                $scope.showProfile = function () {
                    $scope.profileIsVisible = true;
                };

                $scope.hideProfile = function () {
                    $scope.profileIsVisible = false;
                    $scope.cancelPreview();
                };

                $scope.updateCurrentUser = function () {
                    updateUser();
                };

                $scope.previewImage = function (el) {
                    if (el.files && el.files[0]) {
                        $scope.previewFile = el;
                        var reader = new FileReader();

                        reader.onload = function (e) {
                            $timeout(function () {
                                $scope.isPreviewImage = true;
                                $scope.previewImageSrc = e.target.result;
                            });
                        };

                        reader.readAsDataURL(el.files[0]);
                    }
                };

                $scope.cancelPreview = function () {
                    $scope.isPreviewImage = false;
                    $scope.previewImageSrc = '';
                };

                var updateUserData = function () {
                    User.updateCurrentUser($scope.currentUser)
                        .then(function () {
                            $window.location.reload();
                        });
                };

                var updateUser = function () {
                    if ($scope.previewImageSrc) {
                        UserImage.updateIcon($scope.previewFile.files[0]);
                    }
                    updateUserData();
                }
            }]);
