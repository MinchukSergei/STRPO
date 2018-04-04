<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<event-calendar></event-calendar>
<div class="event-list">
    <event-block ng-repeat="dayEvent in dayEvents" day-event="dayEvent" current-user="currentUser"></event-block>
    <div class="add-event" ng-click="addEvent()">
        <div class="icon-plus"></div>
    </div>
</div>

<div class="member-list-modal" ng-show="isMembersModalVisible">
    <div class="member-list-wrap">
        <div class="member-list-content">
            <div class="member-list-header">
                <div class="member-list-name" ng-show="isOwnEvent">Your Friends</div>
                <div class="member-list-name">Event Members</div>
            </div>
            <div class="member-lists">
                <div class="remaining-friends" ng-show="isOwnEvent">
                    <div class="scroll-list">
                        <div class="user-list">
                            <div class="user-info-block" ng-repeat="friend in remainingFriends">
                                <div class="user-icon-block">
                                    <img ng-src="{{friend.userIcon.imageName | imagePrefix}}">
                                </div>
                                <div class="user-data-block">
                                    {{friend.userFirstName + ' ' + friend.userLastName}}
                                </div>
                                <div class="more-info-icon" ng-click="addFriendToEvent(friend.userLogin)">
                                    <img src="${pageContext.request.contextPath}/resources/images/assets/add.png">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
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
                                <div class="more-info-icon" ng-show="isOwnEvent" ng-click="deleteFriendFromEvent(eventMember.userLogin)">
                                    <img src="${pageContext.request.contextPath}/resources/images/assets/remove.png">
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