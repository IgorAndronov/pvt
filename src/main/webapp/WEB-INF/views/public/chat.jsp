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
                        <button ng-click="sendDataAjax()" class="btn btn-xs" style="float: right; margin-bottom: 30px">send</button>
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
        <textarea class="form-control" rows="2" id="chat" placeholder="${nickname} type and press 'enter' to chat"></textarea>
    </p>

</div>


</body>
<script>
    var app = angular.module('chat', []);
    app.controller('chatCtrl', function($scope, $http) {

        $scope.chatPvtData="";

        $scope.sendDataAjax = function(){
            var httppath="getPvtAnswer";
            var data = {};
            data.clientData = $scope.chatData;

            $http({
                method : "POST",
                url : httppath,
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

        $scope.chat ={};
        $scope.registerTypingEventHandler=function(){

            document.getElementById('chat').onkeydown = function(event) {
                if (event.keyCode == 13) {
                    var message = $scope.chat.getMessage();
                    document.getElementById('chat').value = '';
                    $scope.chat.socket.send(message +"\n");
                }else{
                    $scope.chat.socket.send('typing');
                }
            };
        }

        $scope.chat.getMessage = (function() {
            var message = document.getElementById('chat').value;
            if (message != '') {
                return message;
            }
        });

        $scope.output = {};
        $scope.output.show = (function(message) {
            var outputblock = document.getElementById('console');
            if(message=="typing"){
                return;
            }

            var utterance = new SpeechSynthesisUtterance();
            utterance.text = message;
            utterance.lang = 'ru-RU';
            var voices = window.speechSynthesis.getVoices();
            utterance.voice = voices.filter(function(voice) { return voice.lang == 'ru-RU'; })[0];
            utterance.onend = function(e) {
                console.info("send #continue");
                $scope.chat.socket.send("#continue");
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

        $scope.websocketInit=function () {

            $scope.chat.socket = null;

            (function socketInitialize() {
                var host;

                if (window.location.protocol == 'http:') {
                    host= 'ws://' + window.location.host + '/pvt/websocket/chat';
                } else {
                    host = 'wss://' + window.location.host + '/pvt/websocket/chat';
                }

                if ('WebSocket' in window) {
                    $scope.chat.socket = new WebSocket(host);
                } else if ('MozWebSocket' in window) {
                    $scope.chat.socket = new MozWebSocket(host);
                } else {
                    Console.log('Error: WebSocket is not supported by this browser.');
                    return;
                }
            })();

            $scope.chat.socket.onopen = function () {
                while(!documentReady){
                    setTimeout(function() {}, 500);
                }

                var nickname = document.getElementById('nickname').innerHTML;
                $scope.chat.socket.send('##'+nickname);
                console.log('Info: WebSocket connection opened.');

                $scope.chat.socket.send("#ready");

            };

            $scope.chat.socket.onclose = function () {
                document.getElementById('chat').onkeydown = null;
                console.log('Info: WebSocket closed.');
            };

            $scope.chat.socket.onmessage = function (message) {
                $scope.output.show(message.data);
            };

            $scope.registerTypingEventHandler();

        }


        $scope.websocketInit();

    });

    document.addEventListener("DOMContentLoaded", function() {
        documentReady=true;

    });



</script>

</html>
