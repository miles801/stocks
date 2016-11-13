/**
 * 日K列表
 * 统计每只股票在某一个时间段内各个组合的阳性比
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('stock.stock.stockDay.report', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'stock.stock.stockDay'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, StockDayService, StockDayParam) {
        var defaults = {// 默认查询条件
            businessDateGe: moment().add(-1, 'M').format('YYYY-MM-DD'),
            businessDateLt: moment().format('YYYY-MM-DD')
        };

        $scope.condition = angular.extend({}, defaults);

        // 重置查询条件并查询
        $scope.reset = function () {
            $scope.condition = angular.extend({}, defaults);
            $scope.query();
        };


        // 查询数据
        $scope.query = function () {
            if ($scope.form.$invalid) {
                AlertFactory.warning('请完善查询条件!');
                return;
            }
            if (!(/^\d{6}$/g.test($scope.condition.code))) {
                AlertFactory.warning('请输入正确的股票代码（必须是6位数字）!');
                return;
            }
            var param = angular.extend({start: this.start, limit: this.limit}, $scope.condition);
            if (param.businessDateLt) {
                param.businessDateLt = moment(param.businessDateLt).add(1, 'd').format('YYYY-MM-DD');
            }
            var promise = StockDayService.report6(param, function (data) {
                $scope.beans = data.data || [];
            });
            CommonUtils.loading(promise, 'Loading...');
        };

    });
})(window, angular, jQuery);