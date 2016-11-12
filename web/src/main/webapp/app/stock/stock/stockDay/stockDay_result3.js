/**
 * 日K列表
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('stock.stock.stockDay.result', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'stock.stock.stockDay'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, StockDayService) {
        var defaults = {
            orderBy: 'code',
            reverse: false
        }; // 默认查询条件

        $scope.condition = angular.extend({}, defaults);

        var defer = CommonUtils.defer();
        StockDayService.lastDay(function (o) {
            defer.resolve(o.data);
            defaults.businessDate = o.data;
            $scope.condition.businessDate = o.data;
        });

        // 重置查询条件并查询
        $scope.reset = function () {
            $scope.condition = angular.extend({}, defaults);
            $scope.query();
        };


        // 查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        $scope.order = function (key) {
            if ($scope.condition.orderBy == key) {
                $scope.condition.reverse = !$scope.condition.reverse;
            } else {
                $scope.condition.orderBy = key;
                $scope.condition.reverse = false;
            }
            $scope.query();
        };

        $scope.pager = {
            fetch: function () {
                $scope.beans = [];
                var param = angular.extend({start: this.start, limit: this.limit}, $scope.condition);
                return CommonUtils.promise(function (defer) {
                    if (!$scope.condition.businessDate) {
                        AlertFactory.error('未获取到最近的交易日!请刷新该页面重试，或者请检查交易数据是否存在!');
                        return;
                    }
                    var promise = StockDayService.report3(param, function (data) {
                        $scope.beans = data.data || {total: 0};
                        defer.resolve($scope.beans);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            },
            finishInit: function () {
                var context = this;
                defer.promise.then(function () {
                    context.query();
                });
            }
        };

        // 导出数据
        $scope.exportData = function () {
            if ($scope.pager.total < 1) {
                AlertFactory.error('未获取到可以导出的数据!请先查询出数据!');
                return;
            }
            var o = angular.extend({}, $scope.condition);
            o.start = null;
            o.limit = null;
            window.open(CommonUtils.contextPathURL('/stock/stock/stockDay/export-result?' + encodeURI(encodeURI($.param(o)))));
        };
    });
})(window, angular, jQuery);