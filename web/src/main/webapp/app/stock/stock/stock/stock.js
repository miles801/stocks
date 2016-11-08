/**
 * 股票
 * Created by Michael .
 */
(function (angular) {
    var app = angular.module('stock.stock.stock', [
        'ngResource',
        'base.param',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.service('StockService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/stock/stock/stock/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 不带分页的列表查询
            query: {method: 'POST', params: {method: 'query'}, isArray: false},

            // 导入数据
            importData: {method: 'POST', params: {method: 'import', attachmentIds: '@attachmentIds'}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', limit: '@limit', start: '@start'},
                isArray: false
            },

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            // 股票同步
            sync: {method: 'POST', params: {method: 'sync'}, isArray: false}

        })
    });

    app.service('StockParam', function (ParameterLoader) {
        var o = {};

        // 状态
        o['status'] = function (callback) {
            ParameterLoader.loadSysParam('STOCK_STATUS', callback);
        };

        return o;
    });

})(angular);
