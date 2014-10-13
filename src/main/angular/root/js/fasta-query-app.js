var fastaqueryapp = angular.module("fastaQueryApp", [])


fastaqueryapp.controller('FastaQueryCtrl', function($scope, $http){
  //$scope.querytext = 'whatever';
  $scope.result = "";
  $scope.formData = {};
  var config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}};
  $scope.processQuery = function(){
    //$http.post("/batchquery", $scope.formData, config).success(function(data){ $scope.result = data});};
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
