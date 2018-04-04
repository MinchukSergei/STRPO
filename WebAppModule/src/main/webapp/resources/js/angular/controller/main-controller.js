'use strict';

angular.module('mainModule')
    .controller('mainController',
        ['$scope', 'User',
            function ($scope, User) {
                $scope.CALENDAR_TAB = 'calendar';
                $scope.GALLERY_TAB = 'gallery';
                $scope.CHAT_TAB = 'chat';
                $scope.EVENTS_TAB = 'events';

                $scope.selectedTab = $scope.CALENDAR_TAB;

                $scope.setTab = function (tab) {
                    $scope.selectedTab = tab;
                };

                var setCurrentUser = function () {
                    User.getCurrentUser().then(function (result) {
                        $scope.currentUser = result;
                    });
                };

                setCurrentUser();
            }]);