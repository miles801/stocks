(function (window, angular, $) {

    var app = angular.module('base.position.resource', [
        'base.position',
        'base.resource',
        'eccrm.angular',
        'eccrm.angular.ztree',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, PositionService, PositionTree, ResourceService) {

        // 用于保存当前被点击的组织机构的数据
        $scope.position = {};

        // 初始化树
        var initTree = function () {
            PositionTree.validTree({
                scope: $scope, id: 'tree', onClick: function (o) {
                    $scope.position = o;
                    $scope.query();
                }
            });
        };

        function getZTreeSettings() {
            return {
                view: {showIcon: false},
                check: {enable: true},
                data: {
                    key: {url: '_url'/* 防止url跳转*/},
                    simpleData: {pIdKey: 'parentId', enable: true}
                }
            };
        }

        var menuTree = null;
        var elementTree = null;
        // 初始化菜单资源
        var initMenuTree = function () {
            ResourceService.queryValidMenu(function (data) {
                angular.forEach(data.data, function (o) {
                    o.icon = null;
                });
                menuTree = $.fn.zTree.init($('#menuTree'), getZTreeSettings(), data.data);
            });
        };
        $scope.checkAllMenu = function () {
            menuTree.checkAllNodes(true);
        };

        $scope.clearAllMenu = function () {
            menuTree.checkAllNodes(false);
        };

        // 保存菜单权限
        $scope.saveMenu = function () {
            var nodes = menuTree.getCheckedNodes(true);
            var ids = [];
            angular.forEach(nodes, function (o) {
                ids.push(o.id);
            });
            var promise = ResourceService.grantMenu({positionId: $scope.position.id, resourceIds: ids}, function () {
                AlertFactory.success('授权成功!重新登录后生效!');
            });
            CommonUtils.loading(promise);
        };
        // 初始化数据资源
        var dataTree = null;
        var initDataTree = function () {
            ResourceService.queryValidData(function (data) {
                dataTree = $.fn.zTree.init($('#dataTree'), getZTreeSettings(), data.data);
            });
        };
        // 初始化操作资源
        var elementTree = null;
        var initElementTree = function () {
            ResourceService.queryValidElement(function (data) {
                elementTree = $.fn.zTree.init($('#elementTree'), getZTreeSettings(), data.data);
            });
        };
        $scope.checkAllElement = function () {
            elementTree.checkAllNodes(true);
        };

        $scope.clearAllElement = function () {
            elementTree.checkAllNodes(false);
        };

        // 保存菜单权限
        $scope.saveElement = function () {
            var nodes = elementTree.getCheckedNodes(true);
            var ids = [];
            angular.forEach(nodes, function (o) {
                ids.push(o.id);
            });
            var promise = ResourceService.grantElement({positionId: $scope.position.id, resourceIds: ids}, function () {
                AlertFactory.success('授权成功!重新登录后生效!');
            });
            CommonUtils.loading(promise);
        };

        $scope.query = function () {
            // 查询指定岗位的资源ID
            $scope.clearAllMenu();
            var promise = ResourceService.queryByPosition({positionId: $scope.position.id}, function (data) {
                // 遍历菜单树进行回显
                var ids = data.data || [];
                // 菜单树
                menuTree.getNodesByFilter(function (o) {
                    if ($.inArray(o.id, ids) != -1) {
                        o.checked = true;
                    }
                }, false);
                menuTree.refresh();
                // 资源树
                elementTree.getNodesByFilter(function (o) {
                    o.checked = $.inArray(o.id, ids) != -1;
                }, false);
                elementTree.refresh();
            });
            CommonUtils.loading(promise);
        };

        initTree();

        initMenuTree();
        // fixme 数据权限还未完成
        // initDataTree();
        initElementTree();

    });
})(window, angular, jQuery);