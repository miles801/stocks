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

        var beans = [];

        // 参数：类型
        $scope.types = [{name: '全部', value: '0'}];
        DBParam.type(function (o) {
            $scope.types.push.apply($scope.types, o);
        });

        $scope.dateChange = function () {
            var data = [];  // 第一步过滤
            // 过滤起始BK
            if (/^\d{8}$/g.test($scope.startDate)) {
                var startDate = moment($scope.startDate).valueOf();
                angular.forEach(beans, function (o) {
                    if (o.bk > startDate) {
                        data.push(o);
                    }
                });
            } else {
                data = beans;
            }
            var data2 = []; // 第二步过滤
            // 过滤结束BK
            if (/^\d{8}$/g.test($scope.endDate)) {
                var endDate = moment($scope.endDate).valueOf();
                angular.forEach(data, function (o) {
                    if (o.bk < endDate) {
                        data2.push(o);
                    }
                });
            } else {
                data2 = data;
            }
            $scope.beans = data2;
        };

        // 查询数据
        $scope.query = function () {
            var promise = DBService.calculate($scope.condition, function (data) {
                beans = data.data || [];
                $scope.dateChange();
            });
            CommonUtils.loading(promise, 'Loading...');
        };

        $scope.query();
    });
})(window, angular, jQuery);