/**
 * 基于ztree进行angularjs的封装
 * Created by Michael on 2016/10/2.
 */
// angular-ztree-directive.js
(function (angular, $) {
    var app = angular.module("eccrm.directive.ztree", [
        'eccrm.angular'
    ]);
    var defaults = {
        data: undefined,// （必须）树的数据：支持[]，函数，未来对象（必须返回[]）
        treeId: null,// 树的id
        multi: false,// （已废弃）是否多棵树：如果一个页面中会出现多棵树，则设置该属性为true，表示每次都加载
        zindex: 99,// 树的css层级：z-index的值
        speed: 500,// 展开或折叠的速度：单位为毫秒,
        click: angular.noop,// 点击事件
        disableClick: null,// 是否禁用某次点击操作，返回true表示禁用
        reload: false, // （已废弃）是否重新刷新树：当该值为true时，会重新刷新树
        async: angular.noop,// 异步函数,异步函数参数（选中的节点，数据返回后的回调函数）
        maxHeight: 300, // 树的最大高度：值为数字
        treeSetting: null,// 树的展示方式（一般使用默认值即可）
        position: 'fixed',// 定位方式，默认使用悬浮定位
        backgroundColor: '#f0f3f6'// 树的背景色
    };
    //单选树
    //<input ztree-single="ztreeOptions"/>
    app.directive('ztreeSingle', ['CommonUtils', '$q', function (CommonUtils, $q) {
        // ztree 的默认初始化参数
        var treeCount = 0;
        return {
            scope: {
                options: '=ztreeSingle'
            },
            link: function (scope, element, attrs, ctrl) {
                treeCount++;
                var scrollTop = 0;  // 有滚动条时的偏移量
                var ztreeObj, // ztree对象
                    tree, // tree
                    ztreeSetting, // ztree设置
                    treeContainer;  // ztree容器
                var promise = CommonUtils.parseToPromise(scope.options);
                var init = function (o) {
                    // 初始化参数
                    var options = angular.extend({}, defaults, o);
                    var treeId = options.treeId || CommonUtils.randomID(6);

                    // 给原对象新增一个reload方法，用于重新加载树
                    if (o.reload === undefined) {
                        scope.options.reload = function (p) {
                            element.unbind('click');    // 取消click绑定
                            if (ztreeObj) {
                                ztreeObj.destroy();         // 销毁树
                                ztreeObj = null;
                                treeContainer.remove();     // 销毁树容器
                                tree = null;
                            }
                            init(p || o);               // 重新加载树
                        };
                    }
                    // 只有当树没有被初始化时才会初始化，该方法保证树只被初始化一次
                    var initTree = function (data) {
                        if (!ztreeObj && tree) {
                            ztreeObj = $.fn.zTree.init(tree, ztreeSetting, data);
                            tree.parent().slideDown();
                        }
                    };
                    // 初始化树的数据
                    var loadTreeData = function (data) {
                        if (!data) return;
                        // 数据来源：结果数组
                        if (angular.isArray(data)) {
                            initTree(data);
                            return;
                        }

                        // 数据来源：未来对象
                        if (angular.isObject(data)) {
                            var promise = data.$promise || data.promise || $q.when(data);
                            promise.then(function (value) {
                                initTree(value);
                            });
                            return;
                        }
                        // 数据来源：函数
                        if (angular.isFunction(data)) {
                            var result = data.call(scope);
                            loadTreeData(result);
                            return;
                        }
                        alert('不支持的数据类型!');
                    };

                    // 事件绑定,点击时判断树是否被初始化，如果没有则初始化，否则直接显示
                    element.on('click', function (e) {
                        if (!ztreeObj) {
                            var ztreeDefaultSetting = {
                                view: {showIcon: false},
                                data: {
                                    simpleData: {
                                        enable: true,
                                        idKey: "id",
                                        pIdKey: "parentId"
                                    }
                                }
                            };
                            treeContainer = $('<span style="position: absolute;z-index: ' + options.zindex + ';top:0;left:0;padding-top: 28px;"></span>');
                            var treeDiv = $('<div style="display: none;border: 1px solid #afd0ee;padding-bottom: 5px;position: ' + options.position + ';background-color: ' + options.backgroundColor + ';" >' + '</div >');
                            tree = $('<ul class="ztree" style="max-height:' + options.maxHeight + 'px;overflow:auto;padding-right: 20px;" id="' + treeId + '"></ul >');
                            treeContainer.insertAfter(element);
                            treeContainer.append(treeDiv);
                            treeDiv.append(tree);
                            var speed = options.speed;
                            if (options.position == 'fixed' && scrollTop) {
                                treeDiv.css('top', (scrollTop - element.offset().top - 10) + 'px');
                            }

                            // 获得树的初始化参数
                            // 设置树的点击事件
                            ztreeSetting = angular.extend({}, ztreeDefaultSetting, options.treeSetting);
                            ztreeSetting.callback = ztreeSetting.callback || {};
                            ztreeSetting.callback.onClick = function (event, tid, treeNode) {
                                event.preventDefault();
                                // 是否禁用当前点击操作
                                if (angular.isFunction(options.disableClick) && options.disableClick(treeNode) == true) {
                                    return false;
                                }
                                tree.parent().slideUp(speed);
                                scope.$apply(function () {
                                    var clickCallBack = options.click;
                                    if (clickCallBack && angular.isFunction(clickCallBack)) {
                                        clickCallBack.call(scope, treeNode);
                                    }
                                });
                            };
                            // 如果配置了异步函数：则在点击展开时加载数据
                            if (options.async && angular.isFunction(options.async)) {
                                ztreeSetting.callback.onExpand = function (event, treeId, treeNode) {
                                    var obj = this.getZTreeObj(treeId);
                                    // 如果没有孩子节点，则加载数据
                                    if (!(treeNode.children && treeNode.children.length > 0)) {
                                        options.async.call(scope, treeNode, function (asyncData) {
                                            treeNode.children = asyncData || [];
                                            obj.refresh();
                                        });
                                    }
                                }
                            }
                            loadTreeData(options.data);
                        } else {
                            // 在fixed定位下重置树的位置
                            if (options.position == 'fixed') {
                                if (scrollTop > 0) {
                                    tree.parent().css('top', (scrollTop - element.offset().top - 10) + 'px');
                                } else {
                                    tree.parent().css('top', 'auto');
                                }
                            }
                            // 切换显示/隐藏树
                            tree.parent().slideToggle(speed);
                        }
                    });

                };
                promise.then(init);

                // 有滚动元素时，设置偏移量
                element.parents().find('div').bind('scroll', function (event) {
                    scrollTop = $(event.currentTarget).scrollTop();
                    event.preventDefault();
                });

                // 销毁时接触绑定
                scope.$on('destroy', function () {
                    element.parents().unbind('scroll');
                    element.unbind('click');
                });
            }
        }
    }]);

    //多选树
    app.directive('ztreeMulti', function () {
        return {
            scope: {
                options: '=ztreeMulti'
            },
            link: function (scope, element, attrs, ctrl) {
                var setting, tree;
                var options = scope.options = angular.extend({
                    checkedData: []
                }, defaults, scope.options);
                tree = $('' +
                    '<ul class="ztree" style="max-height:' + options.maxHeight + 'px;overflow:auto;padding-right: 20px;"></ul >'
                );
                var treeDiv = $('<div style="display: none;border: 1px solid #9fb5ac;padding-bottom: 5px;position: absolute;background-color: ' + options.backgroundColor + ';" >' + '</div >')
                    .append(tree);
                var treeSpan = $('<span style="position: fixed;z-index: ' + options.zindex + ';"></span>')
                    .append(treeDiv);
                treeSpan.insertAfter(element);
                var speed = options.speed;
                setting = {
                    treeId: new Date().getTime(),
                    view: {showIcon: false},
                    check: {
                        enable: true
                    },
                    data: {
                        simpleData: {enable: true}
                    },
                    callback: {}
                };
                if (options.async && angular.isFunction(options.async)) {
                    setting.callback.onExpand = function (event, treeId, treeNode) {
                        var obj = this.getZTreeObj(treeId);
                        if (!(treeNode.children && treeNode.children.length > 0)) {
                            options.async.call(scope, treeNode, function (asyncData) {
                                treeNode.children = asyncData || [];
                                obj.refresh();
                            });
                        }
                    }
                }
                var treeObj;
                var initTree = function (data) {
                    if (!data) return;
                    if (data && angular.isArray(data)) {
                        treeObj = $.fn.zTree.init(tree, setting, data);
                    } else if (angular.isFunction(data)) {
                        data.call(scope, function (result) {
                            treeObj = $.fn.zTree.init(tree, setting, result);
                        });
                    }
                };
                if (!options.multi) {
                    initTree(options.data);
                }

                element.on('click', function () {
                    //如果同一个页面包含多棵树，则每次点击都会加载一次数据
                    if (treeDiv.is(':hidden')) {
                        if (options.multi) {
                            initTree(options.data);
                        }

                        //回显数据
                        if (options.checkedData && options.checkedData.length > 0) {
                            var nodes = treeObj.getNodes();
                            console.dir(nodes);
                        }
                    }
                    if (treeDiv.is(':visible')) {
                        if (angular.isFunction(options.confirm)) {
                            options.confirm(treeObj.getCheckedNodes());
                        }
                    }
                    treeDiv.slideToggle(speed);
                });
            }
        }
    });

})(angular, jQuery);


