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
                    var promise = StockDayService.result6(param, function (data) {
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
            window.open(CommonUtils.contextPathURL('/stock/stock/stockDay/export-result6?' + encodeURI(encodeURI($.param(o)))));
        };

        $scope.order = function (key) {
            $scope.condition.orderBy = key;
            $scope.condition.reverse = !$scope.condition.reverse;
            $scope.orderBy = key;
            $scope.reverse = $scope.condition.reverse;
            if ($scope.pager.query) {
                $scope.query();
            }
        };

        $scope.resetData = function () {
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="text-danger">是否重新产生计算结果（默认每天凌晨2点产生新的结果），请确认!</span>',
                callback: function () {
                    var promise = StockDayService.resetDayResult(function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        $scope.order('code');


    });
})(window, angular, jQuery);