(function (angular, $) {
    var app = angular.module('base.resource.list', [
        'base.resource',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    app.controller('Ctrl', function ($scope, ResourceService, ResourceTree, CommonUtils, ModalFactory, AlertFactory) {

        // 当前被选中的节点
        var bean = null;
        var type = $('#type').val();
        if (!type) {
            AlertFactory.error('错误的访问!未指明页面类型!');
            return;
        }

        //定义方法
        $scope.query = function () {
            if (!bean || !bean.id) {
                AlertFactory.warning('请选中节点后再查询!');
                return;
            }
            var promise = ResourceService.children({id: bean.id}, function (data) {
                $scope.beans = data.data || [];
                if (bean) {
                    $scope.beans.unshift(bean);
                }
            });
            CommonUtils.loading(promise);
        };

        var initTree = function () {
            ResourceTree.menuTree($scope, 'treeDemo', function (node) {
                bean = node;
                $scope.query();
            }, type);
        };

        $scope.remove = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '禁用该资源时，会同时禁用该资源下的所有子资源，请确认!',
                callback: function () {
                    var promise = ResourceService.disable({ids: id}, function () {
                        AlertFactory.success('禁用成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise, '删除中...');
                }
            });
        };
        $scope.enable = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '是否启用该资源，请确认!',
                callback: function () {
                    var promise = ResourceService.enable({ids: id}, function () {
                        AlertFactory.success('启用成功!');
                        $scope.query();
                    });
                    CommonUtils.loading(promise, '删除中...');
                }
            });
        };

        $scope.add = function () {
            CommonUtils.addTab({
                title: '新建资源',
                url: '/base/resource/add?type=' + type,
                onUpdate: initTree
            });
        };

        $scope.modify = function (id) {
            CommonUtils.addTab({
                title: '更新资源',
                url: "/base/resource/modify?id=" + id,
                onUpdate: initTree
            });
        };
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '资源明细',
                url: "base/resource/detail?id=" + id
            });
        };
        //执行方法
        initTree();
    })
})(angular, jQuery);