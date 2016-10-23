/**
 * 新闻公告相关服务
 */
(function (angular) {
    var app = angular.module("eccrm.im.news", [
        'ngResource',
        'base.param'
    ]);

    /**
     * 新闻公告服务
     */
    function NewsService(CommonUtils, $resource) {
        var query = {method: 'pageQuery', start: '@start', limit: '@limit', orderBy: '@orderBy', reverse: '@reverse'};
        return $resource(CommonUtils.contextPathURL('/base/news/:method'), {}, {

            /**
             * 保存公告
             * @param {object}
             */
            save: {method: 'POST', params: {method: 'save', attachmentIds: '@attachmentIds'}, isArray: false},

            /**
             *  发布公告
             * @param {string} id required
             * @return {object}
             */
            publish: {method: 'POST', params: {method: 'publish', id: '@id'}, isArray: false},

            /**
             * 顶置公告
             * @param {string} id required
             * @return {object}
             */
            markTop: {method: 'POST', params: {method: 'markTop', id: '@id'}, isArray: false},

            /**
             * 将指定的公告加入收藏
             */
            markStar: {method: 'POST', params: {method: 'markStar', id: '@id'}, isArray: false},

            /**
             * 取消收藏
             */
            removeStar: {method: 'POST', params: {method: 'removeStar', id: '@id'}, isArray: false},
            /**
             * 将指定的公告标记为已读
             */
            markRead: {method: 'POST', params: {method: 'markRead', id: '@id'}, isArray: false},

            /**
             * 注销公告
             * @param {string} id required
             * @return {object}
             */
            cancel: {method: 'POST', params: {method: 'cancel', id: '@id'}, isArray: false},

            update: {method: 'POST', params: {method: 'update', attachmentIds: '@attachmentIds'}, isArray: false},

            /**
             * 根据ID删除对象
             * @param {string} ids required,ID字符串，如果有多个，使用英文逗号进行分割
             * @return {object}
             */
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            /**
             * 根据ID查询公告
             * @param {string} id required,公告ID
             * @return {object}
             */
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},


            // 高级查询对象，支持传递一个RoleBo对象进行模糊匹配查询
            // 必须参数：无
            // 可选参数：
            // 返回数据：{success:true,data:{ total:总数据,data:[] }}
            query: {method: 'POST', params: query, isArray: false},//查询出所有的菜单,允许使用json的方式传递参数

            // 查询个人未读新闻公告
            queryPersonalUnreadNews: {
                method: 'POST',
                params: {method: 'personalUnreadNews', start: '@start', limit: '@limit'},
                isArray: false
            },

            // 查询个人已读新闻公告
            queryPersonalReadNews: {
                method: 'POST',
                params: {method: 'personalReadNews', start: '@start', limit: '@limit'},
                isArray: false
            },
            // 查询个人加入收藏的新闻公告
            queryPersonalStarNews: {
                method: 'POST',
                params: {method: 'personalStarNews', start: '@start', limit: '@limit'},
                isArray: false
            }
        })
    }

    // 注册服务
    app.service('NewsService', NewsService);

    app.service('News', function (CommonUtils, ParameterLoader) {
        return {
            /**
             * 业务参数，公告类型
             * @param callback 异步查询后的结果毁掉，该函数接收一个参数，该参数为数组
             */
            type: function (callback) {
                ParameterLoader.loadBusinessParam('GG_TYPE', callback, '加载公告类型...');
            },
            /**
             * 获得新闻公告状态
             * @param callback
             */
            state: function (callback) {
                ParameterLoader.loadSysParam('SP_NEWS_STATE', callback, '加载公告状态...');
            }
        };
    });


})(angular);
