<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="mainModule">
<head>
    <title>Hello</title>
    <%@include file="util/include.jsp"%>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/roboto-font.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user-list.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/calendar.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/events.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/gallery.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/chat.css">

    <script type="text/javascript" src="webjars/angularjs/1.5.9/angular.js"></script>
    <script type="text/javascript" src="webjars/angularjs/1.5.9/angular-resource.js"></script>
    <script type="text/javascript" src="webjars/angularjs-scroll-glue/2.0.2/scrollglue.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/app.main.module.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/main-controller.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/header-controller.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/friends-controller.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/calendar-controller.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/gallery-controller.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/chat-controller.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/controller/events-controller.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/calendar/calendar.module.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/calendar/calendar.component.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/user/user.module.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/user/user.service.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/image/image.service.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/event/service/event.module.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/event/service/event.service.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/event-member/event-member.service.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/chat/chat.service.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/chat/message.service.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/event/component/event.module.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/event/component/event.component.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/click-out/click-out.directive.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/filters/image-prefix.filter.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/angular/core/filters/type-icon-path.js"></script>
</head>
<body>

<div class="main-container" ng-controller="mainController">
    <div class="header" ng-controller="headerController">
        <div class="brand">
            <img class="brand-logo" src="${pageContext.request.contextPath}/resources/images/calendar.png">
            <p class="brand-name">Events</p>
        </div>

        <div class="user-info-btn" ng-click="showMenu()" click-out="hideMenu()">
            <div class="user-info-header">
                <p class="user-name">{{::currentUser.userFirstName}}{{' '}}{{::currentUser.userLastName}}</p>
                <div class="user-icon">
                    <img ng-src="{{currentUser.userIcon.imageName | imagePrefix}}">
                </div>
            </div>
            <div class="user-info-menu" ng-show="menuIsVisible">
                <div class="info-item" ng-click="showProfile()">Profile</div>
                <div class="info-divider"></div>
                <div class="info-item" ng-click="logout()">Logout</div>
            </div>
        </div>

        <div class="user-profile-modal" ng-show="profileIsVisible">
            <form class="user-profile" name="profileForm">
                <div class="profile-data">
                    <div class="profile-image">
                        <img ng-src="{{currentUser.userIcon.imageName | imagePrefix}}" ng-hide="isPreviewImage">
                        <div class="preview-img" ng-show="isPreviewImage">
                            <img ng-src="{{previewImageSrc}}">
                        </div>
                        <div class="fileupload">
                            <input type='file' title="Choose image"
                                   onchange="angular.element(this).scope().previewImage(this)"/>
                        </div>
                        <span class="preview-img-cancel" title="Cancel" ng-show="isPreviewImage"
                              ng-click="cancelPreview()">cancel</span>
                    </div>
                    <div class="profile-fields">
                        <div class="profile-name">
                            <div class="profile-name-wrap">
                                <div class="profile-field-name">Firstname</div>
                                <input class="user-firstname" name="firstName" required
                                       ng-model="currentUser.userFirstName">
                            </div>

                            <div class="profile-name-wrap">
                                <div class="profile-field-name">Lastname</div>
                                <input class="user-lastname" name="lastName" required
                                       ng-model="currentUser.userLastName">
                            </div>
                        </div>

                        <div class="profile-field">
                            <div class="profile-field-name">Login:</div>
                            <div class="profile-field-div">{{currentUser.userLogin}}</div>
                        </div>
                        <div class="profile-field">
                            <div class="profile-field-name">Password:</div>
                            <input class="profile-field-input" type="password" ng-model="currentUser.userPassword"
                                   placeholder="leave blank if won't be changed">
                        </div>
                        <div class="profile-field">
                            <div class="profile-field-name">Email:</div>
                            <input class="profile-field-input" type="email" ng-model="currentUser.userEmail">
                        </div>
                        <div class="profile-field">
                            <div class="profile-field-name">Phone:</div>
                            <input class="profile-field-input" ng-model="currentUser.userPhone">
                        </div>
                        <div class="profile-field">
                            <div class="profile-field-name">Address:</div>
                            <input class="profile-field-input" ng-model="currentUser.userAddress">
                        </div>
                    </div>
                </div>
                <div class="profile-buttons">
                    <div class="profile-btn" ng-show="profileForm.firstName.$valid && profileForm.lastName.$valid"
                         ng-click="updateCurrentUser()">Update
                    </div>
                    <div class="profile-btn" ng-click="hideProfile()">Cancel</div>
                </div>
            </form>
        </div>
    </div>

    <div class="main-wrap" ng-controller="chatController">
        <div class="left-block" ng-controller="friendsController">
            <div class="search-box">
                <img class="style-icon" src="${pageContext.request.contextPath}/resources/images/assets/search.png" ng-click="searchUser()">
                <input class="search-input" placeholder="Search" ng-model="searchFriend"
                       ng-keyup="$event.keyCode == 13 && searchUser()" ng-change="checkSearchInput()">
                <img class="clear-input" src="${pageContext.request.contextPath}/resources/images/assets/cancel.png" ng-click="clearSearchInput()">
            </div>

            <div class="extra-info-background" ng-show="extraInfoIsVisible" ng-click="hideExtraInfo()"></div>

            <div class="scroll-list">
                <div class="user-list">
                    <div class="user-not-found" ng-show="userNotFound">
                        Not found
                    </div>
                    <div class="user-info-block" ng-repeat="user in newMessagesUserList"
                         ng-click="selectNewMessageUser(user)">
                        <div class="user-icon-block">
                            <img ng-src="{{ user.userIcon.imageName | imagePrefix }}">
                        </div>
                        <div class="user-data-block">
                            {{ user.userFirstName + ' ' + user.userLastName }}
                        </div>
                        <div class="more-info-icon">
                            <img src="${pageContext.request.contextPath}/resources/images/assets/new-message.png" ng-if="!user.readMessage">
                            <img src="${pageContext.request.contextPath}/resources/images/assets/read-message.png" ng-if="user.readMessage">
                        </div>
                    </div>
                    <div class="user-info-block" ng-repeat="user in userList | filter:searchFriend"
                         ng-click="selectUser(user, $index)" ng-class="{'selected': $index === selectedIndex}">
                        <div class="user-icon-block">
                            <img ng-src="{{ user.userIcon.imageName | imagePrefix }}">
                        </div>
                        <div class="user-data-block">
                            {{ user.userFirstName + ' ' + user.userLastName }}
                        </div>
                        <div class="more-info-icon" ng-click="showExtraInfo()">
                            <img src="${pageContext.request.contextPath}/resources/images/assets/more.png">
                        </div>
                    </div>
                </div>
            </div>

            <div class="user-extra-info" ng-show="extraInfoIsVisible">
                <div class="extra-info-block">
                    <div class="extra-name">{{ selectedUser.userFirstName + ' ' + selectedUser.userLastName }}</div>
                    <div class="extra-divider"></div>
                    <div class="extra-icon">
                        <img ng-src="{{ selectedUser.userIcon.imageName | imagePrefix }}">
                    </div>
                    <div class="extra-divider"></div>
                    <div class="extra-field">
                        <div class="extra-field-name">Login:</div>
                        {{selectedUser.userLogin}}
                    </div>
                    <div class="extra-field">
                        <div class="extra-field-name">Email:</div>
                        {{selectedUser.userEmail}}
                    </div>
                    <div class="extra-field">
                        <div class="extra-field-name">Phone:</div>
                        {{selectedUser.userPhone}}
                    </div>
                    <div class="extra-field">
                        <div class="extra-field-name">Address:</div>
                        {{selectedUser.userAddress}}
                    </div>
                    <div ng-hide="selectedUser.userLogin === currentUser.userLogin">
                        <div class="extra-divider"></div>
                        <div class="extra-buttons">
                            <div class="extra-button" ng-class="{'enable': isSearch}"
                                 ng-click="addFriend(selectedUser.userLogin)">Add
                            </div>
                            <div class="extra-button" ng-class="{'enable': !isSearch}"
                                 ng-click="removeFriend(selectedUser.userLogin)">Remove
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="right-block">
            <div class="tab-box">
                <div class="tabs">
                    <div ng-class="{'active' : selectedTab === CALENDAR_TAB}" class="tab"
                         ng-click="setTab(CALENDAR_TAB)">
                        Calendar
                    </div>
                    <div ng-class="{'active' : selectedTab === GALLERY_TAB}" class="tab"
                         ng-click="setTab(GALLERY_TAB)">
                        Gallery
                    </div>
                    <div ng-class="{'active' : selectedTab === CHAT_TAB}" class="tab"
                         ng-click="setTab(CHAT_TAB)">
                        Chat
                    </div>
                    <div ng-class="{'active' : selectedTab === EVENTS_TAB}" class="tab"
                         ng-click="setTab(EVENTS_TAB)">
                        Events
                    </div>
                </div>
            </div>
            <div class="active-tab" ng-switch="selectedTab">
                <div ng-switch-when="calendar" class="calendar-tab" ng-controller="calendarController">
                    <jsp:include page="calendar.jsp"/>
                </div>
                <div ng-switch-when="gallery" class="gallery-tab" ng-controller="galleryController">
                    <jsp:include page="gallery.jsp"/>
                </div>
                <div ng-switch-when="chat" class="chat-tab">
                    <jsp:include page="chat.jsp"/>
                </div>
                <div ng-switch-when="events" class="events-tab" ng-controller="eventsController">
                    <jsp:include page="events.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script type="text/javascript" src="webjars/stomp-websocket/2.3.3/stomp.min.js"></script>
</body>
</html>
