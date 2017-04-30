var globalPageSize=15;
// 定义工具类
window.utils = (function () {
    return {
        /**
         * 请求后台接口，无论成功或失败，都会调用回调方法。
         * @param url 请求的地址。
         * @param data [可选]发送到后台的数据。
         * @param callback [可选]回调方法，接受返回值。
         */
        request: function (url, data, callback) {
            $.ajax({
                url: url,
                type: "POST",
                data: data || {},
                dataType: "json",
                success: callback || function () {
                    console.info("请求成功！");
                },
                error: function () {
                    if (callback)
                        callback({code: 7, success: false, message: "请求服务失败！"});
                    else
                        console.info("请求失败！");
                }
            });
        }
    };
})();

