/**
 * 依赖于
 *  angular-all.js
 *  angular-strap-all.js 信息提示用
 * Created by miles on 2014/5/13.
 */
(function (angular) {
    var app = angular.module('base.attachment', [
        'ngResource',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    app.service('AttachmentService', ['$resource', 'CommonUtils', function ($resource, CommonUtils) {

        return $resource(CommonUtils.contextPathURL('attachment/:method'), {}, {
            //上传附件
            upload: {method: 'POST', params: {method: 'upload'}, isArray: false},

            //删除指定id的附件
            remove: {method: 'GET', params: {method: 'delete', ids: '@ids'}, isArray: false},

            removeTmp: {method: 'GET', params: {method: 'delete/tmp', ids: '@ids'}, isArray: false},

            //获得图片信息
            image: {method: 'GET', params: {method: 'image', id: '@id'}},

            //查询指定id的附件信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            //查询附件列表的信息
            //支持的参数：{bid:'业务id',btype:'业务类型',bclass:'业务class'}
            query: {method: 'GET', params: {method: 'query'}, isArray: false},

            // 分页高级查询
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', start: '@start', limit: '@limit'},
                isArray: false
            },

            // 根据id列表查询附件的信息
            queryByIds: {method: 'GET', params: {method: 'queryByIds', ids: '@ids'}, isArray: false},

            // 根据ID删除
            // 注意：由于要对外提供接口，所以这里使用的是GET请求，而非DELETE请求
            deleteByIds: {method: 'GET', params: {method: 'delete', ids: '@ids'}, isArray: false}
        });
    }]);


    app.filter('fileSize', function () {
        return function (value) {
            if (typeof value === 'number') {
                if (value < 1000) {
                    return value + ' Byte';
                } else if (value < 1000000) {
                    return (value / 1000).toFixed(2) + ' KB';
                } else if (value < 1000000000) {
                    return (value / 1000000).toFixed(2) + ' MB';
                } else if (value < 1000000000000) {
                    return (value / 1000000000).toFixed(2) + ' GB';
                } else {
                    return value + ' Byte';
                }
            }
            return '0KB';

        }
    });
})(angular);