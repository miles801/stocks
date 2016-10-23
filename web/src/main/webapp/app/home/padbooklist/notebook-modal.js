/**
 * Created by miles on 14-3-12.
 */
(function (angular) {
    var app = angular.module('eccrm.home.modal', ['mgcrea.ngStrap', 'eccrm.home.padbooklist', 'eccrm.angular', 'eccrm.angularstrap']);
    // 公共的模态对话框服务
    app.factory('NoteBookModal', function ($modal, NoteBookService, AlertFactory, ModalFactory, $filter,CommonUtils) {
        return {
            add: function (cfg, callback) {
                var defaults = {
                    scope: null,
                    callback: null,// 点击确定后要执行的函数
                    afterShown: null// 模态对话框显示完成后要执行的函数
                }
                cfg = angular.extend({}, defaults, cfg);
                var scope = cfg.scope;
                if (!scope) throw '使用模态对话框时必须指定scope!';
                var modal = $modal({backdrop: 'static', scope: scope, template: CommonUtils.contextPathURL('/app/home/padbooklist/edit/padbooklist_add.tpl.html')});
                var context = window.top.getCrmContext() || {};
                var foo = {
                    createdDatetime: new Date().getTime(),
                    createdUser: {
                        username: context.getUsername(),
                        id: context.getUserId()
                    }
                };
                var that = this;
                callback = callback || cfg.callback;
                modal.$scope.notebook = angular.extend({}, foo);
                modal.$scope.save = function () {
                    NoteBookService.save(modal.$scope.notebook, function (data) {
                        if (data && data.success) {
                            modal.$scope.$hide();
                            modal.$scope.query();
                        } else {
                            AlertFactory.error(modal.$scope, '[' + (data && data.error || data.fail || '') + ']', '保存失败!');
                        }
                    });
                };

                modal.$scope.type = 'add';
                var that = this;
                callback = callback || cfg.callback;
                modal.$promise.then(function () {
                    var foo = setInterval(function () {
                        if (modal.$scope.$isShown) {
                            clearInterval(foo);
                            if (cfg.afterShown && angular.isFunction(cfg.afterShown)) {
                                cfg.afterShown.call(that);
                            }
                        }
                    }, 50);
                });
            },
            // 编辑
            modify: function (cfg, callback) {
                var defaults = {
                    id: null,// 必填项
                    scope: null,
                    callback: null,// 点击确定后要执行的函数
                    afterShown: null// 模态对话框显示完成后要执行的函数
                }
                cfg = angular.extend({}, defaults, cfg);
                var scope = cfg.scope;
                if (!scope) throw '使用模态对话框时必须指定scope!';
                var id = cfg.id;
                if (!scope) throw '查看指定用户组信息时,没有获得用户组的ID!';
                callback = callback || cfg.callback;
                var modal = $modal({backdrop: 'static', scope: scope, template: 'app/home/padbooklist/edit/padbooklist_edit.tpl.html'});
                NoteBookService.get({id: id}, function (data) {
                    modal.$scope.notebook = data;
                    //console.dir(data)

                });
                var that = this;
                modal.$scope.update = function () {
                    NoteBookService.update(modal.$scope.notebook, function (data) {
                        if (data && data.success) {
                            if (callback && angular.isFunction(callback)) {
                                callback.call(that, arguments);
                            }
                            modal.$scope.$hide();
                            modal.$scope.query();
                            AlertFactory.success(modal.$scope, null, '更新成功!');
                        } else {
                            AlertFactory.error(modal.$scope, '[' + (data.error || data.fail || '') + ']', '更新失败!');
                        }
                    });
                };
                modal.$scope.remove = function (id) {
                    ModalFactory.remove(modal.$scope, function (data) {
                        NoteBookService.deleteByIds({ids: id}, function (data) {
                            //console.dir(data);
                            if (data && data.success) {
                                modal.$scope.$hide();
                                modal.$scope.query();
                                AlertFactory.success(modal.$scope, null, '删除成功!');
                            } else {
                                AlertFactory.error(modal.$scope, '[' + (data && data.error || data.fail || '') + ']', '保存失败!');
                            }
                        });
                    });
                };

                modal.$scope.chooseParent = function () {
                    that.single({scope: scope, value: modal.$scope.usergroup.parent, exclude: [cfg.id]}, function (data) {
                        modal.$scope.usergroup.parent = data;
                    });
                }
                modal.$scope.type = 'modify';
                if (callback && angular.isFunction(callback)) {
                }
            }

        }
    });
})(angular);