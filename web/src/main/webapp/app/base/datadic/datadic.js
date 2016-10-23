/**
 * 数据字典
 */
(function (angular) {
    var app = angular.module("com.michael.base.datadic", [
        'ngResource',
        'base.param',
        'eccrm.angular'
    ]);
    app.service('DataDicService', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL("base/datadic/:method"), {}, {

            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 批量删除/注销
            //返回：{success:true}
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            // 分页查询
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', start: '@start', limit: '@limit'},
                isArray: false
            },

            // 根据数据字典编号查询字典信息（包含字典项）
            findByCode: {method: 'GET', params: {method: 'findByCode', code: '@code'}, isArray: false},//根据id查询对象

            // 根据ID查询对象的信息
            // 返回{data:{}}
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},//根据id查询对象

            // 判断指定编号是否重复,,code需要使用encodeURI()进行两次编码
            hasCode: {method: 'GET', params: {method: 'hasCode', code: '@code'}, isArray: false}

        })
    });


    app.service('DataDic', function (CommonUtils, ParameterLoader) {
        return {
            /**
             * 获得可选的条件类型
             * @returns {Array}
             */
            conditionType: function () {
                return [
                    {name: '=', value: 'eq'},
                    {name: 'like', value: 'like'},
                    {name: '>', value: 'gt'},
                    {name: '>=', value: 'ge'},
                    {name: '<', value: 'lt'},
                    {name: '<=', value: 'le'},
                    {name: '!=', value: 'ne'},
                    {name: 'in', value: 'in'},
                    {name: 'NULL', value: 'null'},
                    {name: 'NOT NULL', value: 'notnull'}
                ];
            },
            /**
             * 获得值来源数组
             * @returns {Array}
             */
            valueSource: function () {
                return [
                    {name: '输入', value: 'input'},
                    {name: '系统参数', value: 'sys'},
                    {name: '业务参数', value: 'busi'},
                    {name: '时间', value: 'date'},
                    {name: '员工', value: 'employee'},
                    {name: '用户', value: 'user'},
                    {name: '机构', value: 'org'},
                    {name: '岗位', value: 'position'},
                    {name: '业态', value: 'param'}
                ];
            },
            /**
             * 获得值类型
             * @returns {Array}
             */
            valueType: function () {
                return [
                    {name: '字符串', value: 'string'},
                    {name: '整数', value: 'int'},
                    {name: '小数', value: 'double'},
                    {name: '时间', value: 'date'},
                    {name: 'Boolean', value: 'boolean'}
                ];
            }

        };
    });

    // 数据字典插件
    // 该插件接收一个配置对象，初始化完成后会返回这个对象，并给这个对象添加相应的操作方法
    app.directive('eccrmDatadic', function ($injector, CommonUtils, DataDic, DataDicService, ParameterLoader) {
        var defaults = {
            code: null,    // 必须
            type: 'add'     // 类型，add（新增）、modify（更改）、view（查看）
        };
        return {
            templateUrl: CommonUtils.contextPathURL('/app/base/datadic/template/datadic-template.html'),
            scope: {
                options: '=eccrmDatadic'
            },
            link: function (scope, elem, attr, ctrl) {
                // 需要编号
                var promise = CommonUtils.parseToPromise(scope.options);
                if (!promise || typeof promise.then !== 'function') {
                    CommonUtils.errorDialog("初始化eccrm-datadic插件失败!配置项错误!仅支持接收一个object对象!");
                    return false;
                }
                // 条件
                scope.items = [];

                /**
                 *
                 * @param {string} moduleName [required] 模块名称
                 * @param {string} message 加载失败后的提示信息
                 * @returns {boolean}
                 */
                // TODO 待完善
                var loadModule = function (moduleName, message) {
                    try {
                        angular.module(moduleName);
                    } catch (e) {
                        alert(message);
                        return false;
                    }
                    return true;
                };


                var operate = {
                    add: function () {
                        this.items.push({});
                    },
                    remove: function (index) {
                        this.items.splice(index, 1);
                    },
                    addAndGroup: function () {
                        this.items.push({or: true})
                    },
                    addOrGroup: function () {
                        this.items.push({and: true});
                    },
                    addAnd: function (index) {
                        var o = {operate: 'AND', show: true, disabled: false, fieldName: scope.dic.items[0].fieldName};
                        this.items.push(o);
                        this.change(o);
                    },
                    addOr: function (index) {
                        var o = {operate: 'OR', show: true, disabled: false, fieldName: scope.dic.items[0].fieldName};
                        this.items.push(o);
                        this.change(o);
                    },
                    // 当选择的条件发生变化时，重置后面的选项
                    change: function (item) {
                        // 获得所有的条件
                        angular.forEach(this.dic.items || [], function (o) {
                            if (o.fieldName === item.fieldName) {
                                delete item.conditionTypes;
                                item.conditionTypes = [];
                                var conditionTypes = DataDic.conditionType();
                                angular.forEach(o.conditionType.split(',') || [], function (foo) {
                                    for (var i = 0; i < conditionTypes.length; i++) {
                                        if (conditionTypes[i].value == foo) {
                                            item.conditionTypes.push(conditionTypes[i]);
                                        }
                                    }
                                });
                                item.conditionType = item.conditionTypes[0].value;
                                item.valueSource = o.valueSource;
                                item.valueType = o.valueType;
                                // 是参数
                                item.params = null;
                                if (o.valueSource === 'busi') {
                                    ParameterLoader.loadBusinessParam(o.value, function (data) {
                                        item.params = data;
                                    });
                                } else if (o.valueSource === 'sys') {
                                    ParameterLoader.loadSysParam(o.value, function (data) {
                                        item.params = data;
                                    });
                                }

                                return false;
                            }
                        });
                    },
                    // 选择员工
                    pick: function (o) {
                    },

                    // 选择组织机构
                    pickOrg: function () {

                    },

                    // 选择机构下的岗位
                    pickOrgPosition: function () {
                    },

                    // 选择业态
                    pickYeTai: function () {

                    },

                    // 选择系统参数
                    pickSysParam: function () {

                    },

                    // 选择业务参数
                    pickBusiParam: function () {

                    }

                };

                angular.extend(scope, operate);

                var init = function (options) {

                    var code = options.code;
                    if (typeof code !== 'string') {
                        CommonUtils.errorDialog('初始化eccrm-datadic插件失败!没有配置编号(code)');
                        return false;
                    }
                    // 加载数据
                    var defer = DataDicService.findByCode({code: code});
                    CommonUtils.loading(defer, '加载中...', function (data) {
                        if (!data.data) {
                            CommonUtils.errorDialog('数据字典组件初始化失败! 编号不存在或者数据未配置, 字典编号[' + code + ']');
                        }
                        scope.dic = data.data || {};
                    });


                    // 添加新的方法,返回得到的结果
                    if (typeof options.getResult == 'undefined') {
                        /**
                         *  获得用户的操作结果
                         * @returns {Object}
                         */
                        options.getResult = function () {
                            // 只返回必要的属性
                            var conditions = [];
                            var o = {conditions: conditions};
                            var dic = scope.dic;
                            o.id = dic.id;
                            o.name = dic.name;
                            o.className = dic.className;
                            o.code = dic.code;
                            var getValue = function (bean) {
                                var conditionType = bean.conditionType;
                                if (conditionType == 'in') {
                                    var data = [];
                                    angular.forEach(bean.params || [], function (foo) {
                                        if (foo.checked === true) {
                                            data.push(foo.value);
                                        }
                                    });
                                    return data.join(',');
                                } else {
                                    return bean.value;
                                }
                            };
                            angular.forEach(scope.items, function (foo) {
                                // 如果任意值未设置，则跳过
                                var value = getValue(foo);
                                if (!(foo.conditionType && foo.valueType && value && foo.operate && foo.fieldName && foo.valueSource)) {
                                    return;
                                }
                                conditions.push({
                                    conditionType: foo.conditionType,
                                    value: value,
                                    valueType: foo.valueType,
                                    operate: foo.operate,
                                    fieldName: foo.fieldName,
                                    valueSource: foo.valueSource
                                });
                            });
                            // 如果没有设置条件，则返回空
                            if (conditions.length < 1) {
                                conditions = null;
                                o = null;
                            }
                            return o;
                        };
                    }
                    if (typeof options.reload === 'undefined') {
                        /**
                         *重新初始化插件，将会清空之前所有的操作数据
                         * @param {Object} newOptions [optional] 重新加载插件的新的配置项
                         */
                        options.reload = function (newOptions) {
                            scope.items = [];
                            init(newOptions || this);
                        };
                    }

                    if (typeof options.add === 'undefined') {
                        /**
                         *用于直接往插件里添加一条记录
                         * 可配置属性：
                         * disabled {boolean=false} 是否禁止操作
                         * show {boolean=true} 是否显示
                         * @param {Object} item 一个条件配置对象
                         *
                         */
                        options.add = function (item) {
                            item = item || {};
                            scope.items.push(item);
                        }
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
    })
})
(angular);
