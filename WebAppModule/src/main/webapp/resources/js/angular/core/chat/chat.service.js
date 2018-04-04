'use strict';

angular
    .module('mainModule')
    .factory('ChatSocket',
        [function () {
            var stompClient;

            return {
                init: function (url) {
                    stompClient = Stomp.over(new SockJS(url));
                    stompClient.debug = null;
                },

                send: function (destination, headers, object) {
                    stompClient.send(destination, headers, object);
                },

                connect: function (successCallback, errorCallback) {
                    stompClient.connect({}, function (frame) {
                        successCallback(frame);
                    }, function (error) {
                        errorCallback(error);
                    });
                },

                subscribe: function (destination, callback) {
                    stompClient.subscribe(destination, function (message) {
                        callback(message);
                    });
                }
            };
        }]);
