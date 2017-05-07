<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                            <textarea class="form-control" rows="5" ng-model="chatData" ></textarea>
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
                                <textarea class="form-control" rows="25" ></textarea>
                            </div>
                            <div class="col-sm-6">
                                <textarea class="form-control" rows="25" ></textarea>
                            </div>


                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
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





    });
</script>

</html>
