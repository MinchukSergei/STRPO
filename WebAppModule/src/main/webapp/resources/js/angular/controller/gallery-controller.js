'use strict';

angular.module('mainModule')
    .controller('galleryController',
        ['$scope', 'UserImage',
            function ($scope, UserImage) {
                $scope.imageThumbIsVisible = false;
                $scope.currentThumbImage = '';
                $scope.selectedUser = $scope.currentUser;
                $scope.isOwnGallery = true;

                $scope.showThumb = function (imagePath) {
                    $scope.imageThumbIsVisible = true;
                    $scope.currentThumbImage = imagePath;
                };

                $scope.hideThumb = function () {
                    $scope.imageThumbIsVisible = false;
                    $scope.currentThumbImage = '';
                };

                $scope.$on('onUserSelected', function (event, data) {
                    if (data != $scope.selectedUser) {
                        $scope.isOwnGallery = data == $scope.currentUser;
                        $scope.selectedUser = data;
                        getGalleryByLogin($scope.selectedUser.userLogin);
                    }
                });

                $scope.addImageToGallery = function (el) {
                    if (el.files && el.files[0]) {
                        UserImage.addImageToGallery(el.files[0])
                            .then(function (result) {
                                getGalleryByLogin($scope.selectedUser.userLogin);
                            });
                    }
                };

                $scope.deleteImageByName = function (imageName) {
                    UserImage.deleteImageFromGalleryByName(imageName)
                        .then(function (result) {
                            getGalleryByLogin($scope.selectedUser.userLogin);
                        })
                };

                var getGalleryByLogin = function (login) {
                    UserImage.getGalleryByLogin(login).then(function (result) {
                        $scope.galleryImages = result;
                    });
                };

                getGalleryByLogin($scope.selectedUser.userLogin);
            }]);