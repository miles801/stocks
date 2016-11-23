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
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, DBService, DBParam) {

        $scope.orderBy = 'bk';
        $scope.reverse = false;
        $scope.condition = {
            type: 3,
            db: '1',
            value: 10
        };

        // 参数：类型
        $scope.types = [{name: '全部', value: '0'}];
        DBParam.type(function (o) {
            $scope.types.push.apply($scope.types, o);
        });

        // 查询数据
        $scope.query = function () {
            var promise = DBService.calculate($scope.condition, function (data) {
                $scope.beans = data.data || [];
            });
            CommonUtils.loading(promise, 'Loading...');
        };

        $scope.query();
    });
})(window, angular, jQuery);