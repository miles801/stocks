// 基于uploadify.js/uploadifive.js封装的附件上传插件
// 如果浏览器支持html5，则使用html5的引擎
// 否则尝试启用flash插件的方式

(function ($) {
    // 检查浏览器是否安装了Flash插件
    function checkFlash() {
        var isSupport = true;
        var notInstallFlashMsg = '您没有安装Flash插件，附件上传功能将无法使用!\r\n请安装最新版Flash插件后重启浏览器!';
        var isIe = $.browser.msie;
        if (isIe) {
            // 判断是否安装了flash插件
            try {
                new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
            } catch (e) {
                alert(notInstallFlashMsg);
                isSupport = false;
            }
        } else {
            var installedFlash = navigator.plugins['Shockwave Flash'];
            if (installedFlash === undefined) {
                alert(notInstallFlashMsg);
                isSupport = false;
            }
        }
        return isSupport;
    }

    if (!checkFlash()) {
        throw '没有安装Flash插件!无法使用附件上传功能';
    }

    // 检测是否引入了基础模块
    var app = angular.module('eccrm.angular');
    if (app == undefined) {
        alert('没有检测到[eccr.angular]模块,请保证引入该js前已经引入了eccrm-angular.js');
        return false;
    }

    // 插件相关js和css是否加载成功的标示
    var envPrepared = false;

    // 附件上传上下文
    // 保存了成功上传的附件信息
    app.provider('EccrmUploadContext', [function () {
        // 上传成功的附件列表
        var ids = [];
        return {
            $get: function () {
                return {
                    // 插件内部使用，用于成功上传附件后添加附件的id
                    // 必须参数：id[string]
                    push: function (id) {
                        if (angular.isArray(id)) {
                            ids = ids.concat(id);
                        } else if (angular.isString(id) && id) {
                            ids.push(id);
                        }
                    },

                    // 将已成功添加的附件从列表中移除
                    // 必须参数：id[string] 附件的id
                    remove: function (id) {
                        if (!id) return;
                        for (var i = ids.length; i > 0; i--) {
                            if (ids[i] == id) {
                                ids.splice(i, 1);
                                break;
                            }
                        }
                    },
                    // 获取被成功上传的附件的id集合
                    // 注意：这个数组是一个副本，对返回的数组的操作不会影响原数组
                    get: function () {
                        return ids.slice(0);
                    }
                }
            }
        }
    }]);

    // 需要注意的问题
    // 当页面的session失效后，sessionId就会获取不到，从而会导致该插件出现302错误
    app.directive('eccrmUpload', ['CommonUtils', '$timeout', 'EccrmUploadContext', function (CommonUtils, $timeout, EccrmUploadContext) {
        var uploadifyJsPath = CommonUtils.contextPathURL('/vendor/uploadify/jquery.uploadify.min.js');
        var uploadifyCssPath = CommonUtils.contextPathURL('/vendor/uploadify/uploadify.css');
        var uploadifySwfPath = CommonUtils.contextPathURL('/vendor/uploadify/uploadify.swf');
        // 加载上传附件的js
        var loadJs = function () {
            var context = this;
            if (envPrepared == true) {
                context.resolve();
                return true;
            }
            var timer = $timeout(function () {
                context.reject('请求超时:' + uploadifyJsPath);
            }, 5000);
            $.getScript(uploadifyJsPath, function () {
                $timeout.cancel(timer);
                context.resolve();
            });
        };

        // 加载附件上传的css
        var loadCss = function () {
            var context = this;
            if (envPrepared == true) {
                context.resolve();
                return true;
            }
            var head = document.getElementsByTagName('head')[0];
            var link = document.createElement('link');
            link.href = uploadifyCssPath;
            link.rel = 'stylesheet';
            link.type = 'text/css';
            head.appendChild(link);
            this.resolve();
        };

        // 默认配置
        // 其他允许的配置请参考demo.html
        var defaults = {
            // uploadify的swf文件的存放位置，默认值为uploadify.swf
            // 该参数不允许设置
            swf: uploadifySwfPath,
            buttonText: '上传',
            height: 24,
            width: 80,
            // 文件最大大小20M
            fileSizeLimit: 20 * 1000,
            removeTimeout: 1,
            removeCompleted: true,
            // 是否允许上传多个
            multi: false,

            // 初始数组（用于回显）
            // 数组元素格式类型：{}
            // id:附件的id（必须）
            // name：文件名称
            // uploadTime：上传时间（格式为时间戳）
            // size:文件大小（单位为byte）
            files: [],

            // 附件要上传的地址
            uploader: CommonUtils.contextPathURL('/attachment/upload'),
            onSelectError: function (file, errorCode, errorMsg) {
                switch (errorCode) {
                    case 'QUEUE_LIMIT_EXCEEDED':
                        alert('超出允许上传的最大文件数!');
                        break;
                    case 'FILE_EXCEEDS_SIZE_LIMIT':
                        alert('超出单个文件允许的大小!');
                        break;
                    case 'ZERO_BYTE_FILE':
                        alert('不允许上传空文件!');
                        break;
                    case 'INVALID_FILETYPE':
                        alert('不支持上传的文件类型!');
                        break;
                }
            },
            'overrideEvents': ['onUploadError', 'onDialogClose', 'onSelectError'],

            onDialogClose: function () {
                var swfUpload = this;
                if (swfUpload.queue.length > swfUpload.settings.uploadLimit) {
                    alert('超出允许上传的最大文件数' + swfUpload.uploadLimit + '!');
                    return false;
                }

            },
            onUploadError: function (e, errorCode, m1, m2) {
                if (errorCode == -240) {
                    alert('上传失败：超出允许上传的最大文件数!');
                } else {
                    alert("上传失败：" + m1 + ":" + m2);
                }
            },
            onFallback: function () {
                alert('初始化附件上传失败,没有检测到当前浏览器安装Flash插件!');
            }
        };

        return {
            restrict: 'A',
            scope: {
                options: '=eccrmUpload'
            },
            link: function (scope, elem) {

                // 如果元素没有指定ID则产生一个随机ID
                // （uploadify要求元素必须有一个ID）
                var setIdIfNull = function () {
                    if (elem.attr('id') == undefined || elem.attr('id') == null) {
                        elem.attr('id', CommonUtils.randomID(20));
                    }
                };

                // 初始化插件
                var init = function () {

                    // 检测插件是否初始化成功
                    if (!angular.isFunction(elem.uploadify)) {
                        alert('初始化附件上传失败!没有获得uploadify()方法!');
                        return false;
                    }

                    // 检测[设置]元素是否具有id
                    setIdIfNull();

                    // 修改标识位，表示已经初始化过一次
                    envPrepared = true;

                    // 获取自定义配置 & 初始化uploadify参数
                    scope.options = scope.options || {};
                    var promise = CommonUtils.parseToPromise(scope.options);
                    promise.then(function (cfg) {
                        var options = angular.extend({}, defaults, cfg);
                        // not limit file count,just limit queue size
                        var SWFUpload;
                        options.queueSizeLimit = cfg.uploadLimit;
                        options.onUploadSuccess = function (file, data, response) {
                            var obj = $.parseJSON(data);
                            if (!obj.success || obj.data.length < 1) {
                                alert('附件上传失败!');
                                throw '附件上传失败!没有获取到返回的附件信息!' + (obj.error || obj.fail || obj.message || "");
                            }

                            // 获得附件id
                            EccrmUploadContext.push(obj.data);
                            if (angular.isFunction(cfg.onUploadSuccess)) {
                                scope.$apply(function () {
                                    file.fileName = file.name;
                                    cfg.onUploadSuccess(obj.data[0], file);
                                });
                            }
                        };
                        options.onInit = function (instance) {
                            SWFUpload = instance;
                        };
                        elem.uploadify(options);

                        cfg.remove = function (fileId) {
                        };
                    });
                };

                // 加载js失败
                var loadError = function (errorMsg) {
                    alert(errorMsg);
                };

                // 加载js和样式
                CommonUtils.chain([loadJs, loadCss], init, loadError);
            }
        };
    }]);
})(jQuery);