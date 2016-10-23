/**
 * Created by miles on 13-11-25.
 */
(function (angular, $) {
    var app = angular.module('base.parameter.type.list', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'base.param',
        'base.param.modal.business',
        'eccrm.directive.ztree'
    ]);
    app.controller('ParameterTypeListController', function ($scope, ModalFactory, BusinessParamType, AlertFactory, BusinessParamTypeModal, ParameterConstant, Debounce, $q) {
        $scope.status = ParameterConstant.getStatus().push({code: 0, name: '全部'});
        $scope.condition = {
            status: 0
        };

        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, {start: this.start, limit: this.limit}, $scope.condition);
                if (param.status == 0) {
                    delete param.status;
                }
                var defer = $q.defer();
                var result = BusinessParamType.query(param);
                AlertFactory.handle($scope, result, function (data) {
                    $scope.parameters = data.data;
                    defer.resolve(data.data);
                });
                return defer;
            }
        };

        //查询数据
        $scope.query = function () {
            ($scope.pager.query || $scope.pager.fetch)();
        };

        $scope.cancelOrDelete = function (id) {
            ModalFactory.remove($scope, function () {
                if (!id) {
                    var items = [];
                    angular.forEach($scope.items, function (v) {
                        items.push(v.id);
                    });
                    if (!items || items.length < 1) {
                        AlertFactory.error($scope, '请选择至少一条数据后再进行删除/注销操作!', '错误操作!');
                        return;
                    }
                    id = items.join(',');
                }
                BusinessParamType.deleteByIds({ids: id}, function (data) {
                    if (data && data.success) {
                        AlertFactory.success($scope, null, '删除成功!');
                        $scope.query();
                        return;
                    }
                    AlertFactory.deleteError($scope, data);
                });
            });
        };

        $scope.add = function () {
            BusinessParamTypeModal.add({scope: $scope}, $scope.query);
        };
        $scope.modify = function (id) {
            BusinessParamTypeModal.modify({id: id, scope: $scope}, $scope.query);
        };
        $scope.view = function (id) {
            BusinessParamTypeModal.view({id: id, scope: $scope});
        };
    })
})
(angular, jQuery);