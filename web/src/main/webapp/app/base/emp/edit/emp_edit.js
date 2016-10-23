/**
 * Created by Michael on 2016-04-07 09:06:28.
 */
(function (window, angular, $) {
    var app = angular.module('base.emp.edit', [
        'base.emp',
        'eccrm.angular',
        'eccrm.angular.ztree',
        'base.org',
        'base.position',
        'eccrm.angularstrap'
    ]);

    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory, EmpService, ParameterLoader, OrgTree, PositionModal) {

        var pageType = $scope.pageType = $('#pageType').val();
        var id = $('#id').val();

        $scope.back = CommonUtils.back;

        // 性别
        $scope.sex = [{name: '请选择...'}];
        ParameterLoader.loadBusinessParam('BP_SEX', function (data) {
            $scope.sex.push.apply($scope.sex, data)
        });

        // 头像
        $scope.uploadOptions = {
            labelText: '头像',
            maxFile: 1,
            thumb: true,
            thumbWidth: 120,
            thumbHeight: 140,
            showTable: false,
            onSuccess: function (o) {
                var id = o.id;
                $('#imageId').html('<img style="height: 140px;width: 120px;" src="' + CommonUtils.contextPathURL('/attachment/temp/view?id=' + id) + '"/>');
                $scope.$apply(function () {
                    $scope.beans.attachmentIds = id;
                    $scope.beans.icon = id;
                });
            },

            onDelete: function () {
                $('#imageId').html('');
                $scope.beans.attachmentIds = null;
                $scope.beans.icon = null;
            },
            bid: id,
            swfOption: {
                fileSizeLimit: 10 * 1000 * 1000,
                fileTypeExts: "*.png;*.jpg"
            }
        };

        // 保存
        $scope.save = function (createNew) {
            var promise = EmpService.save($scope.beans, function (data) {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                if (createNew === true) {
                    $scope.beans.name = null;
                    $scope.beans.code = null;
                    $scope.beans.loginName = null;
                    $scope.beans.sequenceNO = ($scope.beans.sequenceNO || 0) + 1;
                } else {
                    $scope.form.$setValidity('committed', false);
                    CommonUtils.delay($scope.back, 2000);
                }
            });
            CommonUtils.loading(promise, '保存中...');
        };

        $scope.orgTree = OrgTree.pick(function (o) {
            $scope.beans.orgId = o.id;
            $scope.beans.orgName = o.name;
        });

        $scope.beans = {};

        // 选择岗位
        $scope.pickPosition = function () {
            // 获取已保存的岗位信息
            var beans = $scope.beans;
            var ids = (beans.roleIds || '').split(',');
            var names = (beans.roleNames || '').split(',');
            var condition = {
                path: ($('#positionIds').val() || '').split(',')[0]
            };
            // 选择岗位
            PositionModal.pickMulti({ids: ids, names: names, condition: condition}, function (ps) {
                var pIds = [];
                var pNames = [];
                angular.forEach(ps || [], function (p) {
                    pIds.push(p.id);
                    pNames.push(p.name);
                });
                beans.roleNames = pNames.join(",");
                beans.roles = pIds.join(",");
            });
        };

        // 清除岗位
        $scope.clearPosition = function () {
            $scope.beans.roleNames = null;
            $scope.beans.roles = null;
        };

        /**
         * 清除机构信息
         */
        $scope.clearOrg = function () {
            $scope.beans.orgId = null;
            $scope.beans.orgName = null;
        };


        // 更新
        $scope.update = function () {
            var promise = EmpService.update($scope.beans, function (data) {
                AlertFactory.success('更新成功!');
                $scope.form.$setValidity('committed', false);
                CommonUtils.addTab('update');
                CommonUtils.delay($scope.back, 2000);
            });
            CommonUtils.loading(promise, '更新中...');
        };

        // 加载数据
        $scope.load = function (id) {
            var promise = EmpService.get({id: id}, function (data) {
                $scope.beans = data.data || {};
                $scope.beans.roles = $scope.beans.roleIds;
                // 头像
                var imageId = $scope.beans.icon;
                if (imageId) {
                    $('#imageId').html('<img style="height: 140px;width: 120px;" src="' + CommonUtils.contextPathURL('/attachment/view?id=' + imageId) + '"/>');
                }
            });
            CommonUtils.loading(promise, 'Loading...');
        };

        $scope.removePicture = function () {
            $scope.beans.icon = null;
            $scope.uploadOptions.removeAll();
            $('#imageId').html('');
        };


        if (pageType == 'add') {
            $scope.beans = {
                locked: 0,
                lockedName: '正常'
            };
        } else if (pageType == 'modify') {
            $scope.load(id);
        } else if (pageType == 'detail') {
            $scope.load(id);
            $('input,textarea,select', 'form').attr('disabled', 'disabled');
            $('.add-on i.icons.icon', 'form').remove();
        } else {
            AlertFactory.error($scope, '错误的页面类型');
        }

    });
})(window, angular, jQuery);