'use strict';

angular.module('mainModule')
    .controller('chatController',
        ['$scope', 'ChatSocket', 'Message', '$filter',
            function ($scope, ChatSocket, Message, $filter) {
                var DATE_PATTERN = 'dd-MM-yyyy';
                var TIME_PATTERN = 'HH:mm:ss';

                $scope.messages = null;
                $scope.receiver = null;
                $scope.newMessagesUserList = [];
                $scope.newMessage = {
                    messageText: ''
                };

                initChatSocket();

                $scope.sendMessage = function () {
                    if ($scope.receiver) {
                        var destination = '/app/chat.private.' + $scope.receiver.userLogin;
                        ChatSocket.send(destination, {}, $scope.newMessage.messageText);
                        clearNewMessage();
                    }
                };

                function clearNewMessage() {
                    $scope.newMessage.messageText = '';
                }

                $scope.$on('onUserSelected', function (event, user) {
                    if ($scope.selectedTab != $scope.CHAT_TAB) {
                        return;
                    }

                    if (user == null) {
                        $scope.messages = [];
                        $scope.receiver = user;
                        return;
                    }

                    if (user != $scope.receiver) {
                        $scope.receiver = user;
                        setMessages(user.userLogin);
                    }
                });

                function setMessages(userLogin) {
                    Message.getMessagesFromUser(userLogin).then(function (messages) {
                        messages.forEach(function (message) {
                            formatMessage(message);
                        });
                        $scope.messages = messages;
                    })
                }

                function formatMessage(message) {
                    message.isOwn = $scope.currentUser.userLogin == message.messageSender.userLogin;
                    var messageTime = new Date(message.messageTime);
                    message.date = $filter('date')(messageTime, DATE_PATTERN);
                    message.time = $filter('date')(messageTime, TIME_PATTERN);
                }

                function initChatSocket() {
                    ChatSocket.init(applicationContext + '/ws');
                    ChatSocket.connect(function () {
                        ChatSocket.subscribe("/user/exchange/amq.direct/chat.message", function (message) {
                            receiveNewMessage(message);
                        });
                    }, function (error) {
                        // TODO Create message error: connection lost
                    });
                }

                $scope.selectNewMessageUser = function (newUser) {
                    $scope.$broadcast('changeSelectedUser', newUser, function () {
                        $scope.newMessagesUserList = $scope.newMessagesUserList.filter(function (user) {
                            return user.userLogin != newUser.userLogin;
                        });
                    });
                    newUser.readMessage = true;
                    $scope.setTab($scope.CHAT_TAB);
                    $scope.receiver = newUser;
                    setMessages(newUser.userLogin);
                };

                function receiveNewMessage(newMessage) {
                    var message = JSON.parse(newMessage.body);
                    formatMessage(message);
                    if ($scope.selectedTab != $scope.CHAT_TAB) {
                        notifyUser(message);
                        return;
                    }
                    if (message.messageSender.userLogin == $scope.currentUser.userLogin ||
                        message.messageSender.userLogin == $scope.receiver.userLogin) {
                        updateMessages(message);
                    } else {
                        notifyUser(message);
                    }
                }

                function updateMessages(message) {
                    $scope.messages.push(message);
                    $scope.$apply();
                }

                function notifyUser(message) {
                    var contains = $scope.newMessagesUserList.some(function (user) {
                        if (user.userLogin == message.messageSender.userLogin) {
                            user.readMessage = false;
                            return true;
                        }
                        return false;
                    });
                    if (!contains) {
                        $scope.newMessagesUserList.push(message.messageSender);
                    }
                    $scope.$apply();
                }
            }]);