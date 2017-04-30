/**
 * Created by zy on 2017/3/1.
 * 核心包，动态加载css/js带时间戳
 */

function autoLoad(load_list) {
    var css, js, url, onload,
        version = Math.floor(Date.parse(new Date()) / 1000).toString(),
        css_elem = document.getElementsByTagName("head")[0],
        js_elem = document.body;

    $.each(load_list, function () {
        if (this.onload) {
            onload = this.onload;
            url = this.url;
        } else {
            url = this;
        }

        if (url.endsWith("css")) {
            css = document.createElement("link");
            css.type = "text/css";
            css.rel = "stylesheet";
            css.href = url + "?v=" + version;
            css_elem.appendChild(css);
        } else if (url.endsWith("js")) {
            js = document.createElement("script");
            js.src = url;
            js.type = "text/javascript";
            if (onload) {
                js.onload = onload;
            }
            js_elem.appendChild(js);
        }
    });
}

// 获取url.hash
var getUrlParams = function () {
    // This function is anonymous, is executed immediately and
    // the return value is assigned to QueryString!
    var query_string = {};
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        // If first entry with this name
        if (typeof query_string[pair[0]] === "undefined") {
            query_string[pair[0]] = decodeURIComponent(pair[1]);
            // If second entry with this name
        } else if (typeof query_string[pair[0]] === "string") {
            var arr = [query_string[pair[0]], decodeURIComponent(pair[1])];
            query_string[pair[0]] = arr;
            // If third or later entry with this name
        } else {
            query_string[pair[0]].push(decodeURIComponent(pair[1]));
        }
    }
    return query_string;
}();

/*
    图片处理
    @el 元素本身
    @url 默认加载失败后加载的图片, 三次都加载失败就直接显示图裂
 */
var imgErrorLoad = function (el, url) {
    var that = $(el),
        failed = that.data('failed'),
        Url = url ? url : 'statics/images/goods_empty.jpg';
    if (!failed || failed < 2) {
        el.src = Url;
        that.data('failed', failed === undefined ? 0 : failed + 1);
    } else {
        return false;
    }
};

$(document).ready(function () {
    var load_list = [];

    // fastclick.js
    load_list.push({
        url: "statics/js/lib/fastclick.min.js",
        onload: function () {
            FastClick.attach(document.body);
        }
    });

    // wexin.js
    load_list.push({
        url: "statics/js/lib/jweixin-1.0.0.js",
        onload: function () {
            $.ajax({
                url: '/RailwayService/entrance/wxshare.do?url=' + encodeURIComponent(encodeURIComponent(location.href.split('#')[0])),
                contentType: 'application/json',
                type: 'POST',
                dataType: 'json',
                success: function (resb) {
                    if (resb.success) {
                        var shareParams = {
                            title: resb.data.title,
                            desc: resb.data.desc,
                            link: resb.data.link,
                            sharepic: resb.data.imgUrl
                        };

                        // js-sdk初始化
                        wx.config({
                            debug: false,
                            appId: resb.data.appid,
                            timestamp: resb.data.timestamp,
                            nonceStr: resb.data.noncestr,
                            signature: resb.data.jsSignature,
                            jsApiList: ['onMenuShareAppMessage', 'onMenuShareTimeline']
                        });

                        // 分享接口
                        wx.ready(function () {
                            wx.onMenuShareAppMessage({
                                title: shareParams.title,
                                desc: shareParams.desc,
                                link: shareParams.link,
                                imgUrl: shareParams.sharepic,
                                success: function (res) {
                                },
                                cancel: function (res) {
                                }
                            });
                            wx.onMenuShareTimeline({
                                title: shareParams.title,
                                link: shareParams.link,
                                imgUrl: shareParams.sharepic,
                                success: function (res) {
                                },
                                cancel: function (res) {
                                }
                            });
                        });

                        wx.error(function (res) {
                            console.log(res);
                        });
                    }
                },
                error: function (resb) {
                    console.log(resb);
                }
            });
        }
    });

    // 监控商品缓存
    if (window.location.pathname.indexOf('merchandise.html') === -1) {
        sessionStorage.removeItem('shoppingCart');
    }

    return autoLoad(load_list)
});
