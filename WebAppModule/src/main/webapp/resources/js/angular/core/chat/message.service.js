'use strict';

angular
    .module('mainModule')
    .factory('Message',
        ['$http', function ($http) {
            return {
                getMessagesFromUser: function (userLogin) {
                    return $http.get(applicationContext + '/message/' + userLogin)
                        .then(function (result) {
                            return result.data;
                        });
                }
            }
        }]);