'use strict';

angular
    .module('mainModule')
    .factory('UserImage',
        ['$http',
            function ($http) {
                return {
                    updateIcon: function (image) {
                        var formData = new FormData();
                        formData.append('image', image);
                        var req = {
                            method: 'POST',
                            url: applicationContext + '/icon/edit',
                            headers: {
                                'Content-Type': undefined
                            },
                            transformRequest: angular.identity,
                            data: formData
                        };

                        return $http(req)
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    getGalleryByLogin: function (login) {
                        var req = {
                            method: 'GET',
                            url: applicationContext + '/image/gallery/' + login
                        };

                        return $http(req)
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    addImageToGallery: function (image) {
                        var formData = new FormData();
                        formData.append('image', image);
                        var req = {
                            method: 'POST',
                            url: applicationContext + '/image',
                            headers: {
                                'Content-Type': undefined
                            },
                            transformRequest: angular.identity,
                            data: formData
                        };

                        return $http(req)
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    deleteImageFromGalleryByName: function (imageName) {
                        var req = {
                            method: 'DELETE',
                            url: applicationContext + '/image/' + imageName
                        };

                        return $http(req)
                            .then(function (result) {
                                return result.data;
                            });
                    }
                }
            }]);
