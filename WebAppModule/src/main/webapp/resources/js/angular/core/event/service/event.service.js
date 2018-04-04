'use strict';

angular
    .module('userEvent')
    .factory('UserEvent',
        ['$http',
            function ($http) {
                return {
                    getEventCountBetweenDates: function (from, to) {
                        return $http.get(applicationContext + '/event/count/' + from + '/' + to)
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    getEventsBetweenDates: function (from, to) {
                        return $http.get(applicationContext + '/event/' + from + '/' + to)
                            .then(function (result) {
                                return result.data;
                            })
                    },

                    getOwnEvents: function () {
                        return $http.get(applicationContext + '/event')
                            .then(function (result) {
                                return result.data;
                            })
                    },

                    addEvent: function (eventData) {
                        var req = {
                            method: 'POST',
                            url: applicationContext + '/event',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            data: eventData
                        };

                        return $http(req)
                            .then(function (result) {
                                return result.data;
                            });
                    },

                    removeEvent: function (eventId) {
                        var req = {
                            method: 'DELETE',
                            url: applicationContext + '/event/' + eventId
                        };

                        return $http(req)
                            .then(function (result) {
                                return result;
                            });
                    },

                    updateEvent: function (eventData) {
                        var req = {
                            method: 'PUT',
                            url: applicationContext + '/event',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            data: eventData
                        };

                        return $http(req)
                            .then(function (result) {
                                return result.data;
                            });
                    },
                    
                    generate: function () {
                        return $http.post(applicationContext + '/event/generate')
                            .then(function (result) {
                                return result;
                            })
                    },

                    getPublicFriendEvents: function () {
                        return $http.get(applicationContext + '/learning/all')
                            .then(function (result) {
                                return result.data;
                            })
                    },

                    getActualEvents: function () {
                        return $http.get(applicationContext + '/learning/actual')
                            .then(function (result) {
                                return result.data;
                            })
                    },

                    acceptEvent: function (eventData, isAccept) {
                        var req = {
                            method: 'POST',
                            url: applicationContext + '/learning/' + isAccept,
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            data: eventData
                        };

                        return $http(req)
                            .then(function (result) {
                                return result;
                            });
                    }
                }
            }]);
