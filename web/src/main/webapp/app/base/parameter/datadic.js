/**
 * 直接基于parameter添加的
 */
(function () {
    var app = angular.module("base.param");

    app.service('Params', function (ParameterLoader) {
        return {
            /**
             * 性别
             */
            sex: function (callback) {
                ParameterLoader.loadBusinessParam('BP_SEX', callback);
            },
            /**
             * 职务
             */
            duty: function (callback) {
                ParameterLoader.loadBusinessParam('BP_DUTY', callback);
            }
        }
    });
})();