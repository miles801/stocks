/**
 * 日K
 * Created by Michael .
 */
(function (angular) {
    var app = angular.module('stock.stock.stockDay', [
        'ngResource',
        'base.param',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.service('StockDayService', function (CommonUtils, $resource) {
        return $resource(CommonUtils.contextPathURL('/stock/stock/stockDay/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 不带分页的列表查询
            query: {method: 'POST', params: {method: 'query'}, isArray: false},

            // 导入数据
            importData: {method: 'POST', params: {method: 'import'}, isArray: false},

            // 根据id查询信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {
                    method: 'pageQuery',
                    limit: '@limit',
                    start: '@start',
                    orderBy: '@orderBy',
                    reverse: '@reverse'
                },
                isArray: false
            },

            // 根据id字符串（使用逗号分隔多个值）
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            // 3线分析报告
            report3: {
                method: 'POST',
                params: {method: 'report3', start: '@start', limit: '@limit', orderBy: '@orderBy', reverse: '@reverse'},
                isArray: false
            },
            // 6线分析报告
            report6: {
                method: 'POST',
                params: {method: 'report6', start: '@start', limit: '@limit', orderBy: '@orderBy', reverse: '@reverse'},
                isArray: false
            },
            // 3线估值结果
            result3: {
                method: 'POST',
                params: {method: 'result3', start: '@start', limit: '@limit', orderBy: '@orderBy', reverse: '@reverse'},
                isArray: false
            },
            // 6线估值结果
            result6: {
                method: 'POST',
                params: {method: 'result6', start: '@start', limit: '@limit', orderBy: '@orderBy', reverse: '@reverse'},
                isArray: false
            },

            // 最后一个交易日
            lastDay: {method: 'GET', params: {method: 'lastDay'}, isArray: false},
            // 重置第七日交易数据
            reset7: {method: 'POST', params: {method: 'reset7'}, isArray: false},

            // 同步股票交易历史
            sync: {method: 'POST', params: {method: 'sync'}, isArray: false}
        })
    });

    app.service('StockDayParam', function (ParameterLoader) {
        var o = {};
        return o;
    });

})(angular);
