/**
 * 日K列表
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('stock.stock.stockDay.report', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'stock.stock.stockDay'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, StockDayService, StockDayParam) {
        $scope.orderBy = 'key1';
        var defaults = {// 默认查询条件
            businessDateGe: moment('19901219').format('YYYYMMDD'),
            businessDateLt: moment().format('YYYYMMDD'),
            orderBy: 'key1'
        };

        $scope.condition = angular.extend({}, defaults);

        // 重置查询条件并查询
        $scope.reset = function () {
            $scope.condition = angular.extend({}, defaults);
            $scope.query();
        };

        $scope.pager = {
            showTotal:false,
            limit: 50,
            fetch: function () {
                $scope.pager.total = 64 * 3000;
                return CommonUtils.promise(function (defer) {
                    if ($scope.condition.code && !(/^\d{6}$/g.test($scope.condition.code))) {
                        AlertFactory.warning('请输入正确的股票代码（必须是6位数字）!');
                        return;
                    }
                    var param = angular.extend({start: $scope.pager.start, limit: $scope.pager.limit}, $scope.condition);
                    if (param.businessDateLt) {
                        param.businessDateLt = moment(param.businessDateLt).add(1, 'd').format('YYYYMMDD');
                    }
                    var promise = StockDayService.report3(param, function (data) {
                        $scope.beans = data.data || [];
                        defer.resolve(64 * 3000);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            }
        };

        // 查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        $scope.order = function (key) {
            $scope.condition.orderBy = key;
            $scope.condition.reverse = !$scope.condition.reverse;
            $scope.orderBy = key;
            $scope.reverse = $scope.condition.reverse;
            $scope.query();
        }

    });
})(window, angular, jQuery);