// angular-ztree-modal.js
(function () {
    var app = angular.module('eccrm.ztree.modal', [
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    // ztree modal
    app.service('ZtreeModal', ['$modal', '$q', 'CommonUtils', 'ModalFactory', function ($modal, $q, CommonUtils, ModalFactory) {
        var defaults = {
            // 模态对话框的宽度
            modalWidth: '800px',
            // 至少要选择的个数
            min: 0,
            // 最大允许选择的个数
            max: undefined,
            // 模态对话框的标题
            title: '',
            // 模态对话框中内容的高度
            bodyHeight: '400px',

            // 初始化左侧树的函数（必须）
            // 接收一个数组
            initLeft: function () {
                return [
                    {id: '1', name: '测试数据'}
                ]
            },

            // 初始化右侧树的函数（可选）
            // 接收一个数组
            initRight: null,

            // 默认左侧树被选中的元素，目前仅仅支持id数组
            // 返回一个数组的函数，支持promise对象
            //
            defaultChecked: function () {
                return [];
            },

            // 是否显示全选按钮
            showSelectAllButton: true,

            // 当点击菜单时触发的事件
            // 接收一个函数，函数参数分别为:事件对象、treeId、当前节点对象、节点的选中状态
            click: null,
            // 当展开菜单时触发的事件
            // 接收一个函数，函数参数分别为:事件对象、treeId、当前节点对象
            expand: null,
            // 数据过滤器
            // 接收一个或多个函数，用于过滤左侧被选中的数据
            // 函数接收一个数组，包含所有被选中的数据，返回一个过滤后的数据
            dataFilter: [],

            // 获取右侧树的结果时，对结果数据进行过滤器
            // 该函数将会接收一个节点数据，然会true/false，true表示通过，false将会被过滤
            resultFilter: null,

            // ztree的配置
            ztree: {
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
            },

            // 点击确定后的事件
            // 将会返回右侧树中的所有数据（孩子节点将会以平级的方式返回）
            callback: function () {
            }
        };
        var common = function (options) {
            var modal = $modal({
                template: CommonUtils.contextPathURL('/static/ycrl/javascript/template/ztree-modal-double.tpl.html'),
                static: true
            });
            ModalFactory.afterShown(modal, options.afterShown);
            return modal.$scope;
        };

        var doLazy = function (data, callback) {
            if (angular.isArray(data)) {
                callback(data);
            } else if (angular.isObject(data)) {
                var promise = data.promise || data.$promise || $q.when(data);
                promise.then(function (promiseValue) {
                    callback(promiseValue);
                });
            } else {
                throw '错误的数据类型，仅支持[]或者promise对象!';
            }
        };
        return {
            doubleTree: function (options) {
                options = angular.extend({}, defaults, options);
                // 配置合法性验证
                if (!angular.isFunction(options.initLeft)) {
                    throw '缺少初始化左侧树的函数!';
                }

                options.ztree.callback = options.ztree.callback || {};
                // 初始化ztree配置
                if (typeof options.click === 'function') {
                    options.ztree.callback.onClick = options.click;
                }
                if (typeof options.expand === 'function') {
                    options.ztree.callback.onExpand = options.expand;
                }


                var leftTree;
                var rightTree;
                var $leftTree;
                var $rightTree;
                // 加载左侧树（所有的树）
                var initLeftTree = function (data) {
                    $leftTree = ($leftTree && $leftTree.length > 0) ? $leftTree : $(".modal-dialog #tree_left");
                    leftTree = $.fn.zTree.init($leftTree, options.ztree, data || []);
                };

                // 初始化右侧树
                var initRightTree = function (items) {
                    $rightTree = ($rightTree && $rightTree.length > 0) ? $rightTree : $(".modal-dialog #tree_right");
                    rightTree = $.fn.zTree.init($rightTree, options.ztree, items || []);
                    rightTree.checkAllNodes(false);
                };
                options.afterShown = function () {
                    // 初始化左侧树
                    doLazy(options.initLeft(), function (data) {
                        initLeftTree(data);

                        // 初始化回显左侧树
                        if (angular.isFunction(options.defaultChecked)) {
                            doLazy(options.defaultChecked(), function (ids) {
                                var nodes = leftTree.getNodesByFilter(function (node) {
                                    return $.inArray(node.id, ids) != -1;
                                });
                                angular.forEach(nodes, function (node) {
                                    leftTree.checkNode(node, true, false);
                                });
                                $scope.addToRight();
                            });
                        }
                    });

                    // 初始化右侧树
                    if (angular.isFunction(options.initRight)) {
                        doLazy(options.initRight(), initRightTree);
                    }

                };
                var $scope = common(options);
                $scope.options = options;

                $scope.showSelectAllButton = options.showSelectAllButton;

                var getFilterData = function (data) {
                    var filterData = (data || []).slice(0);
                    var filters = options.dataFilter || [];
                    if (typeof filters === 'function') {
                        filters = [filters];
                    }
                    // 没有过滤器
                    if (!(angular.isArray(filters) && filters.length > 0)) {
                        return data;
                    }
                    angular.forEach(filters, function (func) {
                        if (typeof func === 'function') {
                            filterData = func(filterData);
                        }
                    });
                    return filterData;
                };
                // 左（选择） --> 右
                $scope.addToRight = function () {
                    var leftCheckedItems = leftTree.getCheckedNodes(true);
                    var items = [];
                    angular.forEach(leftCheckedItems, function (item) {
                        items.push(angular.extend({}, item, {children: []}));
                    });
                    var filterData = getFilterData(items);
                    initRightTree(filterData);
                };

                // 将左侧全部加到右侧
                $scope.addAllToRight = function () {
                    leftTree.checkAllNodes(true);
                    $scope.addToRight();
                };

                // 删除右边选中元素
                $scope.removeFromRight = function () {
                    var items = rightTree.getCheckedNodes(true);
                    angular.forEach(items, function (item) {
                        var status = item.getCheckStatus();
                        // 半选中状态的元素不删除
                        if (status.checked && status.half) return;
                        rightTree.removeNode(item);
                    });
                };

                // 清空右侧的元素
                $scope.removeAllRight = function () {
                    initRightTree([]);
                };

                // 确定
                // 将右侧选中的数据传递给回调函数
                $scope.confirm = function () {
                    var min = options.min;
                    var max = options.max;
                    var items = rightTree && rightTree.getNodes() || [];
                    if (min > 0 && items.length < min) {
                        CommonUtils.errorDialog('至少选择[ ' + min + ' ]个元素!');
                        return false;
                    } else if (max && max < items.length) {
                        CommonUtils.errorDialog('最多选择[ ' + min + ' ]个元素!');
                        return false;
                    }
                    if (angular.isFunction(options.callback)) {
                        var foo = [];
                        rightTree && angular.forEach(rightTree.transformToArray(items), function (item) {
                            if (angular.isFunction(options.resultFilter)) {
                                if (options.resultFilter(item) == true) {
                                    foo.push(angular.extend({}, item, {children: []}));
                                }
                            } else {
                                foo.push(angular.extend({}, item, {children: []}));
                            }
                        });
                        options.callback.call($scope, foo);
                    }
                    $scope.$hide();
                };
            }
        }
    }]);
})();

angular.module('eccrm.angular.ztree', [
    'eccrm.directive.ztree',
    'eccrm.ztree.modal'
]);

