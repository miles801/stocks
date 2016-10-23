/**
 * 新闻公告已读列表页面
 */
(function (angular, $) {
    var app = angular.module('eccrm.im.news.read', [
        'eccrm.im.news',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, ModalFactory, AlertFactory, NewsService, News) {
        // 公告类型
        $scope.newsTypes = null;
        News.type(function (data) {
            data.unshift({name: '全部'});
            $scope.newsTypes = data;
        });

        //初始化分页信息
        $scope.pager = {
            limit: 50,
            fetch: function () {
                var param = angular.extend({}, $scope.condition, {start: this.start, limit: this.limit});
                return CommonUtils.promise(function (defer) {
                    var promise = NewsService.queryPersonalReadNews(param);
                    CommonUtils.loading(promise, '加载中...', function (data) {
                        data = data.data || {};
                        $scope.beans = data.data || [];
                        defer.resolve(data.total);
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


        // 查看明细
        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看公告',
                url: '/base/news/detail?id=' + id
            });
        };

        // 加入收藏
        $scope.star = function (id) {
            ModalFactory.confirm({scope: $scope, keywords: '加入收藏'}, function () {
                var promise = NewsService.markStar({id: id});
                CommonUtils.loading(promise, '设置中...', function (data) {
                    if (data.success) {
                        AlertFactory.success($scope, '收藏成功!');
                    }
                });
            });
        };

    });
})(angular, jQuery);