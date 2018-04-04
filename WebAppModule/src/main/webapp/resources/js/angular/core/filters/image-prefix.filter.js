'use strict';

angular.module('mainModule')
.filter('imagePrefix', function () {
    return function (imageName) {
        var IMAGE_PREFIX = applicationContext + '/image/';
        if (!imageName) {
            imageName = 'default'
        }
        return IMAGE_PREFIX + imageName;
    }
});