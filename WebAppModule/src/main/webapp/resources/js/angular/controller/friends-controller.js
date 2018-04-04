'use strict';

angular.module('mainModule')
    .controller('friendsController',
        ['$scope', 'User', '$window',
            function ($scope, User, $window) {
                $scope.isSearch = false;
                $scope.userNotFound = false;
                $scope.selectedIndex = null;
                $scope.selectedUser = null;

                $scope.extraInfoIsVisible = false;

                $scope.showExtraInfo = function (currentUser) {
                    $scope.extraInfoIsVisible = !$scope.extraInfoIsVisible;
                };

                $scope.hideExtraInfo = function () {
                    $scope.extraInfoIsVisible = false;
                    clearSelection();
                };

                var clearSelection = function () {
                    $scope.selectedIndex = null;
                    $scope.$emit('onUserSelected', null);
                };

                $scope.$on('changeSelectedUser', function (event, newUser, callback) {
                    var contains = $scope.userList.some(function (user, id) {
                        if (user.userLogin == newUser.userLogin) {
                            $scope.clearSearchInput();
                            $scope.selectUser(user, id);
                            callback();
                            return true;
                        }
                        return false;
                    });
                    if (!contains) {
                        clearSelection();
                    }
                });

                $scope.selectUser = function (user, id) {
                    $scope.selectedIndex = id;
                    $scope.selectedUser = user;
                    $scope.$parent.$broadcast('onUserSelected', user);
                    $scope.$emit('onUserSelected', user);
                };

                $scope.clearSearchInput = function () {
                    if ($scope.searchFriend) {
                        $scope.searchFriend = '';
                        $scope.checkSearchInput();
                    }
                    clearSelection();
                };

                $scope.checkSearchInput = function () {
                    if (!$scope.searchFriend) {
                        $scope.userNotFound = false;
                        $scope.isSearch = false;
                        updateFriendList();
                    }
                    clearSelection();
                };

                var updateFriendList = function () {
                    User.getFriends().then(function (data) {
                        $scope.userList = data;
                    });
                };

                $scope.searchUser = function () {
                    if ($scope.searchFriend) {
                        $scope.isSearch = true;
                        User.searchUser($scope.searchFriend).then(function (data) {
                            if (data.length == 0) {
                                $scope.userNotFound = true;
                            }
                            $scope.userList = data;
                        });
                    }
                    clearSelection();
                };

                $scope.addFriend = function (login) {
                    if ($scope.isSearch) {
                        User.addFriend(login);
                        $window.location.reload();
                    }
                };

                $scope.removeFriend = function (login) {
                    if (!$scope.isSearch) {
                        User.removeFriend(login);
                        $window.location.reload();
                    }
                };

                updateFriendList();
            }]);