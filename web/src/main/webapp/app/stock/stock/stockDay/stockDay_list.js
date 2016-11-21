/**
 * 日K列表
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('stock.stock.stockDay.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'stock.stock.stockDay'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, StockDayService, StockDayParam) {
        var defaults = {// 默认查询条件
            orderBy: 'businessDate',
            businessDate: moment().format('YYYY-MM-DD'),
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
                    var promise = StockDayService.pageQuery(param, function (data) {
                        param = null;
                        $scope.beans = data.data || {total: 0};
                        defer.resolve($scope.beans);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            }
        };

        // 删除或批量删除
        $scope.remove = function (id) {
            if (!id) {
                var ids = [];
                angular.forEach($scope.items || [], function (o) {
                    ids.push(o.id);
                });
                id = ids.join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="text-danger">数据一旦删除将不可恢复，请确认!</span>',
                callback: function () {
                    var promise = StockDayService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function () {
            CommonUtils.addTab({
                title: '新增日K',
                url: '/stock/stock/stockDay/add',
                onUpdate: $scope.query
            });
        };

        // 同步今日交易数据
        $scope.sync = function () {
            ModalFactory.confirm({
                scope: $scope,
                content: '是否覆盖今日的交易数据，请确认!',
                callback: function () {
                    var promise = StockDayService.sync(function () {
                        AlertFactory.success('同步成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };
        // 重置第7日交易数据
        $scope.reset7 = function () {
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="text-danger">是否重置所有的交易数据的计算结果，请确认!</span>',
                callback: function () {
                    var promise = StockDayService.reset7(function () {
                        AlertFactory.success('同步成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        // 更新
        $scope.modify = function (id) {
            CommonUtils.addTab({
                title: '更新日K',
                url: '/stock/stock/stockDay/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看日K',
                url: '/stock/stock/stockDay/detail?id=' + id
            });
        };

        // 导入数据
        $scope.importData = function (id) {
            CommonUtils.addTab({
                title: '导入数据',
                url: '/stock/stock/stockDay/import',
                onClose: $scope.query
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
            window.open(CommonUtils.contextPathURL('/stock/stock/stockDay/export?' + encodeURI(encodeURI($.param(o)))));
        };

    });
})(window, angular, jQuery);