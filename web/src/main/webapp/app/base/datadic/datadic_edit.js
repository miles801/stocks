(function (angular, $) {
    var app = angular.module('com.michael.base.datadic.edit', [
        'com.michael.base.datadic',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    app.controller('MainCtrl', function ($scope, CommonUtils, DataDic, DataDicService) {
        // 定义变量
        var items = $scope.items = [];

        // 返回
        $scope.back = CommonUtils.back;

        // 定义值来源
        $scope.sources = DataDic.valueSource();

        // 值类型
        $scope.valueTypes = DataDic.valueType();

        // 操作后的回调
        var callback = function (data) {
            if (data.success) {
                CommonUtils.back();
            }
        };

        // 将属性集合数据转到到数据字典中
        var wrapItems = function () {
            var foo = $scope.beans.items = [];
            angular.forEach(items, function (o) {
                foo.push({
                    label: o.label,
                    fieldName: o.fieldName,
                    valueType: o.valueType,
                    value: o.value,
                    conditionType: (function (types) {
                        var bar = [];
                        for (var key in types) {
                            types[key] === true ? bar.push(key) : null;
                        }
                        return bar.join(',');
                    })(o.conditionType),
                    valueSource: o.valueSource
                });
            });
        };

        // 根据ID加载
        var load = function (id, fn) {
            if (typeof id !== 'string') {
                CommonUtils.errorDialog('加载数据时ID不可为空!');
                return false;
            }
            var promise = DataDicService.get({id: id}, function (data) {
                if (data.success) {
                    $scope.beans = data.data || {};
                    angular.forEach($scope.beans.items || [], function (o) {
                        items.push({
                            label: o.label,
                            fieldName: o.fieldName,
                            valueSource: o.valueSource,
                            valueType: o.valueType,
                            value: o.value,
                            conditionType: (function (foo) {
                                var tmp = {};
                                angular.forEach(foo.split(',') || [], function (bar) {
                                    tmp[bar] = true;
                                });
                                return tmp;
                            })(o.conditionType || '')
                        });
                    });
                    typeof fn === 'function' ? fn(data) : null;
                } else {
                    CommonUtils.errorDialog(data.message);
                }
            });
            CommonUtils.loading(promise, '加载数据...');
        };

        // 保存
        $scope.save = function () {
            wrapItems();
            var promise = DataDicService.save($scope.beans, callback);
            CommonUtils.loading(promise, '保存中...');
        };

        // 更新
        $scope.update = function () {
            wrapItems();
            var promise = DataDicService.update($scope.beans, callback);
            CommonUtils.loading(promise, '更新中...');
        };

        // 添加属性
        $scope.addAttr = function () {
            items.push({valueType: 'string', valueSource: 'input'});
        };

        // 删除属性
        $scope.removeAttr = function (index) {
            items.splice(index, 1);
        };

        // =========================== 执行方法 ===========================
        var pageType = $scope.pageType = $('#pageType').val() || 'add';
        var id = $('#id').val();

        if (pageType == 'modify') {         // 更新页面
            load(id);
        } else if (pageType == 'detail') {  // 查看页面
            load(id, function () {
                $('input,textarea,select').attr('disabled', 'disabled');
            });
        } else if (pageType == 'add') {     // 新增页面
        }

    })
})(angular, jQuery);