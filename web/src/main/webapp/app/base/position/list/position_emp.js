(function (window, angular, $) {

    var app = angular.module('base.position.emp', [
        'base.position',
        'eccrm.angular',
        'eccrm.angular.ztree',
        'base.emp',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, PositionService, PositionTree, EmpModal) {

        // 用于保存当前被点击的组织机构的数据
        $scope.position = {};

        // 初始化树
        var initTree = function () {
            PositionTree.validTree({
                scope: $scope, id: 'tree', showEmpCounts: true, onClick: function (o) {
                    $scope.position = o;
                    $scope.query();
                }
            });
        };

        $scope.query = function () {
            $scope.pager.query();
        };


        $scope.pager = {
            fetch: function () {
                var param = angular.extend({positionId: $scope.position.id}, {start: this.start, limit: this.limit});
                if (!param.positionId) return;
                return CommonUtils.promise(function (defer) {
                    var promise = PositionService.queryEmp(param, function (data) {
                        data = data.data;
                        $scope.beans = data;
                        defer.resolve(data);
                    });
                    CommonUtils.loading(promise);
                });
            }
        };


        /**
         * 给岗位添加员工
         */
        $scope.addEmp = function () {
            EmpModal.pickMulti({}, function (o) {
                if ($scope.position.maxEmp < $scope.position.empCounts + o.length) {
                    AlertFactory.error('超出该岗位允许的最大员工数量，请重新添加!');
                    return;
                }
                var empIds = [];
                angular.forEach(o, function (emp) {
                    empIds.push(emp.id);
                });
                ModalFactory.confirm({
                    scope: $scope,
                    content: "是否要将选中的" + o.length + "个员工添加到[" + $scope.position.name + "下]?请确认!",
                    callback: function () {
                        var promise = PositionService.addEmp({
                            positionId: $scope.position.id,
                            empIds: empIds.join(',')
                        }, function () {
                            AlertFactory.success('添加成功!');
                            $scope.pager.load();
                        });
                        CommonUtils.loading(promise);
                    }

                });
            });
        };

        /**
         * 移除岗位中的员工
         */
        $scope.removeEmp = function (empId) {
            if (!empId) {
                var empIds = [];
                angular.forEach($scope.items, function (o) {
                    empIds.push(o.id);
                });
                empId = empIds.join(',');
            }
            if (!empId) {
                AlertFactory.warning('请选择要移除的员工!');
                return;
            }
            ModalFactory.confirm({
                scope: $scope,
                content: '是否将选中的员工从当前的组织机构中移除，请确认?',
                callback: function () {
                    var promise = PositionService.removeEmp({
                        positionId: $scope.position.id,
                        empIds: empId
                    }, function () {
                        AlertFactory.success('操作成功!');
                        $scope.pager.load();
                    });
                    CommonUtils.loading(promise);
                }
            });
        };


        $scope.view = function (id) {
            CommonUtils.addTab({
                title: '查看员工',
                url: '/base/emp/detail?id=' + id
            })
        };

        initTree();

    });
})(window, angular, jQuery);