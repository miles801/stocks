/**
 * Created by miles on 13-11-25.
 */
(function (angular, $) {
    var app = angular.module('com.michael.base.datadic.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'com.michael.base.datadic'
    ]);


    app.controller('MainCtrl', function ($scope, DataDicService, CommonUtils, ModalFactory) {
        $scope.condition = {};  // 查询条件
        $scope.beans = [];      // 结果

        // 定义变量
        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, $scope.condition, {
                    start: $scope.pager.start,
                    limit: $scope.pager.limit
                });
                return CommonUtils.promise(function (defer) {
                    CommonUtils.loading(defer, '加载数据字典列表...');
                    DataDicService.pageQuery(param, function (data) {
                        data = data.data;
                        $scope.beans = data.data || [];
                        defer.resolve(data.total);
                    });
                });
            }
        };

        $scope.add = function () {
            CommonUtils.addTab({
                title: '新增字典',
                url: '/base/datadic/add',
                onUpdate: $scope.query
            });
        };


        $scope.detail = function (id) {
            CommonUtils.addTab({
                title: '查看字典',
                url: '/base/datadic/detail?id=' + id,
                onUpdate: $scope.query
            });
        };

        $scope.modify = function (id) {
            CommonUtils.addTab({
                title: '更新字典',
                url: '/base/datadic/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 删除
        $scope.remove = function (id) {
            ModalFactory.remove($scope, function () {
                var promise = DataDicService.deleteByIds({ids: id});
                CommonUtils.loading(promise, '删除中...', $scope.query);
            });
        };

        // 定义方法
        $scope.query = function () {
            $scope.pager.query();
        };


        // =========================== 执行方法 ===========================


    });
})(angular, jQuery);