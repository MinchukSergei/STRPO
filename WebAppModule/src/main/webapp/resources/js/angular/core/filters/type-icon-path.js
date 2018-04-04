'use strict';

angular.module('eventComponent')
    .filter('typeIconPath', function () {
        return function (type) {
            var iconTypePrefix = applicationContext + '/resources/images/event/types/';
            var iconTypeDimension = '.png';
            return iconTypePrefix + type + iconTypeDimension;
        }
    });