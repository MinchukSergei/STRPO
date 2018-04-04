'use strict';

angular.module('mainModule')
    .controller('eventsController',
        ['$scope', 'UserEvent', 'EventMember',
            function ($scope, UserEvent, EventMember) {
                $scope.actualEvents = false;
                $scope.isMembersModalVisible = false;
                $scope.eventList = [];
                setEventList();

                $scope.toggleActualEvents = function () {
                    $scope.actualEvents = !$scope.actualEvents;
                    setEventList();
                };

                function setEventList() {
                    if ($scope.actualEvents) {
                        UserEvent.getActualEvents().then(
                            function (eventsData) {
                                prepareEventsData(eventsData);
                            }
                        )
                    } else {
                        UserEvent.getPublicFriendEvents().then(
                            function (eventsData) {
                                prepareEventsData(eventsData);
                            }
                        )
                    }

                }

                function prepareEventsData(eventsData) {
                    eventsData.events.forEach(function (el, i) {
                        var timeStamp = el.eventTimestamp;
                        el.eventTimestamp = new Date();
                        el.eventTimestamp.setTime(timeStamp);
                        el.accepted = eventsData.accepted[i];
                        if (el.eventTimestamp.getDay() == 6 || el.eventTimestamp.getDay() == 0) {
                            el.dayOff = true;
                        }
                    });
                    $scope.eventList = eventsData.events;
                }

                $scope.showEventMembers = function(event) {
                    $scope.isMembersModalVisible = true;
                    EventMember.getEventMembers(event.id)
                        .then(function (result) {
                            $scope.eventMembers = result;
                        });
                };

                $scope.hideMembersModal = function () {
                    $scope.isMembersModalVisible = false;
                };
                
                $scope.generate = function () {
                    UserEvent.generate();
                };

                $scope.acceptEvent = function(eventData, isAccept) {
                    UserEvent.acceptEvent(eventData, isAccept)
                        .then(function (result) {
                            eventData.accepted = isAccept;
                        });
                }
            }]);
