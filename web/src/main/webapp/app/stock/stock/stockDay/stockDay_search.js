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
        var defaults = {
            orderBy: 'businessDate',
            reverse: false
        }; // 默认查询条件

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
                var param = angular.extend({start: this.start, limit: this.limit}, $scope.condition);
                $scope.beans = [];
                if (param.businessDateLt) {
                    param.businessDateLt = moment(param.businessDateLt).add(1, 'd').format('YYYY-MM-DD');
                }
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


        $scope.order = function (key) {
            $scope.condition.orderBy = key;
            $scope.condition.reverse = !$scope.condition.reverse;
            $scope.orderBy = key;
            $scope.reverse = $scope.condition.reverse;
            if ($scope.pager.query) {
                $scope.query();
            }
        };

        $scope.order('businessDate');
    });
})(window, angular, jQuery);