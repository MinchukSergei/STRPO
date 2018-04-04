<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="events-settings">
    <div class="actual-events">
        <label class="actual-events-label" ng-click="toggleActualEvents()">
            <img ng-src="${pageContext.request.contextPath}/resources/images/assets/{{ actualEvents ? 'check.png' : 'uncheck.png'}}">
            Actual events
        </label>
    </div>
</div>
<div class="event-list">
    <div class="event-block" ng-repeat="event in eventList">
        <img class="big-style" ng-src="${pageContext.request.contextPath}/resources/images/assets/style-line.png">
        <div class="event-icons">
            <img ng-src="${pageContext.request.contextPath}/resources/images/assets/pencil-gray.png">
            <img ng-src="${pageContext.request.contextPath}/resources/images/assets/calendar-gray.png">
        </div>
        <div class="event-data">
            <div class="event-name">
                <div class="event-name-input">{{event.eventName}}</div>
            </div>
            <div class="event-datetime">
                <input class="timepicker" type="datetime-local" placeholder="yyyy-MM-ddTHH:mm:ss" ng-model="event.eventTimestamp" ng-readonly="true">
            </div>
            <div ng-show="event.dayOff">
                Day Off
            </div>
        </div>

        <div class="event-info">
            <div class="event-owner-img" title="{{event.userOwner.userFirstName + ' ' + event.userOwner.userLastName}}">
                <img ng-src="{{event.userOwner.userIcon.imageName | imagePrefix}}">
            </div>
            <div class="event-type" click-out="hideEventType()">
                <img class="selected-type" ng-src="{{event.eventType.typeName | typeIconPath}}" title="{{event.eventType.typeName}}">
            </div>
            <div class="event-members-icon" ng-click="showEventMembers(event)">
                <img ng-src="${pageContext.request.contextPath}/resources/images/assets/members.png">
            </div>
        </div>

        <div class="event-actions">
            <img class="event-action" title="I will go" ng-click="acceptEvent(event, true)"
                 ng-src="${pageContext.request.contextPath}/resources/images/assets/like-thumb{{event.accepted === null ? '' : (event.accepted ? '-active' : '')}}.png">
            <img class="event-action" title="Will not go" ng-click="acceptEvent(event, false)"
                 ng-src="${pageContext.request.contextPath}/resources/images/assets/dislike-thumb{{event.accepted === null ? '' : (event.accepted ? '' : '-active')}}.png">
        </div>
    </div>
</div>

<div class="member-list-modal" ng-show="isMembersModalVisible">
    <div class="member-list-wrap">
        <div class="member-list-content">
            <div class="member-list-header">
                <div class="member-list-name">Event Members</div>
            </div>
            <div class="member-lists">
                <div class="actual-members">
                    <div class="scroll-list">
                        <div class="user-list">
                            <div class="user-info-block" ng-repeat="eventMember in eventMembers">
                                <div class="user-icon-block">
                                    <img ng-src="{{eventMember.userIcon.imageName | imagePrefix}}">
                                </div>
                                <div class="user-data-block">
                                    {{eventMember.userFirstName + ' ' + eventMember.userLastName}}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="member-list-footer">
                <div class="member-list-button" ng-click="hideMembersModal()">Ok</div>
            </div>
        </div>
    </div>
</div>