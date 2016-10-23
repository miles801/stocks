/**
 * Created by Michael on 2016-04-29 14:09:31.
 */
(function (window, angular, $) {
    var app = angular.module('base.message.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'base.message'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, MessageService, MessageParam) {
        $scope.condition = {};

        //查询数据
        $scope.query = function () {
            $scope.pager.query();
        };

        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                $scope.beans = [];
                return CommonUtils.promise(function (defer) {
                    var promise = MessageService.pageQuery(param, function (data) {
                        param = null;
                        $scope.beans = data.data || {total: 0};
                        defer.resolve($scope.beans);
                    });
                    CommonUtils.loading(promise, 'Loading...');
                });
            },
            finishInit: function () {
                this.query();
            }
        };

        // 删除或批量删除
        $scope.remove = function (id) {
            if (!id) {
                var ids = [];
                angular.forEach($scope.items, function (o) {
                    ids.push(o.id);
                });
                id = ids.join(',');
            }
            if (!id) {
                AlertFactory.warning('请选择要删除的消息!');
                return;
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '数据一旦删除将不可恢复，请确认!',
                callback: function () {
                    var promise = MessageService.deleteByIds({ids: id}, function () {
                        AlertFactory.success('删除成功!');
                        $scope.query();
                    });
                    CommonUtils.loading((promise));
                }
            });
        };

        // 新增
        $scope.add = function () {
            CommonUtils.addTab({
                title: '新增消息',
                url: '/base/message/add',
                onUpdate: $scope.query
            });
        };

        // 更新
        $scope.modify = function (id) {
            CommonUtils.addTab({
                title: '更新消息',
                url: '/base/message/modify?id=' + id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看消息',
                url: '/base/message/view?id=' + id
            });
        }
    });
})(window, angular, jQuery);