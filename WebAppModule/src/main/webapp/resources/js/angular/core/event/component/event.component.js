'use strict';

angular.module('eventComponent').component('eventBlock', {
    templateUrl: applicationContext + '/resources/js/angular/core/event/component/event.template.html',
    controller: ['$scope',
        function ($scope) {
            var self = this;

            $scope.isTypeSelected = false;
            $scope.isOwnEvent = false;
            $scope.currentUser = this.currentUser;
            $scope.public = false;
            $scope.applicationContext = applicationContext;

            $scope.eventData = {
                id: '',
                eventName: '',
                eventTimestamp: '',
                eventType: {},
                userOwner: {},
                public: false
            };

            var setEventData = function () {
                $scope.dayEvent = self.dayEvent;
                $scope.eventData.id = $scope.dayEvent.id;
                $scope.eventData.eventName = $scope.dayEvent.eventName;
                $scope.eventData.eventTimestamp = new Date();
                $scope.eventData.eventTimestamp.setTime($scope.dayEvent.eventTimestamp);
                $scope.eventData.eventType = $scope.dayEvent.eventType;
                $scope.eventData.userOwner = $scope.dayEvent.userOwner;
                $scope.eventData.public = $scope.dayEvent.public;

                $scope.isOwnEvent = $scope.eventData.userOwner.id == $scope.currentUser.id;

                $scope.eventDate = new Date($scope.eventData.eventTimestamp);
                $scope.public = $scope.dayEvent.public;
            };

            var setDefaultTime = function () {
                $scope.eventData.eventTimestamp = $scope.eventDate;
                $scope.eventData.eventTimestamp.setHours(0, 0, 0, 0);
            };

            $scope.checkTimeInput = function () {
                if (!$scope.eventData.eventTimestamp) {
                    setDefaultTime();
                }
            };

            $scope.removeEvent = function () {
                $scope.$emit('onEventRemove', $scope.eventData.id);
            };

            $scope.updateEvent = function () {
                $scope.$emit('onEventUpdate', $scope.eventData);
            };

            $scope.switchEventTypes = function () {
                $scope.isTypeSelected = !$scope.isTypeSelected;
            };

            $scope.switchEventAccessibility = function () {
                $scope.public = $scope.eventData.public = !$scope.public;
            };

            $scope.hideEventType = function () {
                $scope.isTypeSelected = false;
            };

            $scope.eventTypes = ['other', 'birthday', 'meeting', 'home', 'work', 'sport', 'kids'];

            $scope.setSelectedType = function (type) {
                $scope.eventData.eventType.typeName = type;
            };

            $scope.showEventMembers = function (eventData) {
                $scope.$emit('onEventMembersSelect', eventData);
            };

            setEventData();
        }],

    bindings: {
        dayEvent: '=',
        currentUser: '='
    }
});
