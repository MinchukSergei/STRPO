'use strict';

angular.module('authModule')
    .service('StringUtil', function () {
        this.isBlank = function (str) {
            return (!str || /^\s*$/.test(str));
        }

    });
