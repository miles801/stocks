/**
 * 登录日志编辑
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('base.log.loginLog.edit', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'base.log.loginLog'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, LoginLogService, LoginLogParam) {

        var pageType = $('#pageType').val();
        var id = $('#id').val();

        $scope.back = CommonUtils.back;


        // 参数：退出方式
        $scope.types = [{name: '请选择...'}];
        LoginLogParam.type(function (o) {
            $scope.types.push.apply($scope.types, o);
        });


        // 保存
        $scope.save = function (createNew) {
            var promise = LoginLogService.save($scope.beans, function (data) {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                if (createNew === true) {
                    $scope.beans = {};
                } else {
                    $scope.form.$setValidity('committed', false);
                    CommonUtils.delay($scope.back, 2000);
                }
            });
            CommonUtils.loading(promise);
        };


        // 更新
        $scope.update = function () {
            var promise = LoginLogService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
                $scope.form.$setValidity('committed', false);
                CommonUtils.addTab('update');
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise, '更新中...');
        };

        // 加载数据
        $scope.load = function (id, callback) {
            var promise = LoginLogService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
                callback && callback();
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
            $('span.add-on>.icons').remove();
        } else {
            AlertFactory.error($scope, '错误的页面类型');
        }
    });
})(window, angular, jQuery);