/**
 * 新闻公告列表页面
 */
(function (angular, $) {
    var app = angular.module('eccrm.im.news.list', [
        'eccrm.im.news',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, ModalFactory, AlertFactory, NewsService, News) {
        // 状态
        // 类型
        // 公告类型
        $scope.newsTypes = null;
        News.type(function (data) {
            data.unshift({name: '全部'});
            $scope.newsTypes = data;
        });

        News.state(function (data) {
            data.unshift({name: '全部'});
            $scope.states = data;
        });
        //初始化分页信息
        $scope.pager = {
            fetch: function () {

                var param = angular.extend({}, $scope.condition, {start: this.start, limit: this.limit});
                return CommonUtils.promise(function (defer) {
                    var promise = NewsService.query(param);
                    CommonUtils.loading(promise, '加载中...', function (data) {
                        data = data.data || {};
                        var total = data.total || 0;
                        $scope.beans = data.data || [];
                        defer.resolve(total);
                    });
                });
            },
            finishInit: function () {
                this.query();
            }
        };

        $scope.query = function () {
            $scope.pager.query();
        };

        // 发布
        $scope.publish = function (news) {
            if (news.publishState !== 'UNPUBLISHED') {
                CommonUtils.errorDialog('只有未发布的公告才可以发布!');
                return false;
            }

            ModalFactory.confirm({scope: $scope, content: '公告发布后公告将不可以再次编辑,请确认!'}, function () {
                var promise = NewsService.publish({id: news.id});
                CommonUtils.loading(promise, '发布中...', $scope.query);
            });
        };

        // 顶置
        $scope.setTop = function (id) {
            var promise = NewsService.markTop({id: id});
            CommonUtils.loading(promise, '设置中...', $scope.query);
        };

        // 编辑
        $scope.modify = function (news) {
            if (news.publishState !== 'UNPUBLISHED') {
                CommonUtils.errorDialog('只有未发布的公告才可以编辑!');
                return false;
            }
            CommonUtils.addTab({
                title: '更新公告',
                url: '/base/news/modify?id=' + news.id,
                onUpdate: $scope.query
            });
        };

        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看公告',
                url: '/base/news/detail?id=' + id
            });
        };

        // TODO 阅读记录

        // 关闭
        $scope.close = function (id) {
            var promise = NewsService.cancel({id: id});
            CommonUtils.loading(promise, '设置中...', $scope.query);
        };

    });
})(angular, jQuery);