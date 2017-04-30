/**
 * Created by zy on 2017/3/6.
 */
$(document).ready(function () {
    $('a.btn-cancel').attr('href', 'tel://' + sessionStorage.phone);
    $('#refund').text(sessionStorage.refund);
}).on('click', 'input[type=radio]', function () {
    var $div,
        $inputs,
        $textarea,
        target,
        that = this,
        name = $(this).data('name');

    if (!name) {
        return false;
    }
    $div = $(this).parents('.radio-bd');
    $inputs = $div.find('input[type="radio"][data-name="' + name + '"]');
    $textarea = $div.find('textarea');
    $textarea.attr("disabled", true);

    $.each($inputs, function () {
        if (that === this) {
            if ($(this).data('target')) {
                $textarea.removeAttr("disabled");
            }
            $(this).attr('checked', true);
        } else {
            $(this).attr('checked', false);
        }
    })
});

// 提交取消原因
$('a.btn-confirm').click(function () {
    var value,
        $input = $(':checked'),
        $textarea = $('textarea');

    if ($textarea.attr('disabled')) {
        value = $input.parents('.weui-flex').find('p').text();
    } else {
        value = $textarea.val();
        if (value.trim().length === 0) {
            return $.toast("请输入原因", "forbidden");
        }
    }

    $.ajax({
        url: '/RailwayService/order/cancelMainOrder',
        data: JSON.stringify({mainOrderId: sessionStorage.orderId, reason: value}),
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function () {
            $.showLoading();
        },
        success: function (resb) {
            $.hideLoading();
            if (resb.success) {
                $.toast('提交请求成功', "success");
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