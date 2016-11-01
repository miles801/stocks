/**
 * Fn数据库
 * Created by Michael .
 */
(function (angular) {
    var app = angular.module('stock.db.fnDB', [
        'ngResource',
        'base.param',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.service('FnDBService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/stock/db/fnDB/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 不带分页的列表查询
            query: {method: 'POST', params: {method: 'query'}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 分页查询
            pageQuery: {
                method : 'POST',
                params : {method: 'pageQuery', limit: '@limit', start: '@start'},
                isArray: false
            },

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false}
        })
    });

    app.service('FnDBParam', function (ParameterLoader) {
        var o = {};

        // 所属数据库
        o['type'] = function (callback) {
            ParameterLoader.loadSysParam('DB_TYPE', callback);
        };

        return o;
    });

})(angular);
