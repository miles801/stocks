/**
 * 周K列表
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('stock.stock.stockWeek.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'stock.stock.stockWeek'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, StockWeekService, StockWeekParam) {
        var defaults = {// 默认查询条件
            orderBy: 'closeDate',
            reverse: true
        };

        $scope.condition = angular.extend({}, defaults);

        // 重置查询条件并查询
        $scope.reset = function () {
            $scope.condition = angular.extend({}, defaults);
            $scope.query();
        };


        // 查询数据
        $scope.query = function () {
            $scope.pager.query();
        };
        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                $scope.beans = [];
                return CommonUtils.promise(function (defer) {
                    var promise = StockWeekService.pageQuery(param, function (data) {
                        param = null;
                        $scope.beans = data.data || {total: 0};
                        defer.resolve($scope.beans);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            },
            finishInit: function () {
                this.query();
            }
        };

        $scope.resetData = function () {
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="text-danger">重置所有周K数据，请慎重操作!</span>',
                callback: function () {
                    var promise = StockWeekService.reset(function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };
        $scope.add = function () {
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="text-danger">重置本周所有周K数据，请慎重操作!</span>',
                callback: function () {
                    var promise = StockWeekService.add(function () {
                        AlertFactory.success('操作成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看周K',
                url: '/stock/stock/stockWeek/detail?id=' + id
            });
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
            window.open(CommonUtils.contextPathURL('/stock/stock/stockWeek/export?' + encodeURI(encodeURI($.param(o)))));
        };

    });
})(window, angular, jQuery);