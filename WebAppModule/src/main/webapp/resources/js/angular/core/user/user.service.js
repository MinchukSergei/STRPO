'use strict';

angular
    .module('user')
    .factory('User',
        ['$http', '$window',
            function ($http, $window) {
                return {
                    registerUser: function (userData) {
                        var req = {
                            method: 'POST',
                            url: applicationContext + '/user',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            data: userData
                        };

                        return $http(req)
                            .then(function (result) {
                                return result;
                            });
                    },

                    loginUser: function (userData) {
                        var req = {
                            method: 'POST',
                            url: applicationContext + '/j_spring_security_check',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            transformRequest: function (obj) {
                                var str = [];
                                for (var p in obj)
                                    if (obj.hasOwnProperty(p)) {
                                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                                    }
                                return str.join("&");
                            },
                            data: userData
                        };

                        return $http(req)
                            .then(function (result) {
                                return result;
                            });
                    },

                    getCurrentUser: function () {
                        return $http.get(applicationContext + '/user')
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    updateCurrentUser: function (userData) {
                        var req = {
                            method: 'PUT',
                            url: applicationContext + '/user',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            data: userData
                        };

                        return $http(req)
                            .then(function (result) {
                                return result;
                            });
                    },

                    logoutUser: function () {
                        return $http.get(applicationContext + '/logout')
                            .then(function (result) {
                                $window.location.reload();
                            });
                    },

                    getFriends: function () {
                        return $http.get(applicationContext + '/friend/all')
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    searchUser: function (login) {
                        var userList = [];
                        return $http.get(applicationContext + '/user/' + login)
                            .then(
                                function success(result) {
                                    userList.push(result.data);
                                    return userList;
                                }, function fail() {
                                    return userList;
                                });
                    },

                    addFriend: function (login) {
                        var req = {
                            method: 'POST',
                            url: applicationContext + '/friend/' + login
                        };

                        return $http(req)
                            .then(function (result) {
                                return result;
                            });
                    },

                    removeFriend: function (login) {
                        var req = {
                            method: 'DELETE',
                            url: applicationContext + '/friend/' + login
                        };

                        return $http(req)
                            .then(function (result) {
                                return result;
                            });
                    }
                }
            }]);
