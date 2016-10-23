/**
 * Created by miles on 13-11-25.
 */
(function (angular, $) {
    var app = angular.module('com.michael.base.region.list', [
        'com.michael.base.region',
        'com.michael.base.region.modal'
    ]);
    app.controller('RegionListController', function ($scope, RegionService, RegionModal, ModalFactory, AlertFactory, CommonUtils) {
        $scope.condition = {};
        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {
                    start: $scope.pager.start || 0,
                    limit: $scope.pager.limit || 10
                }, $scope.condition);
                return CommonUtils.promise(function (defer) {
                    var promise = RegionService.query(param, function (data) {
                        data = data.data || {total: 0, data: []};
                        if ($scope.current && $scope.current.id) {
                            data.data = data.data || [];
                            data.data.unshift($scope.current);
                        }
                        $scope.regions = data;
                        defer.resolve(data);
                    });
                    CommonUtils.loading(promise, '加载中...');
                });
            }
        };

        // 新增
        $scope.add = function () {
            RegionModal.add({scope: $scope}, initTree);
        };

        // 修改
        $scope.modify = function (id) {
            RegionModal.modify({scope: $scope, id: id}, $scope.query);
        };

        // 查看
        $scope.view = function (id) {
            RegionModal.view({scope: $scope, id: id});
        };

        // 删除
        $scope.remove = function (id) {
            if (!id) {
                var items = [];
                angular.forEach($scope.items, function (v) {
                    items.push(v.id);
                });
                if (!items || items.length < 1) {
                    AlertFactory.error($scope, '请选择至少一条数据后再进行删除/注销操作!', '错误操作!');
                    return;
                }
                id = items.join(',');
            }
            if (!id) {
                AlertFactory.warning('请选择要删除的数据!');
                return;
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '<span class="text-danger">数据一旦删除后，不可恢复，请确认!</span>',
                callback: function () {
                    var result = RegionService.deleteByIds({ids: id});
                    CommonUtils.loading(result, '删除中...', function () {
                        AlertFactory.success('删除成功!');
                        initTree();
                    });
                }
            });
        };


        //查询数据
        $scope.query = function () {
            $scope.pager.query();
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
                    $scope.condition.parentId = treeNode.id;
                    $scope.current = treeNode;
                    $scope.query();
                }
            }
        };
        var initTree = function () {
            RegionService.tree({root: true}, function (data) {
                data = data.data || [];
                if (!data || data.length == 0) {
                    data = [
                        {name: '行政区域'}
                    ];
                }
                angular.forEach(data, function (o) {
                    o.expand = true;
                });
                $.fn.zTree.init($("#treeDemo"), setting, data);
                $scope.query();
            })
        };
        initTree();
    })
})(angular, jQuery);