<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="chat-wrapper">
    <div class="messages-wrapper" scroll-glue>
        <div ng-repeat="message in messages">
            <div class="message-wrapper" ng-if="message.isOwn">
                <div class="chat-message myself">
                    {{ message.message.messageText }}
                </div>
                <div class="message-info myself">
                    <div class="chat-message-time myself" title="{{message.date}}">
                        {{ message.time }}
                    </div>
                </div>
            </div>

            <div class="message-wrapper" ng-if="!message.isOwn">
                <div class="message-info">
                    <div class="chat-message-time" title="{{message.date}}">
                        {{ message.time }}
                    </div>
                </div>
                <div class="chat-message">
                    {{ message.message.messageText }}
                </div>
            </div>
        </div>
        <div id="down-anchor" hidden="hidden"></div>
    </div>
    <div class="chat-input-wrapper">
        <div class="chat-input">
            <input class="message-input" ng-model="newMessage.messageText" ng-keyup="$event.keyCode == 13 && sendMessage()">
            <button class="send-message-button" ng-class="{'disabled' : !receiver }" ng-disabled="!receiver" ng-click="sendMessage()">SEND</button>
        </div>
    </div>
</div>
