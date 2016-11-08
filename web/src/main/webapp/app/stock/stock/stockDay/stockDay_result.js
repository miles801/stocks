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
        var defaults = {}; // 默认查询条件

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
        var beans = [];
        $scope.pager = {
            fetch: function () {
                var start = this.start;
                var limit = this.limit;
                if (start == 0) {
                    $scope.beans = [];
                    return CommonUtils.promise(function (defer) {
                        var promise = StockDayService.report6($scope.condition, function (data) {
                            beans = data.data || [];
                            var total = beans.length;
                            $scope.beans = beans.slice(start, start + limit > total ? total : start + limit);
                            defer.resolve(total);
                        });
                        CommonUtils.loading(promise, 'Loading...');
                    });
                } else {
                    var end = start + limit;
                    var total = beans.length;
                    $scope.beans = beans.slice(start, end > total ? total : end);
                }
            },
            finishInit: function () {
                this.query();
            }
        };
    });
})(window, angular, jQuery);