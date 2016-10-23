/**
 *
 */
(function (window, angular, $) {
    var app = angular.module('base.message.view', [
        'base.message',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, MessageService) {

        var id = $('#id').val();
        if (!id) {
            AlertFactory.error('错误的访问!没有获取到消息的ID!');
            return;
        }
        $scope.back = CommonUtils.back;

        var promise = MessageService.get({id: id}, function (data) {
            $scope.beans = data.data || {};
            $('#content').html($scope.beans.content);
        });
        CommonUtils.loading(promise, 'Loading...');

    });
})(window, angular, jQuery);