/**
 * Created by miles on 14-3-14.
 * dependencies:
 *  angularstrap-v2.0
 *  angularstrap-wrap.js
 *  parameter.js
 *
 */
(function (angular, window) {
    var app = angular.module('com.michael.base.region.modal', [
        'com.michael.base.region',
        'eccrm.angular',
        'eccrm.angularstrap',
        'base.emp',    // 员工
        'eccrm.directive.ztree'
    ]);
    app.factory('RegionModal', function ($modal, RegionService, AlertFactory, RegionConstant, ModalFactory, CommonUtils, EmpModal) {
            var pageTypes = ['add', 'modify', 'view'];
            var common = function (cfg, callback) {
                var defaults = {
                    scope: null,//必选项
                    callback: null,//点击确定后要执行的函数
                    afterShown: null//模态对话框显示完成后要执行的函数
                };
                cfg = angular.extend({}, defaults, cfg);
                var scope = cfg.scope;
                if (!scope) throw '使用模态对话框时必须指定scope!';
                if ($.inArray(cfg.type, pageTypes) == -1) {
                    throw '不合法的页面类型，仅支持[' + pageTypes.join(',') + ']类型!';
                }
                callback = callback || cfg.callback;

                var modal = $modal({
                    scope: scope,
                    template: CommonUtils.contextPathURL('app/base/region/template/region-modal-edit.html')
                });

                var $scope = modal.$scope;

                $scope.status = RegionConstant.status;
                $scope.types = RegionConstant.type;
                $scope.type = cfg.type;

                // 选择负责人
                $scope.pickMaster = function () {
                    EmpModal.pick({}, function (emp) {
                        $scope.region.masterId = emp.id;
                        $scope.region.masterName = emp.name;
                    });
                };

                $scope.clearMaster = function () {
                    $scope.region.masterId = null;
                    $scope.region.masterName = null;
                };

                $scope.checkCode = {
                    validateMsg: '不合法的区号',
                    trigger: 'now',
                    validateType: 'errorCode',
                    validateFn: function (value) {
                        if (value === '' || value === undefined || (value && /^\d+$/g.test(value))) {
                            return true;
                        }
                        return false;
                    }
                };
                if (cfg.type == 'add') {
                    $scope.save = function (createNew) {
                        RegionService.save($scope.region, function (data) {
                            AlertFactory.success('保存成功!');
                            if (callback && angular.isFunction(callback)) {
                                callback.call($scope, data.id);
                            }
                            if (createNew == true) {
                                $scope.region.name = null;
                                $scope.region.pinyin = null;
                                $scope.region.jp = null;
                                $scope.region.zipcode = null;
                                $scope.region.sequenceNo = ($scope.region.sequenceNo || 0) + 1;
                            } else {
                                $scope.$hide();
                            }
                        });
                    };
                    $scope.region = angular.extend({}, RegionConstant.defaults);
                } else if (cfg.type == 'modify') {
                    RegionService.get({id: cfg.id}, function (data) {
                        data = data.data || {};
                        $scope.region = data;
                        $scope.region.parent = {
                            id: data.parentId,
                            name: data.parentName
                        };
                    });
                    $scope.update = function () {
                        RegionService.update($scope.region, function (data) {
                            if (data && data.success) {
                                if (callback && angular.isFunction(callback)) {
                                    callback.call($scope, data.id);
                                }
                                $scope.$hide();
                            } else {
                                AlertFactory.updateError($scope, data);
                            }
                        });
                    }
                } else if (cfg.type == 'view') {
                    RegionService.get({id: cfg.id}, function (data) {
                        data = data.data || {};
                        $scope.region = data;
                        $('input,select,textarea').attr('disabled', 'disabled');
                    });

                }

                //ztree配置项
                $scope.ztreeOptions = {
                    maxHeight: 250,
                    // 初识数据加载
                    data: function (callback) {
                        return CommonUtils.promise(function (defer) {
                            RegionService.tree(function (data) {
                                data = data.data || [];
                                defer.resolve(data);
                            });
                        });
                    },
                    click: function (data) {
                        $scope.region.parentId = data.id;
                        $scope.region.parentName = data.name;
                    }
                };

                $scope.clearParent = function () {
                    $scope.region.parentId = null;
                    $scope.region.parentName = null;
                };
                //模态对话框显示后要执行的操作
                ModalFactory.afterShown(modal, cfg.afterShown);
            };
            return {
                add: function (cfg, callback) {
                    var o = angular.extend({}, cfg, {type: 'add'});
                    common(o, callback);
                },
                modify: function (cfg, callback) {
                    if (!cfg.id) throw '没有获得ID!';
                    var o = angular.extend({}, cfg, {type: 'modify'});
                    common(o, callback);
                },
                view: function (cfg, callback) {
                    if (!cfg.id) throw '没有获得ID!';
                    var o = angular.extend({}, cfg, {type: 'view'});
                    common(o, callback);
                }
            }
        }
    )
})(angular, window);