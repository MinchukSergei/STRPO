'use strict';

angular.module('mainModule')
    .directive("clickOut", ['$document', function ($document) {
        return {
            link: function ($scope, $element, $attributes) {
                var scopeExpression = $attributes.clickOut;
                var parent = $element[0];

                var onDocumentClick = function (event) {
                    var el = event.target;
                    var isChild = false;
                    while (el = el.parentElement) {
                        if (el == parent) {
                            isChild = true;
                            break;
                        }
                    }

                    if (!isChild) {
                        $scope.$apply(scopeExpression);
                    }
                };

                $document.on("click", onDocumentClick);

                $element.on('$destroy', function () {
                    $document.off("click", onDocumentClick);
                });
            }
        }
    }]);
