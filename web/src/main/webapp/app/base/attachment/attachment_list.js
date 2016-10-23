(function (window, angular, $) {
    var app = angular.module('base.attachment.list', [
        'base.attachment',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    app.controller('Ctrl', function ($window, $scope, CommonUtils, AttachmentService, AlertFactory, ModalFactory) {
        //定义变量

        $scope.status = [
            {name: '全部'},
            {name: '临时', value: 'TEMP'},
            {name: '永久', value: 'ACTIVE'}
        ];

        $scope.size = [
            {name: '全部'},
            {name: '50 KB', value: 1000 * 50},
            {name: '100 KB', value: 1000 * 100},
            {name: '200 KB', value: 1000 * 200},
            {name: '500 KB', value: 1000 * 500},
            {name: '1 MB', value: 1000 * 1000},
            {name: '5 MB', value: 1000 * 5000},
            {name: '10 MB', value: 1000 * 1000 * 10},
            {name: '20 MB', value: 1000 * 1000 * 20},
            {name: '50 MB', value: 1000 * 1000 * 50},
            {name: '100 MB', value: 1000 * 1000 * 100}

        ];
        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, $scope.condition, {start: this.start, limit: this.limit});
                if (param.uploadTimeMin) {
                    param.uploadTimeMin = new Date(param.uploadTimeMin).getTime();
                }
                if (param.uploadTimeMax) {
                    param.uploadTimeMax = new Date(param.uploadTimeMax).getTime();
                }
                return CommonUtils.promise(function (defer) {
                    var result = AttachmentService.pageQuery(param);
                    CommonUtils.loading(result, '加载中...', function (data) {
                        data = data.data || {total: 0};
                        $scope.beans = data;
                        defer.resolve(data);
                    });
                });
            },
            finishInit: function () {
                this.query();
            }
        };


        // 查询
        $scope.query = function () {
            $scope.pager.query();
        };


        // 删除
        $scope.remove = function (id) {
            ModalFactory.remove($scope, function () {
                if (!id) {
                    var items = [];
                    angular.forEach($scope.items, function (v) {
                        items.push(v.id);
                    });
                    id = items.join(',');
                }
                var promise = AttachmentService.deleteByIds({ids: id});
                CommonUtils.loading(promise, '删除中...', function () {
                    AlertFactory.success($scope, null, '删除成功!');
                    $scope.query();
                });
            });
        };

        // 下载
        $scope.download = function (id) {
            window.open(CommonUtils.contextPathURL('/attachment/download?id=' + id), "_blank");
        };


    });
})(window, angular, jQuery);
