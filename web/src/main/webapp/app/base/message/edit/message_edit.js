/**
 *
 */
(function (window, angular, $) {
    var app = angular.module('base.message.edit', [
        'base.message',
        'eccrm.angular',
        'eccrm.angular.ztree',
        'base.emp',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, MessageService, MessageParam, EmpModal) {

        var pageType = $('#pageType').val();
        var id = $('#id').val();

        $scope.back = CommonUtils.back;

        // 消息类型
        $scope.type = [{name: '请选择...'}];
        MessageParam.type(function (data) {
            $scope.type.push.apply($scope.type, data);
        });

        // 初始化富文本编辑器
        var attachmentIds = [];
        // 初始化富文本编辑器
        var editor = KindEditor.create('#content', {
            uploadJson: CommonUtils.contextPathURL('/attachment/upload2?dataType=jsp'),
            afterUpload: function (url, obj) {
                $scope.$apply(function () {
                    attachmentIds.push(obj.id)
                });
            }
        });
        // 保存
        $scope.save = function (createNew) {
            $scope.beans.content = editor.html();
            $scope.beans.attachmentIds = attachmentIds.join(',');
            var promise = MessageService.save($scope.beans, function (data) {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                if (createNew === true) {
                    $scope.beans = {};
                } else {
                    $scope.form.$setValidity('committed', false);
                    CommonUtils.delay($scope.back, 2000);
                }
            });
            CommonUtils.loading(promise, '保存中...');
        };

        // 选择员工
        $scope.pickEmp = function () {
            EmpModal.pickMulti({}, function (o) {
                var ids = [];
                var names = [];
                angular.forEach(o, function (emp) {
                    ids.push(emp.id);
                    names.push(emp.name);
                });
                $scope.beans.receiverId = ids.join(' , ');
                $scope.beans.receiverName = names.join(' , ');
            })
        };

        // 清除员工
        $scope.clearEmp = function () {
            $scope.beans.receiverId = null;
            $scope.beans.receiverName = null;
        };

        $scope.beans = {};

        // 更新
        $scope.update = function () {
            $scope.beans.content = editor.html();
            $scope.beans.attachmentIds = attachmentIds.join(',');
            var promise = MessageService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
                $scope.form.$setValidity('committed', false);
                CommonUtils.addTab('update');
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise, '更新中...');
        };

        // 加载数据
        $scope.load = function (id) {
            var promise = MessageService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
            });
            CommonUtils.loading(promise, 'Loading...');
        };


        if (pageType == 'add') {
            $scope.beans = {};
        } else if (pageType == 'modify') {
            $scope.load(id);
        } else if (pageType == 'detail') {
            $scope.load(id);
            $('input,textarea,select').attr('disabled', 'disabled');
        } else {
            AlertFactory.error($scope, '错误的页面类型');
        }

    });
})(window, angular, jQuery);