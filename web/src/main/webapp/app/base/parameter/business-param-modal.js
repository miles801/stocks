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
    var app = angular.module('base.param.modal.business', [
        'base.param',
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.directive.ztree'
    ]);
    //页面类型
    var pageTypes = ['add', 'modify', 'view'];

    //参数类型模态对话框
    app.factory('BusinessParamTypeModal', function ($modal, BusinessParamType, AlertFactory, Parameter, ModalFactory, $q, Debounce, CommonUtils) {
        var common = function (options, callback) {
            var defaults = {
                id: null,//id
                pageType: null,//必填项,页面类型
                scope: null,//必选项
                callback: null,//点击确定后要执行的函数
                afterShown: null//模态对话框显示完成后要执行的函数
            };
            options = angular.extend({}, defaults, options);
            var scope = options.scope;
            if (!scope) throw '使用模态对话框时必须指定scope!';
            callback = callback || options.callback;
            if ($.inArray(options.type, pageTypes) == -1) {
                throw '不合法的页面类型，仅支持[' + pageTypes.join(',') + ']类型!';
            }

            var modal = $modal({scope: scope, template: CommonUtils.contextPathURL('app/base/parameter/template/param-type-modal.ftl.html'), backdrop: 'static'});

            var $scope = modal.$scope;
            $scope.type = options.type;
            var id = options.id;
            var pageType = options.type;
            Parameter.status(function (data) {
                $scope.status = data.data || [];
            });
            if (pageType == 'add' || pageType == 'modify') {
                $scope.validateMultiName = {
                    validateMsg: '名称重复',
                    validateType: 'multiName',
                    validateFn: function (value) {
                        if (!value) return;
                        var defer = $q.defer();
                        Debounce.delay(function () {
                            BusinessParamType.hasName({id: id, name: value}, function (data) {
                                defer.resolve(data.data === false);
                            }, 1000);
                        });
                        return defer.promise;
                    }
                };
                // 选择上级
                $scope.ztreeOptions = {
                    multi: true,
                    data: function () {
                        var defer = $q.defer();
                        var result = BusinessParamType.queryOther({cid: id});
                        AlertFactory.handle($scope, result, function (data) {
                            defer.resolve(data.data || []);
                        });
                        return defer;
                    },
                    click: function (node) {
                        $scope.parameterType.parent = {id: node.id, name: node.name};
                    }
                };
            }

            if (options.type == 'add') {
                $scope.validateMultiCode = {
                    validateMsg: '编号重复',
                    validateType: 'multiCode',
                    validateFn: function (value) {
                        if (!value) return;
                        var defer = $q.defer();
                        Debounce.delay(function () {
                            var result = BusinessParamType.hasCode({code: value});
                            AlertFactory.handle($scope, result, function (data) {
                                defer.resolve(data.data === false);
                            })
                        }, 1000);
                        return defer.promise;
                    }
                };
                $scope.parameterType = {
                    createdDatetime: new Date().getTime(),
                    creatorName: CommonUtils.loginContext().username,
                    status: 'ACTIVE',
                    sequenceNo: 0
                };
                $scope.save = function (createNew) {
                    var result = BusinessParamType.save($scope.parameterType);
                    AlertFactory.handle($scope, result, function (data) {
                        if (callback && angular.isFunction(callback)) {
                            callback.call($scope, data);
                        }
                        if (createNew == true) {
                            $scope.parameterType.name = null;
                            $scope.parameterType.code = null;
                        } else {
                            $scope.$hide();
                        }
                    });
                }
            }
            else if (pageType == 'modify') {
                //回显数据
                (function () {
                    var result = BusinessParamType.get({id: id});
                    AlertFactory.handle($scope, result, function (data) {
                        data = data.data || {};
                        $scope.parameterType = data;
                        $scope.parameterType.parent = {
                            id: data.parentId,
                            name: data.parentName
                        };
                    });
                })();
                $scope.update = function () {
                    var result = BusinessParamType.update($scope.parameterType);
                    AlertFactory.handle($scope, result, function (data) {
                        data = data.data || {};
                        if (callback && angular.isFunction(callback)) {
                            callback.call($scope, data);
                        }
                        $scope.$hide();
                    });
                }
            } else if (pageType == 'view') {
                //回显数据
                var viewResult = BusinessParamType.get({id: options.id});
                AlertFactory.handle($scope, viewResult, function (data) {
                    data = data.data || {};
                    $scope.parameterType = data;
                    $scope.parameterType.parent = {
                        id: data.parentId,
                        name: data.parentName
                    };
                    $('input,select,textarea').attr('disabled', 'disabled');
                });
            }

            //模态对话框显示后要执行的操作
            ModalFactory.afterShown(modal, options.afterShown);
        };
        return {
            add: function (options, callback) {
                var o = angular.extend({}, options, {type: 'add'});
                common(o, callback);
            },
            modify: function (options, callback) {
                if (!options.id) throw '没有获得ID!';
                var o = angular.extend({}, options, {type: 'modify'});
                common(o, callback);
            },
            view: function (options, callback) {
                if (!options.id) throw '没有获得ID!';
                var o = angular.extend({}, options, {type: 'view'});
                common(o, callback);
            }
        }
    });

    app.factory('BusinessParamItemModal', function ($modal, BusinessParamItem, AlertFactory, Parameter, ModalFactory, $q, Debounce, BusinessParamType, CommonUtils) {
        var common = function (pageType, options, callback) {
            if (!pageType)throw '没有指定页面类型!';
            var defaults = {
                scope: null,//必选项
                type: null,//新建时：所属类型
                callback: null,//点击确定后要执行的函数
                afterShown: null//模态对话框显示完成后要执行的函数
            };
            options = angular.extend({}, defaults, options);
            var scope = options.scope;
            if (!scope) throw '使用模态对话框时必须指定scope!';
            callback = callback || options.callback;

            var modal = $modal({scope: scope, template: CommonUtils.contextPathURL('app/base/parameter/template/param-item-modal.ftl.html'), backdrop: 'static'});

            var $scope = modal.$scope;
            $scope.pageType = pageType;
            Parameter.status(function (data) {
                $scope.status = data.data || [];
            });
            var defaultValue, defaultName;
            $scope.checkValue = {
                validateType: 'multiValue',
                validateMsg: '值重复!',
                validateFn: function (value) {
                    if (!value) return true;
                    if (value == defaultValue) return true;
                    var defer = $q.defer();
                    Debounce.delay(function () {
                        var result = BusinessParamItem.hasValue({type: $scope.beans.type, value: value});
                        AlertFactory.handle($scope, result, function (data) {
                            defer.resolve(!data.data || false);
                        });
                    }, 1000);
                    return defer.promise;
                }
            };
            $scope.checkName = {
                validateType: 'multiName',
                validateMsg: '名称重复!',
                validateFn: function (value) {
                    if (!value) return true;
                    if (value == defaultName) return true;
                    var defer = $q.defer();
                    var result = BusinessParamItem.hasName({type: $scope.beans.type, name: value});
                    AlertFactory.handle($scope, result, function (data) {
                        defer.resolve(!data.data || false);
                    });
                    return defer.promise;
                }
            };
            $scope.cascadeTypeOptions = {
                data: BusinessParamType.queryOther,
                click: function (node) {
                    //console.dir(node);
                    $scope.beans.cascadeTypeName = node.name;
                    $scope.beans.cascadeTypeCode = node.code;
                }
            };
            $scope.cascadeItemOptions = {
                data: BusinessParamType.queryOther,
                click: function (node) {
                    //console.dir(node);
                    $scope.beans.cascadeItemName = node.name;
                    $scope.beans.cascadeItemValue = node.value;
                }
            };
            if (pageType == 'add') {
                if (!options.type || !options.type.code) throw '没有获得参数的所属类型!';
                //选择级联类型
                $scope.beans = {
                    createdDatetime: new Date().getTime(),
                    creatorName: CommonUtils.loginContext().username,
                    status: 'ACTIVE',
                    type: options.type.code,
                    typeName: options.type.name
                };
                $scope.save = function (createNew) {
                    var result = BusinessParamItem.save($scope.beans);
                    AlertFactory.handle($scope, result, function (data) {
                        data = data.data;
                        if (callback && angular.isFunction(callback)) {
                            callback.call($scope, data);
                        }
                        if (createNew == true) {
                            $scope.beans.name = null;
                            $scope.beans.value = null;
                            $scope.beans.sequenceNo = $scope.beans.sequenceNo + 1;
                        } else {
                            $scope.$hide();
                        }
                    })
                }
            } else if (pageType == 'modify') {
                (function () {
                    var result = BusinessParamItem.get({id: options.id});
                    AlertFactory.handle($scope, result, function (data) {
                        data = data.data;
                        $scope.beans = data;
                        defaultName = data.name;
                        defaultValue = data.value;
                    });
                })();
                $scope.update = function () {
                    var result = BusinessParamItem.update($scope.beans);
                    AlertFactory.handle($scope, result, function (data) {
                        if (callback && angular.isFunction(callback)) {
                            callback.call($scope, data.data);
                        }
                        $scope.$hide();
                    })
                }

            } else if (pageType == 'view') {
                (function () {
                    var result = BusinessParamItem.get({id: options.id});
                    AlertFactory.handle($scope, result, function (data) {
                        $scope.beans = data.data;
                        $('input,textarea,select', '.modal').attr('disabled', 'disabled');
                    });
                })();
            }
            //模态对话框显示后要执行的操作
            ModalFactory.afterShown(modal, options.afterShown);
        };
        return {
            add: function (options, callback) {
                var o = angular.extend({}, options);
                common('add', o, callback);
            },
            modify: function (options, callback) {
                if (!options.id) throw '没有获得ID!';
                var o = angular.extend({}, options);
                common('modify', o, callback);
            },
            view: function (options, callback) {
                if (!options.id) throw '没有获得ID!';
                var o = angular.extend({}, options);
                common('view', o, callback);
            }
        }
    });
})
(angular, window);