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
            db: '1',
            value: 0,
            type: 3
        }
        ;

        // 参数：类型
        $scope.types = [];
        DBParam.type(function (o) {
            $scope.types.push.apply($scope.types, o);
        });


        // 查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        $scope.pager = {
            fetch: function () {
                var param = {
                    start: this.start,
                    limit: this.limit,
                    orderBy: $scope.orderBy,
                    reverse: $scope.reverse,
                    xtype: $scope.condition.type
                };
                if (/^\d{8}$/g.test($scope.startDate)) {
                    param.bkGe = moment($scope.startDate, 'YYYYMMDD').valueOf();
                }
                if (/^\d{8}$/g.test($scope.endDate)) {
                    param.bkLe = moment($scope.endDate, 'YYYYMMDD').valueOf();
                }
                if (/^\d+$/g.test($scope.condition.value)) {
                    param.fnGe = -$scope.condition.value;
                    param.fnLe = $scope.condition.value;
                }
                if ($scope.condition.db == '5') {
                    param.typeIn = [1, 2];
                } else if ($scope.condition.db == '6') {
                    param.typeIn = [3, 4];
                } else if (/^\d$/g.test($scope.condition.db)) {
                    param.type = parseInt($scope.condition.db);
                }

                return CommonUtils.promise(function (defer) {
                    var promise = DBService.calculate(param, function (data) {
                        $scope.beans = data.data || {};
                        defer.resolve($scope.beans);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            }
        };

        $scope.order = function (key) {
            $scope.orderBy = key;
            $scope.reverse = !$scope.reverse;
            $scope.query();
        }

    });
})(window, angular, jQuery);