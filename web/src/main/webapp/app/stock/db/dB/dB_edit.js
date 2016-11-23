/**
 * 数据库编辑
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('stock.db.dB.edit', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'stock.db.dB'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, DBService, DBParam) {

        var pageType = $('#pageType').val();
        var id = $('#id').val();

        $scope.back = CommonUtils.back;


        // 参数：类型
        $scope.types = [{name: '请选择...'}];
        DBParam.type(function (o) {
            $scope.types.push.apply($scope.types, o);
        });


        // 保存
        $scope.save = function (createNew) {
            if ($scope.form.$invalid) {
                return;
            }
            var promise = DBService.save($scope.beans, function (data) {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                if (createNew === true) {
                    $scope.beans.dbDate = null;
                    $scope.beans.dbDate2 = null;
                } else {
                    $scope.form.$setValidity('committed', false);
                    CommonUtils.delay($scope.back, 2000);
                }
            });
            CommonUtils.loading(promise);
        };


        // 更新
        $scope.update = function () {
            var promise = DBService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
                $scope.form.$setValidity('committed', false);
                CommonUtils.addTab('update');
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise, '更新中...');
        };

        // 加载数据
        $scope.load = function (id, callback) {
            var promise = DBService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
                callback && callback();
            });
            CommonUtils.loading(promise, 'Loading...');
        };

        $('body').bind('keydown', function (e) {
            var keyCode = e.which || e.keyCode;
            if (keyCode == 13) {
                $scope.save(true);
                e.preventDefault();
            }
        });

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