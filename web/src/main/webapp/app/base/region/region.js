/**
 * Created by miles on 13-11-25.
 */
(function (angular) {
    var app = angular.module("com.michael.base.region", [
        'ngResource',
        'eccrm.angular'
    ]);
    app.service('RegionService', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('base/region/:method'), {}, {
            save: {method: 'POST', params: {method: 'save'}, isArray: false},//保存
            update: {method: 'POST', params: {method: 'update'}, isArray: false},//更新

            // 设置区域负责人
            setMaster: {method: 'POST', params: {method: 'master-set'}, isArray: false},//更新

            // 清空区域负责人
            clearMaster: {method: 'POST', params: {method: 'master-clear', id: '@id'}, isArray: false},

            // 查询我负责的城市的所有区域
            mine: {method: 'GET', params: {method: 'mine'}, isArray: false},
            deleteByIds: {method: 'POST', params: {method: 'delete', ids: '@ids'}, isArray: false},
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},//根据id查询对象

            //查询所有的树，有url
            query: {
                method: 'POST',
                params: {method: 'query', start: '@start', limit: '@limit', orderBy: '@orderBy', reverse: '@reverse'},
                isArray: false
            },
            //查询所有的树，没有url
            tree: {method: 'POST', params: {method: 'tree'}, isArray: false}
        })
    });

    /**
     * 省份、城市、区县的选择器,每一个都将会返回一个未来对象，可以通过将一个回调函数传递给then()来获取结果值
     * 例如获得省份:
     * var province=RegionPicker.province();
     * province.then(function(data){
     *   //do your code here ,the data is the result from RegionPicker
     * });
     */
    app.service('RegionPicker', function (RegionService, $q, CommonUtils) {
        return {

            // 根据上级id选择下级
            // parentId：不为空时，查询子节点；为空时，查询根节点
            // 返回：promise对象
            picker: function (parentId) {

                var cfg = {valid: true};
                if (!parentId) {
                    cfg.root = true;
                } else {
                    cfg.parentId = parentId;
                }
                var defer = $q.defer();
                RegionService.tree(cfg, function (data) {
                    defer.resolve(data);
                });
                return defer.promise;
            },
            // 行政区域树
            // 返回加载完整行政区域树的ztree配置（采用异步加载的方式）
            // 该配置在single-ztree中使用
            // 回调函数将会获得一个参数，该参数为最终用户点击时获得的节点
            // 示例用法：
            //  <input ztree-single="regionTree" />
            //  $scope.regionTree=RegionPicker。getRegionTree(function(node){
            //       拿到你想要的节点了....
            // });
            getRegionTree: function (callback) {
                return {
                    maxHeight: 250,
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: 'parentId'
                    },
                    // 初识数据加载
                    data: function (ck) {
                        return CommonUtils.promise(function (defer) {
                            RegionService.tree(function (data) {
                                data = data.data || [];
                                defer.resolve(data);
                            });
                        });
                    },
                    click: callback
                };
            },

            // 动态显示行政区域树
            // 该方法返回一个ztreeObject的promise对象
            // 必须参数：
            //      scope:当前所属的scope
            //      id:树要初始化的地方
            // 可选参数：
            //      onClick:点击节点时要执行的函数，接收当前被点击的node对象
            tree: function (scope, id, onClick) {
                var setting = {
                    view: {showIcon: false},
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: 'parentId'
                        }
                    },
                    callback: {
                        onClick: function (event, treeId, node) {
                            if (angular.isFunction(onClick)) {
                                scope.$apply(function () {
                                    onClick.call(this, node);
                                });
                            }
                        }
                    }
                };
                var promise = RegionService.tree({root: true});
                var defer = CommonUtils.defer();
                CommonUtils.loading(promise, '加载行政区域树...', function (data) {
                    var treeObj = $.fn.zTree.init($("#" + id), setting, data.data || []);
                    defer.resolve(treeObj);
                });
                return defer.promise;
            },

        }
    });

    //行政区域相关的常量
    app.service('RegionConstant', function (RegionService, CommonUtils) {
        return {
            //行政区域类型
            type: [
                {code: 0, name: '国家'},
                {code: 1, name: '省'},
                {code: 2, name: '市'},
                {code: 3, name: '区/县'},
                {code: 4, name: '乡镇'},
                {code: 5, name: '街道'}
            ],
            //单选行政区域时的树的配置
            ztreeOptions: {
                maxHeight: 250,
                data: function (callback) {
                    RegionService.tree({root: true}, callback);
                },
                async: function (node, callback) {
                    if (!node || !node.id) return;
                    RegionService.tree({parentId: node.id}, callback);
                }
            },
            //新建时行政区域的默认值
            defaults: {
                type: 2,
                sequenceNo: 1
            }
        }
    });

    //行政区域类型转换器
    app.filter('regionType', function (RegionConstant) {
        return function (value) {
            if (!value && value !== 0) return '';
            var i = 0;
            for (; i < RegionConstant.type.length; i++) {
                if (value == RegionConstant.type[i].code)return RegionConstant.type[i].name;
            }
            return '不合法的类型值[' + value + ']!';
        }
    });

})(angular);
