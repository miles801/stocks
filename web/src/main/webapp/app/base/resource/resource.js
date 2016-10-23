/**
 * 系统资源
 */
(function (angular) {
    var app = angular.module("base.resource", [
        'ngResource',
        'base.param',
        'eccrm.angular'
    ]);
    app.service('ResourceService', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL("base/resource/:method"), {}, {
            save: {method: 'POST', params: {method: 'save', attachmentIds: '@attachmentIds'}, isArray: false},//保存

            update: {method: 'POST', params: {method: 'update', attachmentIds: '@attachmentIds'}, isArray: false},//更新

            // 根据ID查询对象的信息
            // 返回{data:{}}
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},//根据id查询对象

            // 批量禁用
            disable: {method: 'POST', params: {method: 'disable', ids: '@ids'}, isArray: false},

            // 批量启用
            enable: {method: 'POST', params: {method: 'enable', ids: '@ids'}, isArray: false},

            // 高级查询对象，返回列表数据
            query: {method: 'POST', params: {method: 'query'}, isArray: false},

            // 查询所有有效的菜单
            queryValidMenu: {method: 'GET', params: {method: 'menu-valid'}, isArray: false},

            // 查询所有的孩子节点
            children: {method: 'GET', params: {method: 'children', id: '@id'}, isArray: false},

            // 查询有效的孩子节点
            validChildren: {method: 'GET', params: {method: 'children-valid', id: '@id'}, isArray: false},

            // 查询所有有效的【页面元素】资源
            // 返回{data:[]}
            queryValidElement: {method: 'GET', params: {method: 'element-valid'}, isArray: false},

            // 查询所有有效的【数据】资源
            // 返回{data:[]}
            queryValidData: {method: 'GET', params: {method: 'data-valid'}, isArray: false},


            // 查询指定岗位的所有资源的ID
            queryByPosition: {method: 'GET', params: {method: 'position', positionId: '@positionId'}, isArray: false},

            // 菜单授权，需要positionId和resourceIds两个属性
            grantMenu: {method: 'POST', params: {method: 'grantMenu'}, isArray: false},
            // 资源授权，需要positionId和resourceIds两个属性
            grantElement: {method: 'POST', params: {method: 'grantElement'}, isArray: false},
            // 数据授权，需要positionId和resourceIds两个属性
            grantData: {method: 'POST', params: {method: 'grantData'}, isArray: false}

        })
    });

    app.service('ResourceTree', function (CommonUtils, ResourceService) {
        return {
            /**
             * 选择菜单资源
             * @param onClick
             */
            pickMenu: function (onClick) {
                return {
                    data: function () {
                        return CommonUtils.promise(function (defer) {
                            var promise = ResourceService.queryValidMenu(function (data) {
                                defer.resolve(data.data || []);
                            });
                            CommonUtils.loading(promise);
                        });
                    },
                    click: onClick
                }
            },
            pickElement: function (onClick) {
                return {
                    data: function () {
                        return CommonUtils.promise(function (defer) {
                            var promise = ResourceService.queryValidElement(function (data) {
                                defer.resolve(data.data || []);
                            });
                            CommonUtils.loading(promise);
                        });
                    },
                    click: onClick
                }
            },
            pickData: function (onClick) {
                return {
                    data: function () {
                        return CommonUtils.promise(function (defer) {
                            var promise = ResourceService.queryValidData(function (data) {
                                defer.resolve(data.data || []);
                            });
                            CommonUtils.loading(promise);
                        });
                    },
                    click: onClick
                }
            },
            menuTree: function (scope, id, onClick, type) {
                var setting = {
                    view: {showIcon: false},
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "parentId"
                        },
                        key: {
                            url: '_url'
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

                var promise = ResourceService.query({type: type || 'MENU'}, function (data) {
                    data = data.data || [];
                    angular.forEach(data, function (o) {
                        o.icon = null;
                    });
                    var treeObj = $.fn.zTree.init($("#" + id), setting, data);
                    defer.resolve(treeObj);
                });
                CommonUtils.loading(promise);
                var defer = CommonUtils.defer();
                return defer.promise;
            },
            validMenuTree: function (scope, id, onClick) {
                var setting = {
                    view: {showIcon: false},
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "parentId"
                        },
                        key: {
                            url: '_url'
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

                var promise = ResourceService.query({type: 'MENU', deleted: false}, function (data) {
                    data = data.data || [];
                    angular.forEach(data, function (o) {
                        o.icon = null;
                    });
                    var treeObj = $.fn.zTree.init($("#" + id), setting, data);
                    defer.resolve(treeObj);
                });
                CommonUtils.loading(promise);
                var defer = CommonUtils.defer();
                return defer.promise;
            }
        }
    });

})(angular);
