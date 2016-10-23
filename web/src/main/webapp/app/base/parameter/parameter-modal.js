/**
 * Created by miles on 14-3-14.
 * dependencies:
 *  angularstrap-v2.0
 *  angularstrap-wrap.js
 *  parameter.js
 *  validation.tooltip.js
 *
 */


(function (angular, window) {
    var app = angular.module('base.parameter.modal', [
        'base.param',
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.directive.ztree'
    ]);
    // 页面类型
    var pageTypes = ['add', 'modify', 'view'];

    var common_type = function (config, options, callback) {
        var defaults = {
            id: null,//id
            pageType: null,// 必填项,页面类型，可选值为add、modify、view
            parentId: null,// 默认上级
            parentName: null, // 上级名称
            scope: null,//必选项
            callback: null,//点击确定后要执行的函数
            afterShown: null//模态对话框加载完成后要执行的函数
        };
        options = angular.extend({}, defaults, options);
        var $modal = config.modal;
        var AlertFactory = config.AlertFactory;
        var CommonUtils = config.CommonUtils;
        var ModalFactory = config.ModalFactory;
        var ParameterType = config.ParameterType;
        var Parameter = config.Parameter;
        var scope = options.scope;
        if (!scope) throw '使用模态对话框时必须指定scope!';
        callback = callback || options.callback;
        var pageType = options.pageType;
        if ($.inArray(pageType, pageTypes) == -1) {
            throw '不合法的页面类型，仅支持[' + pageTypes.join(',') + ']类型!';
        }

        var modal = $modal({
            scope: scope,
            template: CommonUtils.contextPathURL('app/base/parameter/template/param-type-modal.ftl.html'),
            backdrop: 'static'
        });

        var $scope = modal.$scope;
        $scope.pageType = pageType;
        var id = options.id;
        Parameter.status(function (data) {
            $scope.status = data.data || [];
        });

        var load = function (id, callback) {
            var result = ParameterType.get({id: id});
            AlertFactory.handle($scope, result, function (data) {
                data = data.data || {};
                $scope.parameterType = data;
                angular.isFunction(callback) ? callback.call($scope, data) : null;
            });
        };
        if (pageType == 'add' || pageType == 'modify') {
            // 选择上级
            $scope.ztreeOptions = {
                data: function () {
                    return CommonUtils.promise(function (defer) {
                        var result = ParameterType.queryOther({cid: id});
                        AlertFactory.handle($scope, result, function (data) {
                            defer.resolve(data.data || []);
                        });
                    });
                },
                click: function (node) {
                    $scope.parameterType.parentId = node.id;
                    $scope.parameterType.parentName = node.name;
                }
            };
        }
        var originalName;
        var checkName = function (defer) {
            if (originalName === $scope.parameterType.name) {
                defer.resolve(true);
                return true;
            }
            var result = ParameterType.hasName({
                id: $scope.parameterType.parentId,
                name: CommonUtils.encode($scope.parameterType.name)
            });
            AlertFactory.handle($scope, result, function (data) {
                data.data == false ? defer.resolve(true) : defer.reject('名称重复!');
            });
        };
        var checkCode = function (defer) {
            var result = ParameterType.hasCode({code: CommonUtils.encode($scope.parameterType.code)});
            AlertFactory.handle($scope, result, function (data) {
                data.data == false ? defer.resolve(true) : defer.reject('编号重复!');
            })
        };
        if (pageType == 'add') {
            $scope.parameterType = {
                createdDatetime: new Date().getTime(),
                creatorName: CommonUtils.loginContext().username,
                status: 'ACTIVE',
                sequenceNo: 1
            };
            if (options.parentId && options.parentName) {
                $scope.parameterType.parentId = options.id;
                $scope.parameterType.parentName = options.name;
            }
            var reset = function () {
                $scope.parameterType.name = null;
                $scope.parameterType.code = null;
                $scope.parameterType.sequenceNo = ($scope.parameterType.sequenceNo || 1) + 1;
            };
            var save = function (createNew) {
                var result = ParameterType.save($scope.parameterType);
                AlertFactory.handle($scope, result, function (data) {
                    angular.isFunction(callback) ? callback.call($scope, data) : null;
                    createNew == true ? reset() : $scope.$hide();
                });
            };
            $scope.save = function (createNew) {
                // check
                CommonUtils.promise(checkName)
                    .then(function () {
                        return CommonUtils.promise(checkCode);
                    })
                    .then(function () {
                        save(createNew);
                    }, function (reason) {
                        AlertFactory.error($scope, reason);
                    });
            }
        }
        else if (pageType == 'modify') {
            var update = function () {
                var result = ParameterType.update($scope.parameterType);
                AlertFactory.handle($scope, result, function (data) {
                    data = data.data || {};
                    angular.isFunction(callback) ? callback.call($scope, data) : null;
                    $scope.$hide();
                });
            };
            $scope.update = function () {
                CommonUtils.promise(checkName)
                    .then(update, function (reason) {
                        AlertFactory.error(reason);
                    });
            };

            //回显数据
            load(id, function (data) {
                originalName = data.name
            });
        } else if (pageType == 'view') {
            //回显数据
            load(options.id, function () {
                $('input,select,textarea', '.modal').attr('disabled', 'disabled');
            });
        }

        //模态对话框显示后要执行的操作
        ModalFactory.afterShown(modal, options.afterShown);
    };
    var common_item = function (config, options, callback) {
        var defaults = {
            id: null,//id
            parameterType: null,// 新建时：所属的参数类型
            pageType: null,//必填项,页面类型
            scope: null,//必选项
            callback: null,//点击确定后要执行的函数
            afterShown: null//模态对话框显示完成后要执行的函数
        };
        options = angular.extend({}, defaults, options);
        var $modal = config.modal;
        var AlertFactory = config.AlertFactory;
        var CommonUtils = config.CommonUtils;
        var ModalFactory = config.ModalFactory;
        var ParameterType = config.ParameterType;
        var ParameterItem = config.ParameterItem;
        var Parameter = config.Parameter;
        var scope = options.scope;
        if (!scope) throw '使用模态对话框时必须指定scope!';
        callback = callback || options.callback;
        if ($.inArray(options.pageType, pageTypes) == -1) {
            var msg = '不合法的页面类型，仅支持[' + pageTypes.join(',') + ']类型!';
            CommonUtils.errorDialog(msg);
            throw msg;
        }

        var modal = $modal({
            scope: scope,
            template: CommonUtils.contextPathURL('app/base/parameter/template/param-item-modal.ftl.html'),
            backdrop: 'static'
        });

        var $scope = modal.$scope;
        var pageType = $scope.pageType = options.pageType;
        Parameter.status(function (data) {
            $scope.status = data.data || [];
        });
        var defaultValue, defaultName;
        var checkValue = function () {
            var context = this;
            if ($scope.beans.value === defaultValue) {
                context.resolve(true);
                return true;
            }
            var result = ParameterItem.hasValue({
                type: $scope.beans.type,
                value: CommonUtils.encode($scope.beans.value)
            });
            AlertFactory.handle($scope, result, function (data) {
                !data.data == true ? context.resolve(true) : context.reject('值重复!');
            });
        };
        var checkName = function () {
            var context = this;
            if ($scope.beans.name === defaultName) {
                context.resolve(true);
                return true;
            }
            var result = ParameterItem.hasName({type: $scope.beans.type, name: CommonUtils.encode($scope.beans.name)});
            AlertFactory.handle($scope, result, function (data) {
                !data.data == true ? context.resolve(true) : context.reject('名称重复!');
            });
        };

        // 级联类型
        $scope.cascadeTypeOptions = {
            treeId: 'cascadeTypeTree',
            data: function () {
                return CommonUtils.promise(function (defer) {
                    var result = ParameterType.queryValid();
                    AlertFactory.handle($scope, result, function (data) {
                        defer.resolve(data.data || []);
                    });
                });
            },
            click: function (node) {
                if ($scope.beans.cascadeTypeCode !== node.code) {
                    $scope.beans.cascadeTypeName = node.name;
                    $scope.beans.cascadeTypeCode = node.code;
                    $scope.beans.cascadeItemValue = null;
                    $scope.beans.cascadeItemName = null;
                    // 重新加载选项树
                    $scope.cascadeItemOptions.reload = true;
                }
            }
        };
        // 级联参数
        $scope.cascadeItemOptions = {
            treeId: 'cascadeItemTree',
            multi: true,
            data: function () {
                var type = CommonUtils.parse($scope, 'beans.cascadeTypeCode');
                if (!type) return [];
                return CommonUtils.promise(function (defer) {
                    var result = ParameterItem.queryValid({type: CommonUtils.encode(type)});
                    AlertFactory.handle($scope, result, function (data) {
                        defer.resolve(data.data || []);
                    });
                });
            },
            click: function (node) {
                $scope.beans.cascadeItemName = node.name;
                $scope.beans.cascadeItemValue = node.value;
            }
        };

        var load = function (id, callback) {
            var result = ParameterItem.get({id: id});
            AlertFactory.handle($scope, result, function (data) {
                data = data.data || {};
                $scope.beans = data;
                defaultValue = data.value;
                defaultName = data.name;
                $scope.typeName = data.typeName;
                angular.isFunction(callback) ? callback.call($scope, data) : null;
            });
        };
        if (pageType === 'add') {
            $scope.beans = {
                createdDatetime: new Date().getTime(),
                creatorName: CommonUtils.loginContext().username,
                type: options.parameterType.code,
                sequenceNo: 1,
                status: 'ACTIVE'
            };
            $scope.typeName = options.parameterType.name;

            var save = function (createNew) {
                var result = ParameterItem.save($scope.beans);
                var reset = function () {
                    $scope.beans.name = null;
                    $scope.beans.value = null;
                    $scope.beans.sequenceNo = ($scope.beans.sequenceNo || 1) + 1;
                };
                AlertFactory.handle($scope, result, function (data) {
                    data = data.data;
                    angular.isFunction(callback) ? callback.call($scope, data) : null;
                    createNew === true ? reset() : $scope.$hide();
                });
            };

            $scope.save = function (createNew) {
                CommonUtils.chain([checkName, checkValue], function () {
                    save(createNew);
                }, function (reason) {
                    CommonUtils.errorDialog(reason);
                });
            }
        }
        if (pageType === 'modify') {
            load(options.id, function (data) {
                defaultName = data.name;
                defaultValue = data.value;
            });
            var update = function () {
                var result = ParameterItem.update($scope.beans);
                AlertFactory.handle($scope, result, function (data) {
                    angular.isFunction(callback) ? callback.call($scope, data.data) : null;
                    $scope.$hide();
                })
            };
            $scope.update = function () {
                CommonUtils.chain([checkName, checkValue], update, function (reason) {
                    CommonUtils.errorDialog(reason);
                });
            }
        }
        if (pageType === 'view') {
            load(options.id, function () {
                $('input,textarea,select', '.modal').attr('disabled', 'disabled');
            })
        }

        ModalFactory.afterShown(modal, options.afterShown);
    };
    var commonFunction = function (func, config) {
        return {
            add: function (options, callback) {
                var o = angular.extend({}, options, {pageType: 'add'});
                func(config, o, callback);
            },
            modify: function (options, callback) {
                if (!options.id) throw '没有获得ID!';
                var o = angular.extend({}, options, {pageType: 'modify'});
                func(config, o, callback);
            },
            view: function (options, callback) {
                if (!options.id) throw '没有获得ID!';
                var o = angular.extend({}, options, {pageType: 'view'});
                func(config, o, callback);
            }
        };
    };


    // 系统参数类型模态对话框
    app.factory('SysParamTypeModal', function ($modal, SysParamType, AlertFactory, Parameter, ModalFactory, CommonUtils) {
        var config = {
            modal: $modal,
            ParameterType: SysParamType,
            Parameter: Parameter,
            CommonUtils: CommonUtils,
            AlertFactory: AlertFactory,
            ModalFactory: ModalFactory
        };
        return commonFunction(common_type, config);
    });
    app.factory('BusinessParamTypeModal', function ($modal, BusinessParamType, AlertFactory, Parameter, ModalFactory, CommonUtils) {
        var config = {
            modal: $modal,
            ParameterType: BusinessParamType,
            Parameter: Parameter,
            CommonUtils: CommonUtils,
            AlertFactory: AlertFactory,
            ModalFactory: ModalFactory
        };
        return commonFunction(common_type, config);
    });
    app.factory('SysParamItemModal', function ($modal, SysParamItem, AlertFactory, Parameter, ModalFactory, SysParamType, CommonUtils) {
        var config = {
            modal: $modal,
            ParameterItem: SysParamItem,
            ParameterType: SysParamType,
            Parameter: Parameter,
            CommonUtils: CommonUtils,
            AlertFactory: AlertFactory,
            ModalFactory: ModalFactory
        };
        return commonFunction(common_item, config);
    });
    app.factory('BusinessParamItemModal', function ($modal, BusinessParamItem, AlertFactory, Parameter, ModalFactory, BusinessParamType, CommonUtils) {
        var config = {
            modal: $modal,
            ParameterItem: BusinessParamItem,
            ParameterType: BusinessParamType,
            Parameter: Parameter,
            CommonUtils: CommonUtils,
            AlertFactory: AlertFactory,
            ModalFactory: ModalFactory
        };
        return commonFunction(common_item, config);
    });

})
(angular, window);