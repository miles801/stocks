/**
 * Created by Michael on 2016-04-25 13:24:54.
 */
(function (window, angular, $) {
    var app = angular.module('base.emp.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'base.emp'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, EmpService, EmpParam) {
        $scope.condition = {};

        //查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                $scope.beans = [];
                return CommonUtils.promise(function (defer) {
                    var promise = EmpService.pageQuery(param, function (data) {
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

        // 删除或批量删除
        $scope.remove = function (id, locked) {
            if (!id) {
                var ids = [];
                angular.forEach($scope.items, function (o) {
                    ids.push(o.id);
                });
                id = ids.join(',');
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '确定要将该员工禁用，请确认!',
                callback: function () {
                    var promise = EmpService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.pager.load();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        $scope.start = function (id, locked) {
            ModalFactory.confirm({
                scope: $scope,
                content: '确定要启用该员工，请确认!',
                callback: function () {
                    var promise = EmpService.start({ids: id}, function () {
                        AlertFactory.success('操作成功!');
                        $scope.pager.load();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function () {
            CommonUtils.addTab({
                title: '新增员工',
                url: '/base/emp/add',
                onUpdate: $scope.query
            });
        };

        // 更新
        $scope.modify = function (id) {
            CommonUtils.addTab({
                title: '更新员工',
                url: '/base/emp/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看员工',
                url: '/base/emp/detail?id=' + id
            });
        }
    });
})(window, angular, jQuery);