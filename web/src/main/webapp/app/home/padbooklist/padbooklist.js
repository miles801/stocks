/**
* Created by shenbb on 2014-03-05 18:22:04.
*/
(function (angular) {
    var app = angular.module("eccrm.home.padbooklist", ['ngResource']);
        app.service('NoteBookService', function ($resource,CommonUtils) {
            return $resource(CommonUtils.contextPathURL('/base/notebook/:method/:id'), {}, {
                //保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},
            //更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},
            //根据id查询便签本信息
            get: {method: 'GET', params: {id: '@id'}, isArray: false},
            //分页查询，返回{total:,data:[{},{}]}
            page: {method: 'POST', params: {method: 'query'}, isArray: false},
            //根据id字符串（使用逗号分隔多个值），删除对应的便签本，成功则返回{success:true}
            deleteByIds: {method: 'POST', params: {method: 'delete', ids: '@ids'}, isArray: false},
            //分页查询，返回{total:,data:''}
            pageQuery: {method: 'POST', params: {method: 'query', start: '@start', limit: '@limit'}, isArray: false}
        })
    });
})(angular);

