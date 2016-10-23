/**
 * 数据字典
 */
(function (angular) {
    var app = angular.module("com.michael.base.import", [
        'eccrm.angular'
    ]);


    // 数据字典插件
    // 该插件接收一个配置对象，初始化完成后会返回这个对象，并给这个对象添加相应的操作方法
    app.directive('eccrmImport', function (CommonUtils, ParameterLoader) {
        var defaults = {
            targetClass: '',// 必须
            code: ''         // 编号
        };
        return {
            templateUrl: CommonUtils.contextPathURL('/app/base/import/template/imp-template.html'),
            scope: {
                options: '=eccrmImport'
            },
            link: function (scope, elem, attr, ctrl) {
                var promise = CommonUtils.parseToPromise(scope.options);
                if (!promise || typeof promise.then !== 'function') {
                    CommonUtils.errorDialog("初始化eccrm-import插件失败!配置项错误!仅支持接收一个object对象!");
                    return false;
                }

                var init = function (options) {

                    var targetClass = options.targetClass;
                    if (!targetClass) {
                        CommonUtils.errorDialog('初始化导入组件失败!缺少配置[targetClass]');
                        return false;
                    }
                    var code = options.code;
                    if (!code) {
                        CommonUtils.errorDialog('初始化导入组件失败!缺少配置[code]');
                        return false;
                    }


                    var beans = scope.beans = {startRow: 1, targetClass: targetClass};
                    ParameterLoader.loadBusinessParam(code, function (data) {
                        if (angular.isArray(data) && data.length > 0) {
                            scope.fields = data;
                        } else {
                            CommonUtils.errorDialog('初始化导入组件失败!错误的配置项code[' + code + '],没有查找到相关的配置!');
                        }
                    });

                    var mappings = beans.mappings = scope.mappings = [];
                    scope.add = function () {
                        mappings.push({index: mappings.length, colName: scope.fields[0].value});
                    };

                    scope.remove = function (index) {
                        mappings.splice(index, 1);
                    };

                    if (typeof options.reload !== 'function') {
                        options.reload = function (args) {
                            init(args || options);
                        };
                    }

                    if (typeof options.get !== 'function') {
                        options.get = function () {
                            return beans;
                        };
                    }


                    // 回调
                    if (angular.isFunction(options.callback)) {
                        options.callback.call(options);
                    }
                };

                // 初始化
                promise.then(init);
            }
        };
    });
})
(angular);
