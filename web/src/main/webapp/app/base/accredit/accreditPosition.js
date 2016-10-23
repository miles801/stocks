/**
 * Created by miles on 13-11-25.
 */
(function (angular, $) {
    var app = angular.module('eccrm.accredit.position', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.angular.ztree',
        'eccrm.position.position',
        'com.michael.base.menu',
        'eccrm.accredit.menu',
        'com.michael.base.resource',
        'base.org'
    ]);

    function getZTreeSettings() {
        return {
            view: {
                showIcon: false
            },
            check: {
                enable: true
            },
            data: {
                key: {
                    url: '_url'// 防止url跳转
                },
                simpleData: {
                    pIdKey: 'parentId',
                    enable: true
                }
            }
        };
    }

    // 配置路由信息
    var contextPath = $('#contextPath').val();
    app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
        // 菜单授权
            .when('/menu', {
                templateUrl: contextPath + 'app/base/accredit/template/accredit_menu.html',
                controller: 'AccreditMenuCtrl'
            })
            // 操作点授权
            .when('/operate', {
                templateUrl: contextPath + 'app/base/accredit/template/accredit_operate.html',
                controller: 'AccreditOperateCtrl'
            })
            // 数据级授权
            .when('/data', {
                templateUrl: contextPath + 'app/base/accredit/template/accredit_data.html',
                controller: 'AccreditDataCtrl'
            })
            .otherwise({redirectTo: '/menu'});
    }]);

    var currentNode; // 当前节点（点击左侧树时初始化该值）
    // 入口
    app.controller('AccreditPositionCtrl', function ($scope, CommonUtils, PositionTree, PositionService, $location) {
        // 初始化路由
        $scope.routeOptions = [
            {url: 'menu', name: '菜单授权', active: true},
            {url: 'operate', name: '操作授权'},
            {url: 'data', name: '数据授权'}
        ];

        // 加载左侧岗树
        PositionTree.tree($scope, 'treeDemo', function (node) {
            $scope.active = 0;
            $location.path('/menu');
            // 加上一个随机数，用于强制刷新
            $location.search("_t", CommonUtils.randomID(5));
            $location.replace();
            currentNode = node;
        });
    });

    // 菜单授权
    app.controller('AccreditMenuCtrl', function ($scope, MenuService, CommonUtils, AccreditMenu, AlertFactory) {
        var menus, accreditMenus;

        var zTreeSetting = getZTreeSettings();
        $scope.selectAnyNode = !!currentNode;
        if (!$scope.selectAnyNode) {
            return false;
        }
        var zTreeObj;
        // 初始化树
        var initTree = function () {
            // 显示树
            CommonUtils.deleteAttr(menus, "icon");
            zTreeObj = $.fn.zTree.init($('#accreditMenu').find('.ztree'), zTreeSetting, menus);
            // 回显已授权的数据
            var nodes = zTreeObj.getNodesByFilter(function (node) {
                return $.inArray(node.id, accreditMenus) != -1;
            });
            angular.forEach(nodes, function (node) {
                zTreeObj.checkNode(node, true, false);
            });
        };
        var loadFinish = function () {
            if (menus && accreditMenus) {
                initTree();
            }
        };
        // 加载所有可以被授权的菜单
        var loadingMenu = MenuService.queryValid();
        CommonUtils.loading(loadingMenu, '加载菜单...', function (data) {
            menus = data.data || [];
            loadFinish();
        });

        // 加载所有被授权的权限的ID集合
        var loadingAccreditMenu = AccreditMenu.queryIdsByDept({deptId: currentNode.id});
        CommonUtils.loading(loadingAccreditMenu, '加载权限菜单...', function (data) {
            accreditMenus = data.data || [];
            loadFinish();
        });

        // 全选
        $scope.selectAll = function () {
            zTreeObj.checkAllNodes(true);
        };

        // 清空
        $scope.clearAll = function () {
            zTreeObj.checkAllNodes(false);
        };

        // 反选
        $scope.selectReverse = function () {
            var nodes = zTreeObj.getCheckedNodes(false);
            $scope.clearAll();
            angular.forEach(nodes, function (node) {
                zTreeObj.checkNode(node, true, false);
            });
        };
        // 保存最新授权
        $scope.save = function () {
            var menuIds = [];
            angular.forEach(zTreeObj.getCheckedNodes(true) || [], function (v) {
                menuIds.push(v.id);
            });
            var promise = AccreditMenu.save({deptId: currentNode.id, menuIds: menuIds.join(",")});
            AlertFactory.handle($scope, promise, function (data) {
                AlertFactory.success($scope, '授权成功');
            });
        }
    });

    // 操作点授权
    app.controller('AccreditOperateCtrl', function ($scope, ResourceService, ParameterLoader, CommonUtils, AccreditFunc, AlertFactory) {
        if (!currentNode) return;
        var modules = {};
        var chain = [];
        // 加载所有的模块
        chain.push(function () {
            var context = this;
            ParameterLoader.loadSysParam('SP_MODULE', function (data) {
                // 将所有的查询出来的模块初始化到容器中
                angular.forEach(data || [], function (foo) {
                    if (foo.value === undefined) return;
                    modules[foo.value] = foo;
                });
                context.resolve();
            }, '加载模块信息...');
        });

        // 加载所有已授权的
        chain.push(function () {
            var context = this;
            var promise = AccreditFunc.queryResourceCodeByDept({deptId: currentNode.id}, function (data) {
                context.resolve(data.data || []);
            });
            CommonUtils.loading(promise, '加载已授权操作...');
        });

        // 加载所有的界面元素
        chain.push(function (accreditResourceCodes) {
            var context = this;
            var promise = ResourceService.queryAllValidElement(function (data) {
                if (!data.success) {
                    context.reject(data.error || data.fail || data.message);
                    return;
                }
                // 删除缓存的data数据
                for (var tmp in modules) {
                    if (modules.hasOwnProperty(tmp)) {
                        delete modules[tmp].data;
                    }
                }
                // 将查询的所有操作添加到模块中
                angular.forEach(data.data || [], function (foo) {
                    var moduleName = foo.module;
                    if (!moduleName) return;
                    if (modules[moduleName] === undefined) return;
                    // 回显已授权操作
                    if ($.inArray(foo.code, accreditResourceCodes) > -1) {
                        foo.checked = true;
                    }
                    modules[moduleName].data = modules[moduleName].data || [];
                    modules[moduleName].data.push(foo);
                });
                context.resolve();
            });
            CommonUtils.loading(promise, '加载配置项...');
        });


        // 执行函数链
        CommonUtils.chain(chain, function () {
            $scope.modules = modules;
        });


        var setInvalid = function () {
            $scope.form.$setValidity('handling', false);
        };
        var setValid = function () {
            $scope.form.$setValidity('handling', true);
        };
        // 保存/更新失败后的回调
        var handleFailCallback = function (data) {
            if (data.success) {
                $scope.back();
                return;
            }
            setValid();
        };
        // 保存
        $scope.save = function () {
            // 禁用操作按钮
            setInvalid();
            // 获取搜索被授权的资源id
            var checkedResource = [];
            angular.forEach(modules || [], function (o) {
                var arr = o.data || [];
                angular.forEach(arr, function (foo) {
                    foo.checked === true && checkedResource.push({resourceId: foo.id, resourceCode: foo.code});
                });
            });

            // 执行保存操作
            var promise = AccreditFunc.accreditToDept({
                deptId: currentNode.id,
                resources: checkedResource
            }, function (data) {
                if (data.success) {
                    AlertFactory.success(null, '授权成功!');
                } else {
                    AlertFactory.error($scope, data.fail || data.error);
                }
                // 恢复操作按钮
                setValid();
            }, function (data) {
                AlertFactory.error($scope, data.fail || data.error);
            });
            CommonUtils.loading(promise, '正在保存授权...');
        };

        // 清空指定模块下的节点
        $scope.clearAll = function (data) {
            angular.forEach(data || [], function (foo) {
                foo.checked = false;
            });
        };

        // 选择指定模块下的所有节点
        $scope.checkAll = function (data) {
            angular.forEach(data || [], function (foo) {
                foo.checked = true;
            });
        }
    });

    // 数据级授权
    var cachedResources;// 缓存的资源
    var cachedParams;// 缓存的系统
    app.controller('AccreditDataCtrl', function ($scope, ParameterLoader, AccreditData, CommonUtils, ResourceService, AlertFactory, OrgService, ZtreeModal) {
        var resources;// 可以被授权的数据资源
        var accreditResources;// 已经被授权的数据资源
        var currentResource;// 当前被选中的资源

        $scope.pickedOrgs = [];

        // 数据资源树
        var dataResourceTreeSetting = {
            view: {
                showIcon: false
            },
            data: {
                simpleData: {
                    pIdKey: 'parentId',
                    enable: true
                }
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                    // 设置当前被选中的资源
                    // 如果点击的不是数据类型，则不响应
                    $scope.$apply(function () {
                        $scope.isData = treeNode.isData;
                        currentResource = treeNode;
                        delete $scope.checked;
                        delete $scope.hasMoreOrg;
                        delete $scope.hasMoreParam;
                        // 清空已经被选中的
                        angular.forEach($scope.params || [], function (v) {
                            v.checked = false;
                        });

                        // 回显数据
                        // 根据当前岗位id查询已授权的数据权限
                        var promise = AccreditData.queryAccreditResource({
                            deptId: currentNode.id,
                            resourceCode: currentResource.code
                        }, function (data) {
                            var result = data.data || {};
                            $scope.checked = result.accreditType || '3';
                            var grantParams = result.otherParam;
                            // 回显已授权系统
                            if (grantParams) {
                                $scope.hasMoreParam = true;
                                angular.forEach(grantParams.split(',') || [], function (v) {
                                    angular.forEach($scope.params || [], function (p) {
                                        if (v == p.value) {
                                            p.checked = true;
                                        }
                                    });
                                });
                            }
                            // 回显已授权机构
                            if (result.otherOrgIds) {
                                $scope.hasMoreOrg = true;
                                var ids = result.otherOrgIds.split(',') || [];
                                var names = result.otherOrgNames.split(',') || [];
                                var orgs = [];
                                angular.forEach(ids, function (v, i) {
                                    orgs.push({id: v, name: names[i]});
                                });
                                $scope.pickedOrgs = orgs;
                            }
                        });
                        CommonUtils.loading(promise, '加载已授权数据...');
                    });
                }
            }
        };
        $scope.selectAnyNode = !!currentNode;
        if (!$scope.selectAnyNode) {
            return false;
        }

        // 选择机构

        $scope.pickOrg = function () {
            ZtreeModal.doubleTree({
                title: '选择机构',
                initLeft: function () {
                    return CommonUtils.promise(function (defer) {
                        CommonUtils.loading(defer.promise, '加载组织机构树...');
                        OrgService.queryChildren(function (data) {
                            defer.resolve(data.data || []);
                        });
                    });
                },
                expand: function (event, treeId, treeNode) {
                    var obj = this.getZTreeObj(treeId);
                    if (!(treeNode.children && treeNode.children.length > 0)) {
                        var promise = OrgService.queryChildren({id: treeNode.id});
                        CommonUtils.loading(promise, '加载机构数据...', function (data) {
                            treeNode.children = data.data || [];
                            obj.refresh();
                        });

                    }
                },
                callback: function (data) {
                }
            });
        };

        var dataResourceTree;
        // 初始化数据资源树
        var initDataResourceTree = function () {
            dataResourceTree = $.fn.zTree.init($('#accreditData').find('#resourceTree'), dataResourceTreeSetting, resources);
        };
        // 加载所有可以被授权的数据资源
        if (cachedResources != undefined) {
            resources = cachedResources.slice(0);
            initDataResourceTree();
        } else {
            var loadingValidDataResource = ResourceService.queryAllValidData();
            CommonUtils.loading(loadingValidDataResource, '加载数据资源...', function (data) {
                cachedResources = data.data || [];
                resources = cachedResources.slice(0);
                initDataResourceTree();
            });
        }

        // 加载所有的系统
        if (cachedParams != undefined) {
            $scope.params = cachedParams.slice(0);
        } else {
            ParameterLoader.loadBusinessParam("BP_YETAI", function (data) {
                cachedParams = data || [];
                $scope.params = cachedParams.slice(0);
            }, "加载系统...");
        }

        // 保存最新授权
        $scope.save = function () {
            var o = {
                deptId: currentNode.id,
                resourceId: currentResource.id,
                resourceCode: currentResource.code,
                accreditType: $scope.checked
            };
            // 额外授予的机构
            if ($scope.hasMoreOrg == true) {
                var ids = [];
                var names = [];
                angular.forEach($scope.pickedOrgs || [], function (v) {
                    ids.push(v.id);
                    names.push(v.name);
                });
                o.otherOrgIds = ids.join(',');
                o.otherOrgNames = names.join(',');
            }

            // 额外授予的系统
            if ($scope.hasMoreParam == true) {
                var checkedParams = [];
                angular.forEach($scope.params, function (v) {
                    v.checked == true ? checkedParams.push(v.value) : null;
                });
                o.otherParam = checkedParams.join(',');
            }
            var promise = AccreditData.accreditToDept(o);
            AlertFactory.handle($scope, promise, function (data) {
                AlertFactory.success($scope, '授权成功');
            });
        };


        // 初始化数据
        $scope.isData = false;

        // 初始化机构树

        var orgTree;
        var orgTreeSetting = {
            view: {showIcon: false},
            check: {enable: true},
            callback: {
                onExpand: function (event, treeId, treeNode) {
                    // 展示时动态加载数据
                    var obj = this.getZTreeObj(treeId);
                    if (!(treeNode.children && treeNode.children.length > 0)) {
                        var promise = OrgService.queryChildren({id: treeNode.id});
                        CommonUtils.loading(promise, '加载机构数据...', function (data) {
                            treeNode.children = data.data || [];
                            // 从新查询出的子节点中判断是否回显
                            angular.forEach($scope.pickedOrgs || [], function (v) {
                                angular.forEach(treeNode.children || [], function (d) {
                                    if (d.id == v.id) {
                                        d.checked = true;// 回显当前节点
                                        treeNode.checked = true;// 回显上级节点
                                    }
                                });
                            });
                            // 刷新树
                            obj.refresh();
                        });
                    } else {
                        // 从缓存的数据中判断是否回显
                        angular.forEach($scope.pickedOrgs || [], function (v) {
                            angular.forEach(treeNode.children || [], function (d) {
                                if (d.id == v.id) {
                                    d.checked = true;// 回显当前节点
                                    treeNode.checked = true;// 回显上级节点
                                }
                            });
                        });
                    }
                }
            }
        };
        $scope.removeOrg = function (index) {
            var o = $scope.pickedOrgs.splice(index, 1);
            // 从树中取消选中
            orgTree.checkNode(o[0], false);
        };
        $scope.pickOrg = function () {
            var checkedOrg = orgTree.getCheckedNodes(true);
            var items = [];
            angular.forEach(checkedOrg, function (item) {
                if (item.check_Child_State == -1 || item.check_Child_State == 2) {
                    items.push(item);
                }
            });
            $scope.pickedOrgs = items;
        };
        if (orgTree == undefined) {
            OrgService.queryChildren(function (data) {
                orgTree = $.fn.zTree.init($('#accreditData').find('#orgTree'), orgTreeSetting, data.data || []);
            });
        } else {
            orgTree.checkAllNodes(false);
        }

    });

})(angular, jQuery);