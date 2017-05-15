<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.3/angular.min.js"></script>

</head>

<body>

<div ng-app="chat" ng-controller="chatCtrl">
    <div class="container" style="min-width: 95%;font-size: 12px">
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-4">
                <h5>PVT chat</h5>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="form-group" style="margin-bottom: 5px">
                            <textarea class="form-control" rows="5" ng-model="chatData"></textarea>
                        </div>
                        <button ng-click="sendData()" class="btn btn-xs" style="float: right; margin-bottom: 30px">send</button>
                        <div class="form-group">
                            <textarea class="form-control" rows="5" >{{chatPvtData}}</textarea>
                        </div>

                    </div>
                </div>
            </div>
            <div class="col-sm-7" >
                <h5>PVT board</h5>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="panel panel-default">
                                    <div class="panel-body" id="console" style="min-height: 300px"></div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div>
                                    <img src="./resources/images/pvt2.jpg">

                                </div>
                            </div>


                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<div>
    <p>
        <span id="nickname">${nickname}</span>
        <input type="text" id="chat" placeholder="${nickname} type and press 'enter' to chat" />
    </p>

</div>


</body>
<script>
    var app = angular.module('chat', []);
    app.controller('chatCtrl', function($scope, $http) {

        $scope.httppath="getPvtAnswer";
        $scope.chatPvtData="";

        $scope.sendData = function(){

            var data = {};
            data.clientData = $scope.chatData;

            $http({
                method : "POST",
                url : $scope.httppath,
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(data)

            }).then(function mySucces(response) {
                $scope.chatPvtData += response.data.name +"\n";
            }, function myError(response) {
                $scope.chatPvtData = response.statusText;
            });

        }


        $scope.websocketInit=function () {
            var Chat = {};
            Chat.socket = null;
            Chat.connect = (function(host) {
                if ('WebSocket' in window) {
                    Chat.socket = new WebSocket(host);
                } else if ('MozWebSocket' in window) {
                    Chat.socket = new MozWebSocket(host);
                } else {
                    Console.log('Error: WebSocket is not supported by this browser.');
                    return;
                }

                Chat.socket.onopen = function () {
                    var nickname = document.getElementById('nickname').innerHTML;
                    Chat.socket.send('##'+nickname);
                    console.log('Info: WebSocket connection opened.');
                    document.getElementById('chat').onkeydown = function(event) {
                        if (event.keyCode == 13) {
                            var message = Chat.getMessage();
                            document.getElementById('chat').value = '';
                            Chat.socket.send(message +"\n");
                        }else{
                            Chat.socket.send('typing');
                        }
                    };

                    while(!documentReady){
                        setTimeout(function() {}, 500);
                    }
                    Chat.socket.send("#ready");

                };

                Chat.socket.onclose = function () {
                    document.getElementById('chat').onkeydown = null;
                    Output.log('Info: WebSocket closed.');
                };

                Chat.socket.onmessage = function (message) {
                    Output.show(message.data);
                };
            });

            Chat.initialize = function() {
                if (window.location.protocol == 'http:') {
                    Chat.connect('ws://' + window.location.host + '/pvt/websocket/chat');
                } else {
                    Chat.connect('wss://' + window.location.host + '/pvt/websocket/chat');
                }
            };

            Chat.getMessage = (function() {
                var message = document.getElementById('chat').value;
                if (message != '') {
                    return message;
                }
            });

            var Output = {};

            Output.show = (function(message) {
                var outputblock = document.getElementById('console');
                if(message=="typing"){
                    return;
                }

                var utterance = new SpeechSynthesisUtterance();
                utterance.text = message;
                utterance.lang = 'ru-RU';
                utterance.onend = function(e) {
                    Chat.socket.send("#continue");
                };
                window.speechSynthesis.speak(utterance);

                console.info(message);
                if(message.startsWith("\n")){
                    var p = document.createElement('p');
                    outputblock.appendChild(p);
                }

                var span = document.createElement('span');
                span.style.wordWrap = 'break-word';
                span.innerHTML = message;
                outputblock.appendChild(span);
                while (outputblock.childNodes.length > 25) {
                    outputblock.removeChild(outputblock.firstChild);
                }
                outputblock.scrollTop = outputblock.scrollHeight;
            });

            Chat.initialize();


        }
        $scope.websocketInit();



    });

    document.addEventListener("DOMContentLoaded", function() {
        documentReady=true;

    });



</script>

</html>
