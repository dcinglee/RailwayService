/**
 * Created by zy on 2017/3/3.
 */

$(document).ready(function () {
    var url;

    if (sessionStorage.merchantLogo) {
        url = '/RailwayService' + sessionStorage.merchantLogo;
    } else {
        url = '/RailwayService/images/default.jpg';
    }

    // 设置背景图片
    $('.order-top').append('<style>.order-top:before{background: url("' + url + '")}</style>');
    // 设置logo
    $('.logo-circle').attr('src', url);
    // 设置商家名称
    $('.merchant-title span').text(sessionStorage.merchantName);
});

// 提交评价
$('a#submit').click(function () {
    var $inputs = $(':checked'),
        text = $('textarea').val();

    if ($inputs.length === 0) {
        return $.toast("请选择评分等级", 'forbidden');
    }

    if (text.length === 0) {
        return $.toast("请输入评价内容", 'forbidden');
    }

    $.ajax({
        url: '/RailwayService/comment/add',
        data: JSON.stringify({merchantId: sessionStorage.merchantId, grade: $inputs.val(), content: text, orderId: sessionStorage.orderId}),
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function () {
            $.showLoading();
        },
        success: function (resb) {
            $.hideLoading();
            if (resb.success) {
                $.toast(resb.message, "success");
                sessionStorage.removeItem('orderId');
                setTimeout(function () {
                    window.location.href = '/RailwayService/wechat/order.html';
                }, 2000);
            } else {
                $.toast(resb.message, "forbidden");
            }
        },
        error: function (xml) {
            $.hideLoading();
            $.toast(xml.statusText, "forbidden");
        }
    });
});