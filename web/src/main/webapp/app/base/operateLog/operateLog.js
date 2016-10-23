(function (angular) {
    var app = angular.module("com.michael.base.operatelog", [
        'ngResource',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);


    app.service('OperateLogService', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('/base/operateLog/:method'), {}, {

            //根据id查询账户信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},


            //分页查询，返回data数据为：{total:,data:[]}
            pageQuery: {method: 'POST', params: {method: 'pageQuery', start: '@start', limit: '@limit'}, isArray: false}

        })
    });

    app.controller('OperateLogCtrl', function ($scope, OperateLogService, CommonUtils) {
        $scope.operateTypes = [
            {name: '全部'},
            {name: '新增', value: 'add'},
            {name: '修改', value: 'modify'},
            {name: '删除', value: 'delete'}
        ];
        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, $scope.condition, {start: this.start, limit: this.limit});
                return CommonUtils.promise(function (defer) {
                    var promise = OperateLogService.pageQuery(param);
                    CommonUtils.loading(promise, '加载中...', function (data) {
                        data = data.data || {total: 0};
                        $scope.beans = data;
                        defer.resolve(data);
                    });
                });
            }
        };


        //定义方法
        $scope.query = function () { //查询
            $scope.pager.query();
        };

    });
})(angular);
