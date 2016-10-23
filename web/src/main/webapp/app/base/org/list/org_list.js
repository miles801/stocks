(function (window, angular, $) {

    var app = angular.module('base.org.list', [
        'base.org',
        'eccrm.angular',
        'eccrm.angular.ztree',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, OrgService, OrgTree) {

        // 添加机构
        $scope.add = function () {
            var beans = $scope.beans || {};
            $scope.beans = {
                minEmp: 0,
                maxEmp: 999,
                level: (beans.level || 0) + 1,
                sequenceNo: (beans.children || []).length + 1,
                parentId: beans.id,
                parentName: beans.id ? beans.name : ""
            };
        };

        // 初始化树
        var initTree = function () {
            OrgTree.tree($scope, 'orgTree', function (o) {
                $scope.beans = o;
            });
        };

        // 选择组织机构
        $scope.parentOptions = OrgTree.pick(function (o) {
            $scope.beans.parentId = o.id;
            $scope.beans.parentName = o.name;
            $scope.beans.level = o.level + 1;
            $scope.beans.sequenceNo = (o.children || []).length + 1;
        });

        // 清除选择的组织机构
        $scope.clearOrg = function () {
            var beans = $scope.beans || {};
            beans.parentId = null;
            beans.parentName = null;
            beans.level = 0;
        };

        /**
         * 保存
         */
        $scope.save = function (addNew) {
            var promise = OrgService.save($scope.beans, function () {
                AlertFactory.success('保存成功!');
                if (addNew) {
                    $scope.beans.name = null;
                    $scope.beans.longName = null;
                    $scope.beans.code = null;
                    $scope.beans.sequenceNo = ($scope.beans.sequenceNo || 0) + 1;
                    return;
                }
                $scope.beans = {};
                initTree();
            });
            CommonUtils.loading(promise);
        };


        /**
         * 移除
         */
        $scope.remove = function () {
            if (!$scope.beans.id) {
                AlertFactory.warning("请选择需要禁用的组织机构!");
                return;
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '注销后，该机构将不可见，请确认?',
                callback: function () {
                    var promise = OrgService.deleteByIds({ids: $scope.beans.id}, function () {
                        AlertFactory.success('操作成功!');
                        initTree();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };

        $scope.enable = function () {
            if (!$scope.beans.id) {
                AlertFactory.warning("请选择需要启用的组织机构!");
                return;
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '启用该组织机构的同时，会同时启用其所有的上级机构，请确认?',
                callback: function () {
                    var promise = OrgService.enable({ids: $scope.beans.id}, function () {
                        AlertFactory.success('操作成功!');
                        initTree();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };


        $scope.update = function () {
            var promise = OrgService.update($scope.beans, function () {
                AlertFactory.success($scope, '更新成功!');
            });
            CommonUtils.loading(promise);
        };


        initTree();

    });
})(window, angular, jQuery);