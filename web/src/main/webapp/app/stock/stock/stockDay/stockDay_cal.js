/**
 * 日K列表
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('stock.db.calculate', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'stock.db.dB'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, DBService) {

        $scope.type = 3;

        // 查询数据
        $scope.query = function () {
            var promise = DBService.calculate({type:$scope.type}, function (data) {
                $scope.beans = data.data || [];
            });
            CommonUtils.loading(promise, 'Loading...');
        };

        $scope.query();
    });
})(window, angular, jQuery);