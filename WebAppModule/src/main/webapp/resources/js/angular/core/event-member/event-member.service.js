'use strict';
angular
    .module('mainModule')
    .factory('EventMember',
        ['$http',
            function ($http) {
                return {
                    getEventMembers: function (eventId) {
                        return $http.get(applicationContext + '/member/' + eventId)
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    getRemainingFriends: function (eventId) {
                        return $http.get(applicationContext + '/member/not/' + eventId)
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    addMemberToEvent: function (eventId, friendLogin) {
                        var req = {
                            method: 'POST',
                            url: applicationContext + '/member/' + eventId + '/' + friendLogin
                        };

                        return $http(req)
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    deleteMemberFromEvent: function (eventId, friendLogin) {
                        var req = {
                            method: 'DELETE',
                            url: applicationContext + '/member/' + eventId + '/' + friendLogin
                        };

                        return $http(req)
                            .then(function (result) {
                                return result;
                            });
                    }
                }
            }]);
