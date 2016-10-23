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
    app.controller('ParameterItemListController', function ($scope, Debounce, ModalFactory, BusinessParamItem, BusinessParamType, AlertFactory, BusinessParamItemModal, $q, CommonUtils) {
        $scope.condition = {};
        var ztreeObj;
        $scope.pager = {
            fetch: function (callback) {
                var param = angular.extend({}, {
                    start: $scope.pager.start || 0,
                    limit: $scope.pager.limit || 10
                }, $scope.condition);
                var defer = $q.defer();
                var promise = BusinessParamItem.query(param, function (data) {
                    $scope.parameters = data.data;
                    defer.resolve(data.data);
                });
                CommonUtils.loading(promise);
                return defer.promise;
            }
        };

        //查询数据
        $scope.queryItems = function () {
            Debounce.delay($scope.pager.query, 0);
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
                var promise = BusinessParamItem.enable({ids: id}, function () {
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
                var promise = BusinessParamItem.disable({ids: id}, function () {
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
                var promise = BusinessParamItem.deleteByIds({ids: id}, function () {
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
                simpleData: {enable: true, idKey: "id", pIdKey: "parentId"}
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    if (treeNode === $scope.current) return;
                    if (treeNode.isParent && treeNode.open === false) {
                        ztreeObj.expandNode(treeNode, true, true, true);
                    }
                    if (!treeNode.code) return;
                    $scope.$apply(function () {
                        $scope.condition.type = treeNode.code;
                        $scope.current = treeNode;
                        $scope.queryItems();
                    });
                }
            }
        };
        var initTree = $scope.initTree = function () {
            var promise = BusinessParamType.queryOther(function (data) {
                data = data.data || [];
                var tree = [
                    {name: '业务参数', open: true}
                ];
                if (data && data.length > 0) {
                    $scope.condition.type = data[0].code;
                    tree = data;
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
            BusinessParamItemModal.add({scope: $scope, parameterType: $scope.current}, $scope.pager.load);
        };

        //更新
        $scope.modify = function (id) {
            BusinessParamItemModal.modify({scope: $scope, id: id}, $scope.pager.load);
        };

        //查看
        $scope.view = function (id) {
            BusinessParamItemModal.view({scope: $scope, id: id});
        };

    })
})(angular, jQuery);