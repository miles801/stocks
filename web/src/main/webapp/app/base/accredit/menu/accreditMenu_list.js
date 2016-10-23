/**
 * Created by miles on 13-11-25.
 */
(function (angular, $) {
    var app = angular.module('eccrm.accredit.menu.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.position.position',
        'com.michael.base.menu',
        'com.michael.base.menu.modal',
        'eccrm.accredit.menu'
    ]);
    app.controller('AccreditMenuCtrl', function ($scope, AlertFactory, AccreditMenu, CommonUtils, CompileTree, PositionService, MenuModal) {
        var selectedIds = '';
        var setting = {
            view: {showIcon: false},
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    // 防止重复点击 或者是类型
                    if ($scope.currentId == treeNode.id) return;
                    if (treeNode.isType) return;
                    // 查询权限菜单
                    $scope.$apply(function () {
                        $scope.currentId = treeNode.id;
                        $scope.query();
                    });
                    event.preventDefault();
                },
                onExpand: function (event, treeId, treeNode) {
                    var obj = this.getZTreeObj(treeId);
                    // 如果没有孩子节点，则重新加载
                    if (selectedIds.indexOf(treeNode.id) !== -1) {
                        return;
                    }
                    PositionService.queryAll({id: treeNode.id}, function (result) {
                        selectedIds += "|" + treeNode.id;
                        treeNode.children = (treeNode.children || []).concat(result.data.data || []);
                        obj.refresh();
                    });
                }
            }
        };

        // 加载左侧岗树
        CompileTree.init().then(function (positionTypes) {
            var data = positionTypes || [];
            angular.forEach(data, function (v) {
                v.isParent = true;
                v.isType = true;
            });
            $.fn.zTree.init($("#treeDemo"), setting, data);
        });

        $scope.query = function () {
            if (!$scope.currentId) {
                return;
            }
            var promise = AccreditMenu.queryByDept({deptId: $scope.currentId});
            AlertFactory.handle($scope, promise, function (result) {
                $scope.menus = result.data || [];
            });
        };

        $scope.grantMenu = function () {
            if (!$scope.currentId) {
                AlertFactory.error($scope, "请选择要授权的岗位!");
                return false;
            }
            MenuModal.dbTreePicker({title: '功能授权', min: 0, defaultChecked: getCurrentIds}, function (items) {
                var menuIds = [];
                angular.forEach(items || [], function (v) {
                    menuIds.push(v.id);
                });
                var promise = AccreditMenu.save({deptId: $scope.currentId, menuIds: menuIds.join(",")});
                AlertFactory.handle($scope, promise, function (data) {
                    AlertFactory.success($scope, '授权成功');
                    $scope.query();
                });
            });
        };
        // 获得当前岗位下的所有资源的id列表
        function getCurrentIds() {
            var ids = [];
            angular.forEach($scope.menus || [], function (v) {
                ids.push(v.id);
            });
            return ids;
        }

    })
})(angular, jQuery);