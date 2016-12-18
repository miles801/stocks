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
        };

        // 参数：类型
        $scope.types = [];
        DBParam.type(function (o) {
            $scope.types.push.apply($scope.types, o);
        });


        // 查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        // bk对应的数据
        $scope.bks = [];

        $scope.pager = {
            allowNav: false,
            configLimit: false,
            limit: 10,
            fetch: function () {
                $scope.bks.length = 0;
                $scope.bk = null;
                var param = {
                    start: this.start,
                    limit: this.limit,
                    orderBy: $scope.orderBy,
                    reverse: $scope.reverse,
                    xtype: $scope.condition.type
                };
                if (/^\d{8}$/g.test($scope.startDate)) {
                    param.bkGe = moment($scope.startDate, 'YYYYMMDD').valueOf();
                } else if (/^\d{4}-\d{1,2}-\d{1,2}$/g.test($scope.startDate)) {
                    param.bkGe = moment($scope.startDate, 'YYYY-MM-DD').valueOf();
                }
                if (/^\d{8}$/g.test($scope.endDate)) {
                    param.bkLe = moment($scope.endDate, 'YYYYMMDD').valueOf();
                } else if (/^\d{4}-\d{1,2}-\d{1,2}$/g.test($scope.endDate)) {
                    param.bkGe = moment($scope.endDate, 'YYYY-MM-DD').valueOf();
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

                $scope.bks.length = 0;
                return CommonUtils.promise(function (defer) {
                    var promise = DBService.calculate(param, function (data) {
                        $scope.beans = data.data || {};
                        angular.forEach($scope.beans.data, function (o) {
                            angular.forEach(o.data, function (foo) {
                                $scope.bks.push(foo);
                            });
                        });
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
        };

        $scope.view = function (bk, data) {
            $scope.bk = bk;
            $scope.bks = data;
        };

    });
})(window, angular, jQuery);