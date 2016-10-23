/**
 * Created by miles on 13-11-25.
 */
(function (angular, $) {
    var app = angular.module('base.parameter.item.list', [
        'base.param',
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.directive.ztree',
        'base.parameter.modal'
    ]);
    app.controller('ParameterItemListController', function ($scope, Debounce, ModalFactory, SysParamItem, SysParamType, AlertFactory, SysParamItemModal, CommonUtils) {
        $scope.condition = {};
        var ztreeObj;// ztree树
        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {
                    start: $scope.pager.start || 0,
                    limit: $scope.pager.limit || 10
                }, $scope.condition);
                return CommonUtils.promise(function (defer) {
                    var promise = SysParamItem.query(param, function (data) {
                        $scope.parameters = data.data;
                        defer.resolve(data.data);
                    });
                    CommonUtils.loading(promise);
                });
            }
        };

        //查询数据
        $scope.queryItems = function () {
            CommonUtils.delay($scope.pager.query, 0);
        };

        // 启用
        $scope.enable = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '启用该参数，请确认!'
            }, function () {
                if (!id) {
                    var ids = [];
                    angular.forEach($scope.items || [], function (o) {
                        ids.push(o.id);
                    });
                    id = ids.join(',');
                    $scope.items = []; // 清空已选择选项，防止多次重复选择
                }
                var promise = SysParamItem.enable({ids: id}, function () {
                    AlertFactory.success('启用成功!');
                    initTree();
                });
                CommonUtils.loading(promise);
            });
        };

        // 禁用
        $scope.disable = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '参数一旦被禁用，将无法使用，请确认!'
            }, function () {
                if (!id) {
                    var ids = [];
                    angular.forEach($scope.items || [], function (o) {
                        ids.push(o.id);
                    });
                    id = ids.join(',');
                    $scope.items = []; // 清空已选择选项，防止多次重复选择
                }
                var promise = SysParamItem.disable({ids: id}, function () {
                    AlertFactory.success('禁用成功!');
                    initTree();
                });
                CommonUtils.loading(promise);
            });
        };

        // 删除
        $scope.remove = function (id) {
            ModalFactory.confirm({
                scope: $scope,
                content: '<b class="text-danger">警告!</b>参数一旦被删除，将无法恢复，请慎重确认!'
            }, function () {
                if (!id) {
                    var ids = [];
                    angular.forEach($scope.items || [], function (o) {
                        ids.push(o.id);
                    });
                    id = ids.join(',');
                    $scope.items = []; // 清空已选择选项，防止多次重复选择
                }
                var promise = SysParamItem.deleteByIds({ids: id}, function () {
                    AlertFactory.success('删除成功!');
                    initTree();
                });
                CommonUtils.loading(promise);
            });
        };

        //初始化ztree树（必须在文档加载后执行）
        var setting = {
            view: {showIcon: false},
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "parentId"
                }
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    if (treeNode.isParent === true && treeNode.open === false) {
                        ztreeObj.expandNode(treeNode, true, true, true);
                    }
                    // 防止重复点击
                    if (!treeNode.code || $scope.current === treeNode) return;
                    $scope.$apply(function () {
                        $scope.condition.type = treeNode.code;
                        $scope.current = treeNode;
                        $scope.queryItems();
                    });
                }
            }
        };
        var initTree = $scope.initTree = function () {
            var promise = SysParamType.queryUsing(function (data) {
                data = data.data || [];
                var tree = [
                    {name: '系统参数', open: true}
                ];
                if (data && data.length > 0) {
                    tree = data;
                    data[0].open = true;
                    $scope.condition.type = data[0].code;
                    $scope.queryItems();
                }
                ztreeObj = $.fn.zTree.init($("#treeDemo"), setting, tree);
            });
            CommonUtils.loading(promise);
        };
        initTree();

        //新增
        $scope.add = function () {
            if (!$scope.condition.type) {
                AlertFactory.error('请选择参数类型!');
                return false;
            }
            SysParamItemModal.add({scope: $scope, parameterType: $scope.current}, $scope.pager.load);
        };

        //更新
        $scope.modify = function (id) {
            SysParamItemModal.modify({scope: $scope, id: id}, $scope.pager.load);
        };

        //查看
        $scope.view = function (id) {
            SysParamItemModal.view({scope: $scope, id: id});
        };

    })
})(angular, jQuery);