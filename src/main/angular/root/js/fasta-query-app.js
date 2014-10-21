var fastaqueryapp = angular.module("fastaQueryApp", [])


fastaqueryapp.controller('FastaQueryCtrl', function($scope, $http){

  $scope.result = "";
  var defaultForm = {
    "query": ""
  };

  $scope.clearBox = function(){
    $scope.formData = defaultForm;
    $scope.result = "";
  };

  $scope.processQuery = function(){
    $http({
        method: 'POST',
        url: "/batchquery",
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        transformRequest: function(obj) {
            var str = [];
            for(var p in obj)
              str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
              return str.join("&");
        },
        data: $scope.formData 
    }).success(function (data) {$scope.result = data});
  }});
