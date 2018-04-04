'use strict';

angular.module('calendarModule').component('eventCalendar', {
    templateUrl: applicationContext + '/resources/js/angular/core/calendar/calendar.template.html',
    controller: ['$scope', '$timeout', 'UserEvent', '$filter',
        function ($scope, $timeout, userEvent, $filter) {
            Date.prototype.addDays = function (days) {
                var d = new Date(this.valueOf());
                d.setDate(d.getDate() + days);
                return d;
            };
            Date.prototype.subDays = function (days) {
                var d = new Date(this.valueOf());
                d.setDate(d.getDate() - days);
                return d;
            };
            Date.prototype.nextMonth = function () {
                var d = new Date(this.valueOf());
                d.setMonth(d.getMonth() + 1);
                return d;
            };
            Date.prototype.prevMonth = function () {
                var d = new Date(this.valueOf());
                d.setMonth(d.getMonth() - 1);
                return d;
            };
            Date.prototype.monthDays = function () {
                var d = new Date(this.getFullYear(), this.getMonth() + 1, 0);
                return d.getDate();
            };

            var weekDays = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];
            var LAST_WEEK_DAY = 6;
            var WEEK_LENGTH = 7;
            var DATE_PATTERN = 'dd-MM-yyyy';

            var today = new Date();
            var currentDate = new Date(today.getTime());
            var daysInMonth = [];

            var setTitle = function () {
                $scope.monthTitle = currentDate.toLocaleString('en-us', {month: 'long'});
                $scope.yearTitle = currentDate.getFullYear();
            };

            var setDayEvents = function () {

                var fromDate = dateToString(daysInMonth[0], DATE_PATTERN);
                var toDate = dateToString(daysInMonth[daysInMonth.length - 1], DATE_PATTERN);

                userEvent.getEventCountBetweenDates(fromDate, toDate)
                    .then(function (result) {
                        setEvents(result);
                    });
            };

            var setEvents = function (eventCounts) {
                daysInMonth.forEach(function (day) {
                    var maxDisplayCount = 6;
                    eventCounts.forEach(function (countEntry) {
                        if (dateToString(day, DATE_PATTERN) == countEntry.date) {
                            day.own = Math.min(maxDisplayCount, countEntry.ownCount);
                            day.shared = Math.min(maxDisplayCount, countEntry.sharedCount);
                        }
                    })
                });
            };

            var dateToString = function (date, pattern) {
                return $filter('date')(date, pattern);
            };

            $scope.rangeNumber = function (num) {
                num = num || 0;
                return new Array(num);
            };


            var setDaysInMonth = function () {
                var currentCopy = new Date(currentDate.getTime());
                var firstDayMonth = new Date(currentCopy.getFullYear(), currentCopy.getMonth(), 1);
                var lastDayMonth = new Date(currentCopy.getFullYear(), currentCopy.getMonth(), currentCopy.monthDays());

                var firstDayOfPage = firstDayMonth.subDays(firstDayMonth.getDay());
                var lastDayOfPage = lastDayMonth.addDays(LAST_WEEK_DAY - lastDayMonth.getDay());

                daysInMonth = getDates(firstDayOfPage, lastDayOfPage);
            };

            var getDates = function (startDate, endDate) {
                var dates = [];
                var currentDate = startDate;
                while (currentDate <= endDate) {
                    dates.push(new Date(currentDate));
                    currentDate = currentDate.addDays(1);
                }
                return dates;
            };

            var setWeeks = function () {
                $scope.weeks = [];
                var i;
                for (i = 0; i < daysInMonth.length; i += WEEK_LENGTH) {
                    $scope.weeks.push(daysInMonth.slice(i, i + WEEK_LENGTH));
                }
            };

            var updateMonth = function () {
                setTitle();
                setDaysInMonth();
                setDayEvents();
                setWeeks();
            };

            var setHoursToZero = function (date) {
                date.setHours(0, 0, 0, 0);
            };

            $scope.nextMonth = function () {
                $scope.isNextMonth = true;
                changeMonth();
            };

            $scope.prevMonth = function () {
                $scope.isNextMonth = false;
                changeMonth();
            };

            var changeMonth = function () {
                setMonthCssClassAndUpdate();
                currentDate = $scope.isNextMonth ? currentDate.nextMonth() : currentDate.prevMonth();
            };

            var setMonthCssClassAndUpdate = function () {
                var fadeOutTimeout = 300;
                var inCss = 'in';
                var outCss = 'out';
                var monthCss = $scope.isNextMonth ? 'next' : 'prev';
                $scope.monthDynamicClass = outCss + ' ' + monthCss;
                $timeout(function () {
                    updateMonth();
                    $scope.monthDynamicClass = inCss + ' ' + monthCss;
                }, fadeOutTimeout);
            };

            var clearSelected = function () {
                $scope.selectedWeekIndex = null;
                $scope.selectedDayIndex = null;
            };

            var setSelectedToday = function () {
                var j;
                $scope.weeks.forEach(function (week, i, arr) {
                    for (j = 0; j < WEEK_LENGTH; j++) {
                        if ($scope.isToday(week[j])) {
                            $scope.setSelected(i, j, week[j]);
                            break;
                        }
                    }
                });
            };

            $scope.setSelected = function (weekIndex, dayIndex, day) {
                $scope.selectedWeekIndex = weekIndex;
                $scope.selectedDayIndex = dayIndex;
                $scope.$emit('onDaySelected', day);
            };

            $scope.$on('onEventChanged', function (event, data) {
                updateMonth();
            });

            $scope.isCurrentMonth = function (date) {
                return date.getMonth() === currentDate.getMonth();
            };

            $scope.isToday = function (date) {
                return date.valueOf() === today.valueOf();
            };

            $scope.getWeekDayName = function (day) {
                return weekDays[day];
            };

            $scope.monthDynamicClass = 'new';

            setHoursToZero(today);
            updateMonth();
            clearSelected();
            setSelectedToday();

        }]
});
