/**
 * 数据库
 * Created by Michael .
 */
(function (angular) {
    var app = angular.module('stock.db.dB', [
        'ngResource',
        'base.param',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.service('DBService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/stock/db/dB/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 不带分页的列表查询
            query: {
                method: 'POST',
                params: {method: 'query', orderBy: 'dbDate', reverse: false, orderBy: '@orderBy', reverse: '@reverse'},
                isArray: false
            },

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 重置投影计算
            reset: {method: 'POST', params: {method: 'reset'}, isArray: false},

            // 计算
            calculate: {
                method: 'POST',
                params: {
                    method: 'calculate',
                    xtype: '@xtype',
                    start: '@start',
                    limit: '@limit',
                    orderBy: '@orderBy',
                    reverse: '@reverse'
                },
                isArray: false
            },

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', limit: '@limit', start: '@start'},
                isArray: false
            },

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('Fn5Service', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/stock/fn/fn5/:method'), {}, {
            // 重置
            reset: {method: 'POST', params: {method: 'reset'}, isArray: false},
            // 最后一次处理时间
            lastHandle: {method: 'GET', params: {method: 'lastHandle', type: '@type'}, isArray: false}
        })
    });

    app.service('Fn4Service', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/stock/fn/fn4/:method'), {}, {
            // 重置
            reset: {method: 'POST', params: {method: 'reset'}, isArray: false},
            // 最后一次处理时间
            lastHandle: {method: 'GET', params: {method: 'lastHandle', type: '@type'}, isArray: false}
        })
    });

    app.service('DBParam', function (ParameterLoader) {
        var o = {};

        // 类型
        o['type'] = function (callback) {
            ParameterLoader.loadSysParam('DB_TYPE', callback);
        };

        return o;
    });

    app.service('DBModal', function ($modal, ModalFactory, AlertFactory, CommonUtils, DBService) {
        var common = function (options, callback) {
            var defaults = {
                id: null,//id
                pageType: null,     // 必填项,页面类型add/modify/view
                callback: null     // 点击确定后要执行的函数
            };
            options = angular.extend({}, defaults, options);
            callback = callback || options.callback;
            var modal = $modal({
                template: CommonUtils.contextPathURL('/app/stock/db/dB/template/dB-modal.ftl.html'),
                backdrop: 'static'
            });
            var $scope = modal.$scope;
            var pageType = $scope.pageType = options.pageType;
            var id = options.id;
            $scope.save = function () {
                var promise = DBService.save($scope.beans, function (data) {
                    AlertFactory.success('保存成功!');
                    angular.isFunction(callback) && callback();
                    $scope.$hide();
                });
                CommonUtils.loading(promise);
            };

            $scope.update = function () {
                var promise = DBService.update($scope.beans, function (data) {
                    AlertFactory.success('更新成功!');
                    angular.isFunction(callback) && callback();
                    $scope.$hide();
                });
                CommonUtils.loading(promise);
            };

            var load = function (id, callback) {
                var promise = DBService.get({id: id}, function (data) {
                    $scope.beans = data.data || {};
                    callback($scope.beans);
                });
                CommonUtils.loading(promise);
            };

            if (pageType == 'add') {
                $scope.beans = {};
            } else if (pageType == 'modify') {
                load(id);
            } else {
                load(id, function () {
                    $('.modal-body').find('input,select,textarea').attr('disabled', 'disabled');
                    $('.modal-body').find('.icons.icon').remove();
                });
            }
        };
        return {
            add: function (options, callback) {
                var o = angular.extend({}, options, {pageType: 'add'});
                common(o, callback);
            },
            modify: function (options, callback) {
                if (!options.id) {
                    alert('更新页面加载失败!没有获得ID');
                    return;
                }
                var o = angular.extend({}, options, {pageType: 'modify'});
                common(o, callback);
            },
            view: function (options, callback) {
                if (!options.id) {
                    alert('明细页面加载失败!没有获得ID');
                    return;
                }
                var o = angular.extend({}, options, {pageType: 'view'});
                common(o, callback);
            }
        }
    });
})(angular);
