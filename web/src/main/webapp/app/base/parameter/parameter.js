/**
 * Created by miles on 13-11-25.
 */
(function (angular) {
    var app = angular.module("base.param", [
        'ngResource',
        'eccrm.angular'
    ]);

    app.service('Parameter', ['SysParamType', 'BusinessParamType', 'SysParamItem', 'BusinessParamItem', function (SysParamType, BusinessParamType, SysParamItem, BusinessParamItem) {
        return {
            // 根据基础参数类型编号获得对应的基础参数
            // 参数type(必须):基础参数类型编号
            // 参数callback（必须）：查询出数据后的回调，将会把查询出的数据注入到函数中
            systemItems: function (type, callback) {
                return SysParamItem.validItems({type: type}, callback);
            },

            // 根据业务参数类型编号获得对应的业务参数
            // 参数type(必须):业务参数类型编号
            // 参数callback（必须）：查询出数据后的回调，将会把查询出的数据注入到函数中
            businessItems: function (type, callback) {
                return BusinessParamItem.validItems({type: type}, callback);
            },

            // 查询基础参数的级联参数
            // 参数1（必须）：cascadeType <string>：当前类型编号
            // 参数2（必须）：cascadeValue <string>：当前值
            // 参数3（必须）：callback <function> ：成功执行后的回调
            fetchSystemCascade: function (cascadeType, cascadeValue, callback) {
                return SysParamItem.fetchCascade({type: cascadeType, value: cascadeValue}, callback);
            },

            // 查询业务参数的级联参数
            // 参数1（必须）：cascadeType <string>：当前类型编号
            // 参数2（必须）：cascadeValue <string>：当前值
            // 参数3（必须）：callback <function> ：成功执行后的回调
            fetchBusinessCascade: function (cascadeType, cascadeValue, callback) {
                return BusinessParamItem.fetchCascade({type: cascadeType, value: cascadeValue}, callback);
            },

            // 获得通用参数的状态
            // 参数callback（必须）：查询出状态列表后要执行的操作
            status: function (callback) {
                this.systemItems('SP_COMMON_STATE', callback);
            }
        };

    }]);
    // 基础参数类型
    app.service('SysParamType', ['$resource', 'CommonUtils', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('base/parameter/type/system/:method'), {}, {
            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 批量删除
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},
            // 启用
            enable: {method: 'POST', params: {method: 'enable', ids: '@ids'}, isArray: false},
            // 禁用
            disable: {method: 'POST', params: {method: 'disable', ids: '@ids'}, isArray: false},

            //根据id查询参数信息
            get: {method: 'GET', params: {id: '@id', method: 'get'}, isArray: false},

            //分页查询，返回{total:,data:''}
            query: {method: 'POST', params: {method: 'query', start: '@start', limit: '@limit'}, isArray: false},

            // 查询所有的类型并组装成树
            tree: {method: 'GET', params: {method: 'tree'}, isArray: false},

            // 查询指定节点的所有子节点(包含自己）
            // id（必须）:当前节点id
            children: {method: 'GET', params: {method: 'children', id: '@id'}, isArray: false},

            //查询除当前节点（包括子节点）外的所有节点组成的树形数据
            // cid（可选）:当前节点的id
            queryOther: {method: 'GET', params: {method: 'other'}, isArray: false},

            // 查询所有状态为启用的参数类型，返回树形数据
            // 用于：参数编辑页面选择级联参数
            queryValid: {method: 'GET', params: {method: 'queryValid'}, isArray: false},

            // 查询所有启用、已注销状态的参数类型，返回树形数据
            // 用于：参数页面的左侧树
            queryUsing: {method: 'GET', params: {method: 'queryUsing'}, isArray: false},

            //判断指定编号是否重复
            // code(必须）:使用URIEncode进行编码的编号
            hasCode: {method: 'GET', params: {method: 'hasCode', code: '@code'}, isArray: false},

            //判断在指定层级下的名称是否重复：需要参数{id:'',name:''}
            // id：上级节点的id
            // name（必须）:当前节点的名称，使用URIEncode编码
            hasName: {method: 'GET', params: {method: 'hasName', id: '@id', name: '@name'}, isArray: false},

            // 根据类型编号获得类型名称
            // 参数code（必须）：参数类型的编号（需要进行两次转码）
            getName: {method: 'GET', params: {method: 'name', code: '@code'}, isArray: false}

        });
    }]);

    // 业务参数类型
    app.service('BusinessParamType', ['$resource', 'CommonUtils', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('base/parameter/type/business/:method'), {}, {
            //保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            //更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            //批量删除
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            // 启用
            enable: {method: 'POST', params: {method: 'enable', ids: '@ids'}, isArray: false},
            // 禁用
            disable: {method: 'POST', params: {method: 'disable', ids: '@ids'}, isArray: false},

            //根据id查询参数信息
            get: {method: 'GET', params: {id: '@id', method: 'get'}, isArray: false},

            //分页查询，返回{total:,data:''}
            query: {method: 'POST', params: {method: 'query', start: '@start', limit: '@limit'}, isArray: false},

            // 查询所有的类型并组装成树
            tree: {method: 'GET', params: {method: 'tree'}, isArray: false},

            // 查询指定节点的所有子节点(包含自己）
            // id（必须）:当前节点id
            children: {method: 'GET', params: {method: 'children', id: '@id'}, isArray: false},

            //查询除当前节点（包括子节点）外的所有节点组成的树形数据
            // cid（可选）:当前节点的id
            queryOther: {method: 'GET', params: {method: 'other'}, isArray: false},

            // 查询所有状态为启用的参数类型，返回树形数据
            // 用于：参数编辑页面选择级联参数
            queryValid: {method: 'GET', params: {method: 'queryValid'}, isArray: false},

            // 查询所有启用、已注销状态的参数类型，返回树形数据
            // 用于：参数页面的左侧树
            queryUsing: {method: 'GET', params: {method: 'queryUsing'}, isArray: false},

            //判断指定编号是否重复
            // code(必须）:使用URIEncode进行编码的编号
            hasCode: {method: 'GET', params: {method: 'hasCode', code: '@code'}, isArray: false},

            //判断在指定层级下的名称是否重复：需要参数{id:'',name:''}
            // id：上级节点的id
            // name（必须）:当前节点的名称，使用URIEncode编码
            hasName: {method: 'GET', params: {method: 'hasName', id: '@id', name: '@name'}, isArray: false},

            // 根据类型编号获得类型名称
            // 参数code（必须）：参数类型的编号（需要进行两次转码）
            getName: {method: 'GET', params: {method: 'name', code: '@code'}, isArray: false}

        });
    }]);

    // 基础参数
    app.service('SysParamItem', ['$resource', 'CommonUtils', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('base/parameter/item/system/:method'), {}, {
            //保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            //更新
            update: {method: 'PUT', params: {method: 'update'}, isArray: false},

            //批量删除
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            // 启用
            enable: {method: 'POST', params: {method: 'enable', ids: '@ids'}, isArray: false},
            // 禁用
            disable: {method: 'POST', params: {method: 'disable', ids: '@ids'}, isArray: false},

            //根据id查询账户信息
            get: {method: 'GET', params: {id: '@id', method: 'get'}, isArray: false},

            // 判断在指定类型下的值是否已经存在，如果存在，则返回false
            // type（必须）:参数类型
            // value（必须）：参数的值
            hasValue: {method: 'GET', params: {method: 'hasValue', type: '@type', value: '@value'}, isArray: false},

            // 判断在指定类型下的名称是否已经存在，如果存在，则返回false
            // type（必须）:参数类型
            // name（必须）：参数的名称
            hasName: {method: 'GET', params: {method: 'hasName', type: '@type', name: '@name'}, isArray: false},

            //分页查询（高级接口）
            query: {method: 'POST', params: {method: 'query', start: '@start', limit: '@limit'}, isArray: false},

            fetchCascade: {
                method: 'GET',
                params: {method: 'fetchCascade', type: "@type", value: "@value"},
                isArray: false
            },
            // 查询指定类型下所有有效的参数
            // type（必须）：指定的参数类型
            queryValid: {method: 'GET', params: {method: 'queryValid', type: '@type'}, isArray: false},

            // 查询指定类型下所有有效的选项（一般用于对外提供接口）
            // 参数:type（必须）：指定基础参数的类型（如果使用中文，则必须进行两次转码）
            validItems: {method: 'GET', params: {method: 'queryValid', type: '@type'}, isArray: false}
        });
    }]);

    // 业务参数
    app.service('BusinessParamItem', ['$resource', 'CommonUtils', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('base/parameter/item/business/:method'), {}, {
            //保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            //更新
            update: {method: 'PUT', params: {method: 'update'}, isArray: false},

            //批量删除
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            // 启用
            enable: {method: 'POST', params: {method: 'enable', ids: '@ids'}, isArray: false},

            // 禁用
            disable: {method: 'POST', params: {method: 'disable', ids: '@ids'}, isArray: false},

            //根据id查询账户信息
            get: {method: 'GET', params: {id: '@id', method: 'get'}, isArray: false},

            // 判断在指定类型下的值是否已经存在，如果存在，则返回false
            // type（必须）:参数类型
            // value（必须）：参数的值
            hasValue: {method: 'GET', params: {method: 'hasValue', type: '@type', value: '@value'}, isArray: false},

            // 判断在指定类型下的名称是否已经存在，如果存在，则返回false
            // type（必须）:参数类型
            // name（必须）：参数的名称
            hasName: {method: 'GET', params: {method: 'hasName', type: '@type', name: '@name'}, isArray: false},

            //分页查询（高级接口）
            query: {method: 'POST', params: {method: 'query', start: '@start', limit: '@limit'}, isArray: false},

            // 查询所有级联了指定业务参数的所有业务参数集合（即级联类型为指定编号，且级联的值为指定的值的参数集合）
            // 例如:
            //  业务参数a级联了编号为'N',值为'1'的业务参数，业务参数b也级联了编号'N’,值为'1'的业务参数
            //  那么使用fetchCascade('N','1')查询出来的集合就是[a,b]
            // 必须参数：
            //  type:级联类型
            //  value：级联的值
            // 返回：列表
            // 属性【isCascaded】表示是否被其他参数级联（常用于树形级联）
            fetchCascade: {
                method: 'GET',
                params: {method: 'fetchCascade', type: "@type", value: "@value"},
                isArray: false
            },

            // 查询指定参数类型下，被级联的参数的集合
            // 必须参数：
            //  type：业务参数类型的编号
            // 返回：列表
            queryCascadeItem: {
                method: 'GET',
                params: {method: 'queryCascadeItem', type: "@type"},
                isArray: false
            },

            // 查询指定类型下所有有效的参数
            // type（必须）：指定的参数类型
            queryValid: {method: 'GET', params: {method: 'queryValid', type: '@type'}, isArray: false},

            // 查询指定类型下所有有效的选项（一般用于对外提供接口）
            // 参数:type（必须）：指定业务参数的类型（如果使用中文，则必须进行两次转码）
            validItems: {method: 'GET', params: {method: 'queryValid', type: '@type'}, isArray: false}
        });
    }]);

    // 参数加载器（只有第一次查询时会执行查询，然后将数据缓存到本地，直到页面离开时销毁）
    // 提供基础参数和业务参数两种查询
    app.service('ParameterLoader', ['Parameter', 'CommonUtils', 'BusinessParamItem', function (Parameter, CommonUtils, BusinessParamItem) {
        return {
            // 查询系统参数
            // 查询完成后将查询结果设置给回调函数
            // 必填参数：
            //  typeCode[string]:系统参数的类型编号
            //  callback[function]:查询完成后要执行的函数，该函数接收查询结果
            // 可选参数：
            //  tipInfo[string]：查询过程中要显示的提示语，默认为“加载数据字典...”
            loadSysParam: function (typeCode, callback, tipInfo) {
                var result = Parameter.systemItems(typeCode, function (data) {
                    callback(data.data || []);
                });
                CommonUtils.loading(result, tipInfo || '加载数据字典...');
            },
            // 查询系统参数
            // 指定系统参数类型，查询对应的选项,并将结果进行缓存
            // 查询完成后将查询结果设置给回调函数
            // 必填参数：
            //  typeCode[string]:系统参数的类型编号
            //  callback[function]:查询完成后要执行的函数，该函数接收查询结果
            // 可选参数：
            //  tipInfo[string]：查询过程中要显示的提示语，默认为“加载数据字典...”
            loadBusinessParam: function (typeCode, callback, tipInfo) {
                var result = Parameter.businessItems(typeCode, function (data) {
                    callback(data.data || []);
                });
                CommonUtils.loading(result, tipInfo || '加载数据字典...');
            },

            // 业务参数的树形级联
            // 必须参数：
            //  type：初始化时业务参数的类型
            //  callback：选中节点后的回调
            // 可选参数：
            //  value：如果指定了该值，则表示展示的是级联该参数的树，如果没有指定该值，则表示树的第一级为业务参数类型的选项，选项中的子选项才是级联的参数

            // 也可以只传递一个配置对象作为初始化参数
            //  可配置项：type、callback、value、filter
            //      filter为一个方法，接收一个数组对象，返回一个数据对象
            businessCascadeTree: function (type, value, callback) {

                if (!type) {
                    alert('初始化级联参数树时，缺少参数[type]');
                    return false;
                }
                var filter;
                // 表示传递的是一个配置对象
                if (arguments.length == 1 && angular.isObject(arguments[0])) {
                    var options = type;
                    type = options.type;
                    value = options.value;
                    callback = options.callback;
                    filter = options.filter;
                }

                if (arguments.length == 2) {
                    callback = value;
                }


                var wrap = function (data) {
                    angular.forEach(data || [], function (o) {
                        if (o['isCascaded'] == true) {
                            o.isParent = true;
                        }
                    });
                };
                return {
                    data: function () {
                        return CommonUtils.promise(function (defer) {
                            var promise;
                            if (callback == value) {
                                // 第一级是参数
                                promise = BusinessParamItem.queryCascadeItem({type: type});
                                CommonUtils.loading(promise, '加载选项...', function (data) {
                                    data = data.data || [];
                                    angular.forEach(data, function (o) {
                                        o.isParent = true;
                                        o.isItem = true;
                                    });
                                    // 执行过滤器
                                    if (angular.isFunction(filter)) {
                                        data = filter(data);
                                    }

                                    defer.resolve(data);
                                });
                            } else {
                                // 选项为被级联的参数
                                promise = Parameter.fetchBusinessCascade(type, value);
                                CommonUtils.loading(promise, '加载级联参数...', function (data) {
                                    data = data.data || [];
                                    wrap(data);
                                    // 执行过滤器
                                    if (angular.isFunction(filter)) {
                                        data = filter(data);
                                    }
                                    defer.resolve(data);
                                });
                            }
                        });
                    },
                    disableClick: function (node) {
                        if (node.isItem == true) {
                            alert('请选择被级联的参数!');
                            return true;
                        }
                        return false;
                    },
                    async: function (treeNode, callback) {
                        return CommonUtils.promise(function (defer) {
                            var promise = Parameter.fetchBusinessCascade(treeNode.type, treeNode.value);
                            CommonUtils.loading(promise, '加载级联参数...', function (data) {
                                data = data.data || [];
                                wrap(data);
                                defer.resolve(data);
                                callback(data);
                            });
                        });
                    },
                    position: 'absolute',
                    click: callback
                };
            },
            cascadeTree: function (type, value, callback) {

                if (!type) {
                    alert('初始化级联参数树时，缺少参数[type]');
                    return false;
                }
                var filter;
                // 表示传递的是一个配置对象
                if (arguments.length == 1 && angular.isObject(arguments[0])) {
                    var options = type;
                    type = options.type;
                    value = options.value;
                    callback = options.callback;
                    filter = options.filter;
                }

                if (arguments.length == 2) {
                    callback = value;
                }


                return {
                    data: function () {
                        return CommonUtils.promise(function (defer) {
                            var promise;
                            if (callback == value) {
                                // 第一级是参数
                                promise = Parameter.systemItems(type, function (data) {
                                    data = data.data || [];
                                    angular.forEach(data, function (o) {
                                        o.isParent = o.isCascade;
                                    });
                                    // 执行过滤器
                                    if (angular.isFunction(filter)) {
                                        data = filter(data);
                                    }

                                    defer.resolve(data);
                                });
                                CommonUtils.loading(promise);
                            } else {
                                // 选项为被级联的参数
                                promise = Parameter.fetchSystemCascade(type, value, function (data) {
                                    data = data.data || [];
                                    angular.forEach(data, function (o) {
                                        o.isParent = o.isCascade;
                                    });
                                    // 执行过滤器
                                    if (angular.isFunction(filter)) {
                                        data = filter(data);
                                    }
                                    defer.resolve(data);
                                });
                                CommonUtils.loading(promise);
                            }
                        });
                    },
                    async: function (treeNode, callback) {
                        return CommonUtils.promise(function (defer) {
                            var promise = Parameter.fetchSystemCascade(treeNode.type, treeNode.value, function (data) {
                                data = data.data || [];
                                angular.forEach(data, function (o) {
                                    o.isParent = o.isCascade;
                                });
                                defer.resolve(data);
                                callback(data);
                            });
                            CommonUtils.loading(promise);
                        });
                    },
                    position: 'absolute',
                    click: callback
                };
            }


        };
    }]);
})
(angular);
