/**
 * Created by miles on 13-11-25.
 */
(function (angular) {
    var app = angular.module("base.parameter.type", [
        'ngRoute',
        'base.param',
        'base.parameter.modal',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    var contextPath = $('#contextPath').val();
    app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/system', {
                templateUrl: contextPath + 'app/base/parameter/template/tab-parameterType-system.html',
                controller: 'SystemTypeCtrl'
            })
            .when('/business', {
                templateUrl: contextPath + 'app/base/parameter/template/tab-parameterType-business.html',
                controller: 'BusinessTypeCtrl'
            })
            .otherwise({redirectTo: '/system'});
    }]);

    app.controller('ParameterTypeCtrl', function ($scope) {
        $scope.active = 0;
        $scope.routeOptions = [
            {url: 'system', name: '基础参数类型', active: true},
            {url: 'business', name: '业务参数类型'}
        ];
    });
    var controller = function ($scope, AlertFactory, ModalFactory, ParameterType, ParameterTypeModal, CommonUtils) {
        var ztreeObj;// ztree对象
        $scope.query = function () {
            if (!$scope.id) return;
            var promise = ParameterType.children({id: $scope.id}, function (data) {
                $scope.parameters = data;
            });
            CommonUtils.loading(promise);
        };
        var initParams = function (treeNode) {
            $scope.id = treeNode.id;
            $scope.currentNodeId = treeNode.id;
            $scope.currentNodeName = treeNode.name;
        };
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
                    // 展开节点
                    if (treeNode.isParent === true && treeNode.open === false) {
                        ztreeObj.expandNode(treeNode, true, true, true);
                    }
                    // 防止重复点击
                    if (!treeNode.id || $scope.id == treeNode.id) return;

                    // 初始化参数
                    $scope.$apply(function () {
                        initParams(treeNode);
                    });
                    // 查询子节点
                    $scope.query();
                    event.preventDefault();
                }
            }
        };
        var initTree = function () {
            var result = ParameterType.tree();
            AlertFactory.handle($scope, result, function (data) {
                data = data.data || [];
                data.length > 0 ? initParams(data[0]) : null;
                $scope.query();//默认加载第一个菜单的数据到列表页面
                //初始化菜单
                ztreeObj = $.fn.zTree.init($("#treeDemo"), setting, data || []);
            });
        };

        $scope.add = function () {
            var options = {scope: $scope};
            if ($scope.id && $scope.currentNodeName) {
                options.parentId = $scope.currentNodeId;
                options.parentName = $scope.currentNodeName;
            }
            ParameterTypeModal.add(options, initTree);
        };
        $scope.view = function (id) {
            ParameterTypeModal.view({scope: $scope, id: id});
        };
        $scope.modify = function (id) {
            ParameterTypeModal.modify({scope: $scope, id: id}, initTree);
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
                var promise = ParameterType.enable({ids: id}, function () {
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
                var promise = ParameterType.disable({ids: id}, function () {
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
                var promise = ParameterType.deleteByIds({ids: id}, function () {
                    AlertFactory.success('删除成功!');
                    initTree();
                });
                CommonUtils.loading(promise);
            });
        };
        initTree();
    };
    app.controller('SystemTypeCtrl', ['$scope', 'AlertFactory', 'ModalFactory', 'SysParamType', 'SysParamTypeModal', 'CommonUtils', controller]);
    app.controller('BusinessTypeCtrl', ['$scope', 'AlertFactory', 'ModalFactory', 'BusinessParamType', 'BusinessParamTypeModal', 'CommonUtils', controller]);
})
(angular);
