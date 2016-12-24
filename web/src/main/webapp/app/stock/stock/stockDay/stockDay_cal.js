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
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, DBService, DBParam, Fn4Service, Fn5Service, HandleModal) {

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
            var type = $scope.condition.type;
            $scope.bks.length = 0;
            if (type == '3') {
                $scope.pager.query();
            } else if (type == '4') {
                Fn4Service.lastHandle({type: $scope.condition.db}, function (o) {
                    if (!o.data) {
                        AlertFactory.error('该类型还未进行过处理，请先处理过后再进行查询!');
                        return;
                    }
                    $scope.pager.query();
                })
            } else if (type == '5') {
                Fn5Service.lastHandle({type: $scope.condition.db}, function (o) {
                    if (!o.data) {
                        AlertFactory.error('该类型还未进行过处理，请先处理过后再进行查询!');
                        return;
                    }
                    $scope.pager.query();
                })
            }
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


        var date1, date2;
        $scope.handle = function (type) {
            HandleModal.handle(type, $scope.condition.db, date1, date2, function (o1, o2) {
                date1 = o1;
                date2 = o2;
                AlertFactory.success('后台处理程序已经启动，请耐心等候!');
            }, function () {
                AlertFactory.success('后台处理程序已经完成，可以执行查询！');
            })
        };
    });


    app.service('HandleModal', function ($modal, $filter, CommonUtils, AlertFactory, ModalFactory, Fn4Service, Fn5Service) {
        return {
            /**
             * @param fn 类型
             * @param type 数据库类型
             * @param date1 原来输入的日期
             * @param date2 原来输入的日期
             * @param callback 回调
             * @param callback2 真正的回调
             */
            handle: function (fn, type, date1, date2, callback, callback2) {
                var modal = $modal({
                    template: CommonUtils.contextPathURL('/app/stock/stock/stockDay/fn_handle.html'),
                    backdrop: 'static'
                });
                var $scope = modal.$scope;
                var service = fn == '4' ? Fn4Service : Fn5Service;
                service.lastHandle({type: type}, function (o) {
                    if (o.data) {
                        o = o.data || {};
                        $scope.date1 = moment(o.startTime).format('YYYYMMDD');
                        $scope.date2 = moment(o.endTime).format('YYYYMMDD');
                        $scope.lastHandle = o.handleTime;
                    } else {
                        $scope.date1 = date1;
                        $scope.date2 = date2;
                    }
                });
                $scope.ok = function () {
                    if (!/^\d{8}$/g.test($scope.date1)) {
                        AlertFactory.warning('不合法的日期格式!');
                        return;
                    }
                    if (!/^\d{8}$/g.test($scope.date2)) {
                        AlertFactory.warning('不合法的日期格式!');
                        return;
                    }
                    var o1 = moment($scope.date1, 'YYYYMMDD').valueOf();
                    var o2 = moment($scope.date2, 'YYYYMMDD').valueOf();
                    var result = true;
                    service.reset({type: type, date1: o1, date2: o2}, function (o) {
                        if (o.error) {
                            result = false;
                        } else {
                            if (angular.isFunction(callback2)) {
                                callback2();
                            }
                        }
                    });
                    modal.hide();
                    callback($scope.date1, $scope.date2);
                };

                // 拖动
                ModalFactory.afterShown(modal, function () {
                    var $draggable = $('.modal-content').draggabilly({
                        containment: '.modal'
                    })
                });

            }
        }
    });
})(window, angular, jQuery);