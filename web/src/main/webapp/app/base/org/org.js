/**
 * 组织机构
 */
(function (angular, $) {
    var app = angular.module("base.org", [
        'ngResource',
        'eccrm.angular'
    ]);

    app.service('OrgService', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('/base/org/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 根据id查询机构员工视图信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 禁用组织机构
            disabled: {method: 'POST', params: {method: 'disabled', ids: '@ids'}, isArray: false},

            // 启用组织机构
            enable: {method: 'POST', params: {method: 'enable', ids: '@ids'}, isArray: false},

            // 维护时用：查询所有的菜单，并组装成树
            // 返回：{data : [{id,name,children:[]}]}
            tree: {method: 'POST', params: {method: 'tree'}, isArray: false},

            //分页查询，返回{total:,data:[{},{}]}
            query: {method: 'POST', params: {method: 'query', limit: '@limit', start: '@start'}, isArray: false},

            // 添加员工
            addEmp: {method: 'POST', params: {method: 'emp-add', orgId: '@orgId', empIds: '@empIds'}, isArray: false},
            // 移除员工
            removeEmp: {
                method: 'POST',
                params: {method: 'emp-remove', orgId: '@orgId', empIds: '@empIds'},
                isArray: false
            },

            // 查询员工
            queryEmp: {
                method: 'POST', params: {
                    method: 'emp-query', orgId: '@orgId', limit: '@limit', start: '@start'
                },
                isArray: false
            },

            // 查询直接子节点
            // 可选参数：id(当前节点id，即查询该id下面的直接子节点）
            // 如果没有传递id参数，则默认查询根节点
            queryChildren: {method: 'GET', params: {method: 'children'}, isArray: false},


            //根据id字符串（使用逗号分隔多个值），删除对应的机构员工视图，成功则返回{success:true}
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    // 提供组织机构动态树的功能
    app.service('OrgTree', function (CommonUtils, OrgService) {
        return {
            // 获得动态组织机构树的配置（适用于ztree-single指令的配置）
            // Notice: 该方法只会返回状态为有效的数据
            // 参数：
            // onClick[function]：点击节点时需要触发的回调，该函数接收一个被选中的节点
            pick: function (onClick) {
                return {
                    data: function () {
                        return CommonUtils.promise(function (defer) {
                            var promise = OrgService.tree({deleted: false}); // 只查询有效的数据
                            CommonUtils.loading(promise, '加载机构树...', function (data) {
                                defer.resolve(data.data || []);
                            });
                        });
                    },
                    click: onClick
                }
            },


            // 用于在指定地方动态展示一个动态的组织机构树（一般用于维护界面）
            // 必须参数：
            //  scope:
            //  id：树要显示的地方
            // 可选参数：
            //  onClick：选中节点后要触发的函数，接收一个node对象
            tree: function (scope, id, onClick) {
                var setting = {
                    view: {
                        fontCss: function (treeId, node) {
                            if (node.deleted) {
                                return {'background-color': '#ddd', color: 'white'}
                            }
                            return {};
                        },
                        showIcon: false
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "parentId"
                        }
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode) {
                            var obj = this.getZTreeObj(treeId);
                            if (angular.isFunction(onClick)) {
                                scope.$apply(function () {
                                    onClick.call(obj, treeNode);
                                });
                            }
                        }
                    }
                };

                var promise = OrgService.tree(function (data) {
                    data = data.data || [];
                    var treeObj = $.fn.zTree.init($("#" + id), setting, data);
                    defer.resolve(treeObj);
                });
                CommonUtils.loading(promise, '加载机构树...');
                var defer = CommonUtils.defer();
                return defer.promise;
            },

            /**
             * 用于在指定地方动态展示一个动态的组织机构树（只查询有效的数据）
             * 必须参数：
             * scope
             * id：树要显示的地方
             * onClick：选中节点后要触发的函数，接收一个node对象
             * showEmpCounts:boolean，是否显示员工数量
             * @param options
             */
            validTree: function (options) {
                if (!options) {
                    alert('请提供配置对象!');
                    return;
                }
                if (!options.scope) {
                    alert('配置对象中缺少scope对象!');
                    return;
                }
                if (!options.id) {
                    alert('配置对象中缺少ID，用于指定树形要显示的地方!');
                    return;
                }

                var setting = {
                    view: {
                        showIcon: false
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "parentId"
                        }
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode) {
                            var obj = this.getZTreeObj(treeId);
                            if (angular.isFunction(options.onClick)) {
                                options.scope.$apply(function () {
                                    options.onClick.call(obj, treeNode);
                                });
                            }
                        }
                    }
                };

                var promise = OrgService.tree({deleted: false}, function (data) {
                    data = data.data || [];
                    var treeObj = $.fn.zTree.init($("#" + options.id), setting, data);
                    defer.resolve(treeObj);
                });
                CommonUtils.loading(promise, '加载机构树...');
                var defer = CommonUtils.defer();
                return defer.promise;
            }

        };
    });

})(angular, jQuery);
