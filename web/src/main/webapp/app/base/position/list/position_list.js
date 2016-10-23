/**
 * Created by Michael on 2016-05-03 11:09:53.
 */
(function (window, angular, $) {
    var app = angular.module('base.position.list', [
        'eccrm.angular',
        'eccrm.angular.ztree',
        'eccrm.angularstrap',
        'base.position'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, PositionService, PositionTree) {

        //查询数据
        $scope.query = function () {
        };

        var initTree = function () {
            PositionTree.tree($scope, 'positionTree', function (o) {
                $scope.beans = o;
            });
        };

        $scope.parentOptions = PositionTree.pick(function (o) {
            $scope.beans.parentId = o.id;
            $scope.beans.parentName = o.name;
        });

        $scope.clearParent = function () {
            $scope.beans.parentId = null;
            $scope.beans.parentName = null;
        };

        // 禁用
        $scope.disable = function () {
            ModalFactory.confirm({
                scope: $scope,
                content: '禁用该岗位时，会级联禁用该岗位下所有的岗位，请确认!',
                callback: function () {
                    var promise = PositionService.disable({ids: $scope.beans.id}, function () {
                        AlertFactory.success('禁用成功!');
                        $scope.beans = null;
                        initTree();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };
        // 启用
        $scope.enable = function () {
            ModalFactory.confirm({
                scope: $scope,
                content: '启用该岗位时，会同时启用该岗位的所有上级岗位，请确认!',
                callback: function () {
                    var promise = PositionService.enable({ids: $scope.beans.id}, function () {
                        AlertFactory.success('启用成功!');
                        $scope.beans = null;
                        initTree();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function () {
            $scope.beans = {
                minEmp: 0,
                maxEmp: 999,
                sequenceNo: ($scope.beans && $scope.beans.sequenceNo || 0) + 1,
                parentId: ($scope.beans && $scope.beans.id),
                parentName: ($scope.beans && $scope.beans.id && $scope.beans.name)
            };
        };

        $scope.save = function (addNew) {
            var promise = PositionService.save($scope.beans, function (data) {
                AlertFactory.success('保存成功!');
                if (addNew) {
                    $scope.beans.name = null;
                    $scope.beans.code = null;
                    $scope.beans.sequenceNo = ($scope.beans.sequenceNo || 0) + 1
                    $scope.beans.description = null;
                } else {
                    $scope.beans = null;
                    initTree();
                }
            });
            CommonUtils.loading(promise);
        };

        $scope.update = function () {
            var promise = PositionService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
            });
            CommonUtils.loading(promise);
        };

        $scope.cancel = function () {
            $scope.beans = null;
        };


        initTree();
    });
})(window, angular, jQuery);