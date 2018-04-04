'use strict';

angular.module('regInput').component('regInput', {
    templateUrl: applicationContext + '/resources/js/angular/core/reg-input/reg-input.template.html',
    controller: ['$scope', 'StringUtil',
        function ($scope, StringUtil) {
            var self = this;
            this.isActive = false;

            this.setActive = function () {
                self.isActive = self.model;
            };

            this.checkValid = function () {
                if (self.req && StringUtil.isBlank(self.model)) {
                    self.isEmpty = true;
                } else if (self.req && !StringUtil.isBlank(self.model)) {
                    self.isEmpty = false;
                }
            }
        }],

    bindings: {
        req: '=',
        model: '=',
        val: '@',
        type: '@',
        name: '@'
    }
});