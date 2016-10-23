/**
 * 新闻公告列表页面
 */
(function (angular, $) {
    var app = angular.module('eccrm.im.news.unread', [
        'eccrm.im.news',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, ModalFactory, AlertFactory, NewsService, News) {

        // 查询
        $scope.query = function () {
            var promise = NewsService.queryPersonalUnreadNews({start: 0, limit: 10});
            CommonUtils.loading(promise, '加载中...', function (data) {
                data = data.data || {};
                $scope.beans = data.data || [];
            });
        };

        // 加入收藏
        $scope.star = function (foo) {
            ModalFactory.confirm({scope: $scope, keywords: '加入收藏'}, function () {
                var promise = NewsService.markStar({id: foo.id});
                CommonUtils.loading(promise, '设置中...', function (data) {
                    if (data.success) {
                        foo.hasStar = true;
                        AlertFactory.success($scope, '收藏成功!');
                    }
                });
            });
        };

        $scope.query();

    });
})(angular, jQuery);