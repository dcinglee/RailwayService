/**
 * Created by zy on 2017/4/18.
 */
+function ($) {
    'use strict';

    var callback = {
            beforeSend: null,
            success: null,
            error: null,
            complete: null
        },
        ajax = {
            type: 'POST',
            async: true,
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            data: {},
            timeout: 1000 * 90,
            dataType: 'JSON',
            beforeSend: callback.beforeSend,
            success: callback.success,
            error: callback.error,
            complete: callback.error
        },
        url = {
            login: 'http://thisa:8888/login_kyfw',
            vCode: {
                submit: 'http://thisa:8888/submit_vcode',
                refresh: 'http://thisa:8888/refresh_vcode'
            },
            passengers: {
                get: 'http://thisa:8888/get_passengers',
                add: 'http://thisa:8888/add_passenger'
            },
            check_order: 'http://thisa:8888/check_order',
            submit_order: 'http://thisa:8888/submit_order',
            stopTicketing: 'http://thisa:8888/stop_grabticket',
            cancel_order: 'http://thisa:8888/cancel_order',
            queryTrain: 'http://thisa:8888/query_train'
        },
        doAjax = function (params) {
            // 跨域配置
            $.support.cors = true;
            return $.ajax($.extend({}, ajax, params));
        };

    $.fn.ticket = {
        url: url,
        doAjax: function (params) {
            return doAjax(params);
        },
        login: function (params) {
            var options = $.extend({}, {}, ajax);
            options['url'] = url.login;
            options['data'] = {
                account: params['username'],
                pwd: params['password']
            };

            // 运行前函数
            options.beforeSend = function () {
                $.showLoading('登陆中');
                if (typeof  params.beforeSend === 'function') {
                    params.beforeSend();
                }
            };

            // 成功函数
            options.success = function (resb) {
                $.hideLoading();
                if (resb.step === 1) {
                    // 渲染认证码
                    $('body').append(
                        '<div id="code">' +
                        '  <div class="train-mask"></div>' +
                        '  <div class="auth">' +
                        '    <div class="center">' +
                        '      <img src="' + resb.data.filename + '" id="vcode_img">' +
                        '      <a href="javascript:void(0);" class="weui-btn" id="img_refresh">' +
                        '        <i class="icon iconfont icon-svg27"></i>刷新' +
                        '      </a>' +
                        '      <div class="auth-check">' +
                        '        <label for="1">' +
                        '          <input type="checkbox" id="1">' +
                        '            <i class="icon iconfont icon-huochepiao"></i>' +
                        '        </label>' +
                        '        <label for="2">' +
                        '          <input type="checkbox" id="2">' +
                        '          <i class="icon iconfont icon-huochepiao"></i>' +
                        '        </label>' +
                        '        <label for="3">' +
                        '          <input type="checkbox" id="3">' +
                        '          <i class="icon iconfont icon-huochepiao"></i>' +
                        '        </label>' +
                        '        <label for="4">' +
                        '          <input type="checkbox" id="4">' +
                        '          <i class="icon iconfont icon-huochepiao"></i>' +
                        '        </label>' +
                        '      </div>' +
                        '      <div class="auth-check">' +
                        '        <label for="5">' +
                        '          <input type="checkbox" id="5">' +
                        '          <i class="icon iconfont icon-huochepiao"></i>' +
                        '        </label>' +
                        '        <label for="6">' +
                        '          <input type="checkbox" id="6">' +
                        '          <i class="icon iconfont icon-huochepiao"></i>' +
                        '        </label>' +
                        '        <label for="7">' +
                        '          <input type="checkbox" id="7">' +
                        '          <i class="icon iconfont icon-huochepiao"></i>' +
                        '        </label>' +
                        '        <label for="8">' +
                        '          <input type="checkbox" id="8">' +
                        '          <i class="icon iconfont icon-huochepiao"></i>' +
                        '        </label>' +
                        '      </div>' +
                        '      <div class="weui-btn-area">' +
                        '        <a href="javascript:void(0);" class="weui-btn btn_or" id="vCode">确定</a>' +
                        '      </div>' +
                        '    </div>' +
                        '  </div>' +
                        '</div>'
                    );

                    $(document)
                    // 验证码提交
                        .on('click', '#vCode', function () {
                            var $inputs = $(this).parents('.center').find(':checked'),
                                code = $.map($inputs, function (input) {
                                    return $(input).attr('id');
                                }).join(' ');
                            return doAjax({
                                url: url.vCode.submit,
                                data: {vcode: code},
                                beforeSend: function () {
                                    $.showLoading();
                                },
                                success: function (resb) {
                                    $.hideLoading();
                                    if (resb.step === 1) {
                                        $('#vcode_img').attr('src', resb.data['filename']);
                                        $.alert('验证码错误，请重新选择', '失败', function () {
                                            $(':checked').removeAttr('checked');
                                        });
                                    }
                                    else if (resb.step === -1) {
                                        $('#code').remove();
                                        if (sessionStorage.refresh) {
                                            sessionStorage.removeItem('refresh');
                                            return window.location.reload();
                                        }
                                        $.alert("登陆成功", function () {
                                            $('#bound').addClass('hide');
                                            $('#logged,#modify').removeClass('hide');
                                            $('#modify>dd.weui-flex__item').addClass('color-orange');
                                        });
                                    }
                                    console.log(resb);
                                },
                                error: function (xhr) {
                                    $.alert(xhr.statusText, '错误', function () {
                                        $.hideLoading();
                                        $('#code').remove();
                                    });
                                }
                            });
                        })

                        // 验证码刷新
                        .on('click', '#img_refresh', function () {
                            $(':checked').removeAttr('checked');
                            return doAjax({
                                url: url.vCode.refresh,
                                xhrFields: {
                                    withCredentials: true
                                },
                                crossDomain: true,
                                beforeSend: function () {
                                    $.showLoading();
                                },
                                success: function (resb) {
                                    if (resb.error === 1) {
                                        $.alert(resb.info);
                                    } else {
                                        $('#vcode_img').attr('src', resb.data['filename']);
                                    }
                                },
                                error: function (xhr) {
                                    $.alert(xhr.statusText);
                                },
                                complete: function () {
                                    $.hideLoading();
                                }
                            });
                        })
                }
                if (typeof  params.success === 'function') {
                    params.success(resb);
                }
            };

            // 错误函数
            options.error = function (xhr) {
                $.alert(xhr.statusText, '绑定失败', function () {
                    $.hideLoading();
                    if (typeof  params.success === 'function') {
                        params.error(xhr);
                    }
                });
            };

            // 执行
            doAjax(options);
        },
        storage: {
            set: function (key, value) {
                sessionStorage.setItem(key, JSON.stringify(value));
            },
            get: function (key) {
                if (sessionStorage[key]) {
                    try {
                        return JSON.parse(sessionStorage[key]);
                    } catch (err) {
                        return sessionStorage[key]
                    }
                } else {
                    return undefined;
                }
            },
            remove: function (list) {
                if (!list instanceof Array) {
                    return false;
                }

                $.each(list, function () {
                    sessionStorage.removeItem(this);
                });
                return true;
            }
        }
    }
}(jQuery);