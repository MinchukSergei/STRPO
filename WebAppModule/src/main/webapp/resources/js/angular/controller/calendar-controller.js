'use strict';

angular.module('mainModule')
    .controller('calendarController',
        ['$scope', 'UserEvent', '$filter', 'EventMember',
            function ($scope, UserEvent, $filter, EventMember) {
                $scope.selectedEvent = null;
                $scope.isMembersModalVisible = false;
                $scope.isOwnEvent = false;

                var DATE_PATTERN = 'dd-MM-yyyy';

                var dateToString = function (date, pattern) {
                    return $filter('date')(date, pattern);
                };

                $scope.$on('onDaySelected', function (event, data) {
                    $scope.eventDate = data;
                    setDayEvents();
                });

                var setDayEvents = function () {
                    $scope.dayEvents = [];
                    var date = dateToString($scope.eventDate, DATE_PATTERN);
                    UserEvent.getEventsBetweenDates(date, date)
                        .then(function (result) {
                            $scope.dayEvents = result;
                        });
                };

                $scope.addEvent = function () {
                    var defaultEventData = {
                        eventName: 'Event name',
                        eventTimestamp: $scope.eventDate.getTime(),
                        public: false,
                        eventType: {
                            typeName: 'other'
                        }
                    };

                    UserEvent.addEvent(defaultEventData)
                        .then(function (result) {
                            $scope.$broadcast('onEventChanged');
                            setDayEvents();
                        });
                };

                $scope.$on('onEventUpdate', function (event, data) {
                    UserEvent.updateEvent(data).then(function () {
                        setDayEvents();
                    });
                });

                $scope.$on('onEventRemove', function (event, data) {
                    UserEvent.removeEvent(data).then(function () {
                        $scope.$broadcast('onEventChanged');
                        setDayEvents();
                    })
                });

                $scope.$on('onEventMembersSelect', function (event, data) {
                    $scope.selectedEvent = data;
                    $scope.isOwnEvent = data.userOwner.id == $scope.currentUser.id;
                    $scope.isMembersModalVisible = true;
                    if ($scope.isOwnEvent) {
                        getRemainingFriends();
                    }
                    getEventMembers();
                });

                $scope.hideMembersModal = function () {
                    $scope.isMembersModalVisible = false;
                };

                $scope.addFriendToEvent = function (userLogin) {
                    EventMember.addMemberToEvent($scope.selectedEvent.id, userLogin)
                        .then(function (result) {
                            getEventMembers();
                            getRemainingFriends();
                        })
                };

                $scope.deleteFriendFromEvent = function (userLogin) {
                    EventMember.deleteMemberFromEvent($scope.selectedEvent.id, userLogin)
                        .then(function (result) {
                            getEventMembers();
                            getRemainingFriends();
                        })
                };

                var getEventMembers = function () {
                    EventMember.getEventMembers($scope.selectedEvent.id)
                        .then(function (result) {
                            $scope.eventMembers = result;
                        });
                };

                var getRemainingFriends = function () {
                    EventMember.getRemainingFriends($scope.selectedEvent.id)
                        .then(function (result) {
                            $scope.remainingFriends = result;
                        })
                };
            }]);
