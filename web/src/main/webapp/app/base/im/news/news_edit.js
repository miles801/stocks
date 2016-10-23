/**
 * Created by wangsd on 2014-01-14 11:23:09.
 */
(function (angular, $) {
    var app = angular.module('eccrm.im.news.edit', [
        'eccrm.im.news',
        'eccrm.angular',
        'eccrm.angularstrap',
        'base.org',         // 组织机构
        'base.org.position'// 机构岗位
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, News, NewsService, ParameterLoader, Org, OrgPosition) {

        var beans = $scope.beans = {};
        $scope.orgs = [];
        $scope.params = [];
        $scope.positions = [];

        // 初始化富文本编辑器
        var editor = KindEditor.create('#content');

        // 公告类型
        $scope.newsTypes = null;
        News.type(function (data) {
            $scope.newsTypes = data;
            if (data && data.length > 0) {
                beans.category = data[0].value;
            }
        });

        $scope.orgs = [];

        // 获得富文本区内容
        var getContent = function () {
            var content = editor.text();
            if (content) {
                return editor.html();
            }
            return null;
        };

        // 验证合法性
        var validate = function () {
            var receiverType = beans.receiverType;
            if (receiverType == 'ALL') {
                return true;
            } else if (receiverType == 'ORG' && $scope.orgs.length < 1) {
                CommonUtils.errorDialog('请选择机构!');
                return false;
            } else if (receiverType == 'ORG_POSITION' && $scope.positions.length < 1) {
                CommonUtils.errorDialog('请选择机构岗位!');
                return false;
            } else if (receiverType == 'PARAM' && $scope.params.length < 1) {
                CommonUtils.errorDialog('请选择业态!');
                return false;
            }
            return true;
        };

        /**
         * 获得接收者对象
         * @return []
         */
        var getReceiver = function () {
            var receiverType = beans.receiverType;
            var item = [];
            if (receiverType == 'ORG') {
                angular.forEach($scope.orgs || [], function (o) {
                    item.push({receiverType: receiverType, receiverId: o.id, receiverName: o.longName});
                });
            } else if (receiverType == 'ORG_POSITION') {
                angular.forEach($scope.positions || [], function (o) {
                    item.push({
                        receiverType: receiverType,
                        receiverId: o.id,
                        receiverName: o.name,
                        receiverId2: o.orgId,    // 用description字段来保存机构的ID，组合成机构岗位
                        receiverName2: o.orgLongName    // 用description字段来保存机构的ID，组合成机构岗位
                    });
                });
            } else if (receiverType == 'PARAM') {
                angular.forEach($scope.params || [], function (o) {
                    if (o.selected === true) {
                        item.push({receiverType: receiverType, receiverId: o.value, receiverName: o.name});
                    }
                });
            }
            return item;
        };

        // 获得附件
        var getAttachment = function () {
            return $scope.options.getAttachment() || [];
        };

        /**
         * 保存/更新前获得封装好的数据;
         * 如果数据不符合要求，会返回null，否则返回object对象
         */
        var getData = function () {
            var foo = {
                attachmentIds: getAttachment().join(',')
            };
            var bean = angular.extend({}, beans);
            bean.content = editor.html();
            if (validate()) {
                foo.news = bean;
                foo.receivers = getReceiver();
                return foo;
            }
            return null;
        };

        // 保存
        $scope.save = function () {
            var data = getData();
            if (data != null) {
                var promise = NewsService.save(data);
                CommonUtils.loading(promise, '保存中...', function (result) {
                    CommonUtils.successDialog('保存成功!');
                    if (result.success) {
                        $scope.form.$setValidity("saved", false);
                    }
                });
            }
        };

        // 更新
        $scope.update = function () {
            var data = getData();
            if (data != null) {
                var promise = NewsService.update(data);
                CommonUtils.loading(promise, '更新中...', CommonUtils.back);
            }
        };


        $scope.back = function () {
            CommonUtils.back();
        };
        // 选择机构
        Org.tree($scope, 'orgTree', function (node) {
            var orgs = $scope.orgs;
            var length = orgs.length;
            // 去重
            for (var i = 0; i < length; i++) {
                if (orgs[i].id == node.id) {
                    return;
                }
            }
            orgs.push(node);
        });

        $scope.removeOrg = function (index) {
            $scope.orgs.splice(index, 1);
        };
        // 附件
        $scope.options = CommonUtils.defer();
        var fileUpload = {
            maxFile: 20
        };

        // 选择机构岗位
        OrgPosition.tree($scope, 'orgPositionTree', function (node) {
            var org = node.getParentNode();
            node.orgId = position.id;
            node.orgName = position.name;
            node.orgLongName = position.orgLongName;
            $scope.positions.push(node);
        });
        // 选择业态
        ParameterLoader.loadBusinessParam('BP_YETAI', function (data) {
            $scope.params = data || [];
        }, '加载业态...');

        var pageType = $('#pageType').val();

        /**
         * 加载数据
         * @param id ID
         * @param callback 接收数据后的回调
         */
        var load = function (id, callback) {
            var promise = NewsService.get({id: id});
            CommonUtils.loading(promise, '加载中...', function (data) {
                data = data.data || {};
                angular.extend(beans, data);
                editor.html(data.content);

                // 回调
                if (angular.isFunction(callback)) {
                    callback(data);
                }

                // 查询业态

                // 查询机构

                // 查询岗位
            });
        };

        var id = $('#id').val();
        if (pageType == 'add') {
            beans.receiverType = 'ALL'; // 默认选择全部接受
        } else if (pageType == 'modify') {
            load(id);
            // 初始化附件
            fileUpload.bid = id;
        } else if (pageType == 'detail') {
            load(id, function () {
                editor.readonly(true);
                // 标记为已读
                NewsService.markRead({id: id});

                $('input,textarea,select').attr('disabled', 'disabled');
            });
            // 初始化附件
            fileUpload.bid = id;
            fileUpload.readonly = true;
            fileUpload.canDelete = false;
        }
        // 初始化附件
        $scope.options.resolve(fileUpload);
    });
})(angular, jQuery);