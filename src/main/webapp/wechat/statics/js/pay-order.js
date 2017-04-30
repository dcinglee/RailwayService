/**
 * Created by zy on 2017/3/1.
 */
function add(a, b) {
    var c, d, e;
    try {
        c = a.toString().split(".")[1].length;
    } catch (f) {
        c = 0;
    }
    try {
        d = b.toString().split(".")[1].length;
    } catch (f) {
        d = 0;
    }
    return e = Math.pow(10, Math.max(c, d)), (mul(a, e) + mul(b, e)) / e;
};

function sub(a, b) {
    var c, d, e;
    try {
        c = a.toString().split(".")[1].length;
    } catch (f) {
        c = 0;
    }
    try {
        d = b.toString().split(".")[1].length;
    } catch (f) {
        d = 0;
    }
    return e = Math.pow(10, Math.max(c, d)), (mul(a, e) - mul(b, e)) / e;
};

function mul(a, b) {
    var c = 0,
        d = a.toString(),
        e = b.toString();
    try {
        c += d.split(".")[1].length;
    } catch (f) {
    }
    try {
        c += e.split(".")[1].length;
    } catch (f) {
    }
    return Number(d.replace(".", "")) * Number(e.replace(".", "")) / Math.pow(10, c);
};

function div(a, b) {
    var c, d, e = 0,
        f = 0;
    try {
        e = a.toString().split(".")[1].length;
    } catch (g) {
    }
    try {
        f = b.toString().split(".")[1].length;
    } catch (g) {
    }
    return c = Number(a.toString().replace(".", "")), d = Number(b.toString().replace(".", "")), mul(c / d, Math.pow(10, f - e));
};

// radio事件委托, 切换不同的送餐方式来显示不同的选项、配送费、总价格
$(document).on('click', 'input[type="radio"]', function () {
    var name = $(this).data('name'),
        $inputs = $(this).parents('div.radio').find('input[type="radio"][data-name=' + name + ']'),
        target = $(this).data('target'),
        $cost = $('[data-name="cost"]'),
        cost = Number($cost[0].innerText.split('￥')[1]),
        $fare = $(document).find('div[data-name=fare]'),
        fare = Number($fare.find('.money').text().split('￥')[1]),
        picker = $(this).data('picker'),
        reset = $(this).data('reset'),
        that = this;

    if (picker && !order) {
        if ($('.weui-picker-modal').length === 0) {
            $(picker).picker("open");
        }
    }

    if (reset) {
        $(reset).text('稍晚时间');
    }

    if ($(this).attr('checked')) {
        return false;
    }

    // input[checkbox] 勾选
    $.each($inputs, function () {
        if (that == this) {
            $(this).attr('checked', true);
        } else {
            $(this).attr('checked', false);
        }
    });

    if (target) {
        if (target == 'receipt') {
            $('#time').hide();
            $('#receipt-place,#receipt-time').show();
            if ($('#time-select').val() != '') {
                $("#last-time").val($('#time-select').val());
            }
            $fare.show();
            $cost.text('￥' + add(cost, fare));
        } else {
            // show/hide对应的div,同时处理配送费
            $('#receipt-place,#receipt-time').hide();
            $fare.hide();
            $cost.text('￥' + sub(cost, fare));
            $("#last-time,#time-select").val();
            if ($('#last-time').val() !== '') {
                $("#time-select").val($('#last-time').val());
            }
            $('#time').show();
        }
    }
});

// 渲染页面
var errmsg;
var order = sessionStorage.Order ? JSON.parse(sessionStorage.Order) : undefined;
$.ajax({
    url: '/RailwayService/order/affirmOrder?merchantId=' + sessionStorage.merchantId + '&stationId=' + sessionStorage.stationId,
    type: 'GET',
    dataType: 'json',
    success: function (resb) {
        if (resb.success) {
            var user_info = resb.data.user,
                cost = 0,
                cost_every,
                re = /[^\u4e00-\u9fa5]/,
                name = user_info.name ? user_info.name : user_info.nickName;

            // 判断是否从行程页面过来的
            if (order) {
                $('#username').val(order.customerName);
                $('#phone').val(order.customerPhoneNo);
            } else {
                // 填充用户名
                if (name) {
                    $('#username').val(re.test(name) ? '' : name);
                }

                // 填充手机号
                if (user_info.phoneNo) {
                    $('#phone').val(user_info.phoneNo);
                }
            }

            // init 位置选择
            initLocationPicker(resb.data.listDeliverAddress);

            // init 时间选择
            initTimePicker();

            // 设置商家地址
            $('.order-merchant span').text(resb.data.merchant.address ? resb.data.merchant.address : '商家暂未设置地址resb.data.merchant.address');

            // 渲染详细商品、生成购物车缓存
            var commodity = {};
            $.each(resb.data.listShoppingCart, function () {
                cost_every = mul(this.price, this.count);
                cost = add(cost, cost_every);
                commodity[this.productId] = {num: this.count};

                $('div.order-accounts-bd').append($('#clone').clone().html().replace(
                    '__商品名称__', this.productName).replace(
                    '__商品数量__', this.count).replace('__商品价格__', cost_every));
                productList.push({productId: this.productId, quantity: this.count});
            });

            // 渲染配送费
            sessionStorage.setItem('shoppingCart', JSON.stringify(commodity));
            $('div.order-accounts-bd').append($('#clone').clone().html().replace(
                '__商品名称__', '配送费').replace('X__商品数量__', '').replace('__商品价格__', resb.data.distributionCosts).replace('tr', 'fare'));

            // 设置消费总价
            cost = add(cost, resb.data.distributionCosts);
            $('[data-name=cost]').text('￥' + cost);

            // 配送方式
            if (order) {
                // 配送地址
                if (order.address) {
                    $('#location-select').val(order.address);
                }
                $(order.typeId).click();
                // 配送时间
                $(order.timeId).click().parents('label').find('.font-orange').text(order.afterTime ? order.afterTime : order.latestServiceTime);
                sessionStorage.removeItem('Order');
                order = null;
            }

            // 判断配送人员是否在线
            if (!JSON.parse(sessionStorage.delivery)) {
                $('#delivery').attr('disabled', true).parent().hide();
                $('#self_take').click();
            }

            // 设置小推车图片
            $.each(resb.data.stationForImageRelaList, function () {
                $('.pay-mask .map').append('<img src="' + String(this) + '" alt="图片加载失败" style="color: #fff;">');
            });
        }
        else {
            errmsg = resb.message;
        }
    },
    error: function (XHR) {
        errmsg = XHR.statusText;
    },
    complete: function () {
        if (errmsg) {
            $.toast(errmsg, 'forbidden');
            setTimeout(function () {
                history.back(-1);
            }, 2500)
        }
    }
});

var initLocationPicker = function (address) {
    $("#location-select").picker({
        payOrder: true,
        title: "请选择您的收货地址",
        cols: [
            {
                textAlign: 'center',
                values: $.map(address, function (i) {
                    return i.address
                })
            }
        ],
        address: address,
        onClose: function (p) {
            $(p.params.input).attr('data-deliverAddressId', p.params.address[p.params.cols[0].activeIndex].deliverAddressId);
        }
    });
};
var initTimePicker = function () {
    var date = new Date(),
        min = date.getMinutes() + 20,
        hour = date.getHours(),
        hours = [],
        first_mins = [],
        normal_mins = [],
        remainder = 0,
        index = 0,
        default_time = '';

    for (var i = 0; i <= 55; i += 5) {
        normal_mins.push(i > 5 ? String(i) : '0' + String(i));
    }

    if (min > 60) {
        hour += 1;
        min -= 60;
    }

    remainder = min % 5;

    if (remainder !== 0) {
        min += 5 - remainder;
    } else {
        min += 5;
    }

    if (min > 55) {
        hour++;
        first_mins = normal_mins;
    } else {
        while (min <= 55) {
            first_mins.push(min > 5 ? String(min) : '0' + String(min));
            min += 5;
        }
    }

    for (; hour < 24; ++hour) {
        hours.push(hour);
    }

    // 取得默认时间
    if (sessionStorage.route) {
        var aboardTime = JSON.parse(sessionStorage.route).aboardTime.split(':'),
            default_h = parseInt(aboardTime[0]),
            default_m = parseInt(aboardTime[1]);

        default_m -= 15;
        if ((default_h * 60 + default_m) > (hour * 60 + min)) {
            if (default_m < 0) {
                default_h -= 1;
                default_m += 60;
            }
            if (default_m < 10) {
                default_m = '0' + String(default_m);
            }
            default_time = String(default_h) + ':' + String(default_m);
        }
    }

    $("#last-time,#time-select").val(default_time).picker({
        title: "请选择您的取货时间",
        time: true,
        onClose: function (picker) {
            $($(picker.params.input).data('target')).text(picker.value.join(':'));
        },
        cols: [
            {
                textAlign: 'center',
                values: hours,
                cssClass: 'time-picker',
                onChange: function (picker, value) {
                    var data,
                        idx = picker.cols[0].activeIndex,
                        col2_value = picker.cols[1].value;
                    if ((idx !== 0 && index === 0) || (idx === 0 && index !== 0)) {
                        if (idx === 0) {
                            data = first_mins;
                        } else {
                            data = normal_mins;
                        }
                        picker.cols[1].replaceValues(data, data);
                        $("#time-select").picker('setValue', [value, col2_value]);
                    }
                    index = idx;
                }
            },
            {
                textAlign: 'center',
                cssClass: 'time-picker',
                values: first_mins
            }
        ]
    });
};
var productList = [];

// 获得时间
function getTime(time, offset, hour, min) {
    if (Number(time) === offset) {
        min += offset;
        if (min > 60) {
            min = min % 60;
            if (min < 10) {
                min = '0' + String(min);
            }
            hour += 1;
        }
        time = [hour, min].join(':');
    }
    return time;
}

/*
 *   比较时间, 送餐时间为开车前30分钟之外才允许选择
 *   arriveTime 送达时间
 *   step: 30
 */
function compareTime(arriveTime, step) {
    var arrive = arriveTime.split(':').map(function (i) {
            return +i;
        }),
        step = step ? step : 30,
        aboart = JSON.parse(sessionStorage.route).aboardTime.split(':').map(function (i) {
            return +i;
        });

    if (arrive[0] * 60 + arrive[1] + step >= aboart[0] * 60 + aboart[1]) {
        return false;
    }
    return true;
}

// 提交数据
$('a#submit').click(function () {
    var type = $(':checked').attr('id'),
        user_name = $('#username').val().trim(),
        phone_no = $('#phone').val().trim(),
        address = $('#location-select').data('deliveraddressid'),
        date = new Date(),
        hour = date.getHours(),
        min = date.getMinutes(),
        data = {merchantId: sessionStorage.merchantId},
        url = '/RailwayService/order/createOrder',
        re = /[^\u4e00-\u9fa5]/,
        time,
        confirm_str,
        afterTime,
        typeId,
        timeId,
        routeData;

    // 姓名检测
    if (user_name.length === 0) {
        return $.toast('请输入联系人姓名', 'forbidden');
    }
    if (re.test(user_name)) {
        return $.toast('姓名只能为中文', 'forbidden');
    }
    data['customerName'] = user_name;

    // 电话号码检测
    if (phone_no.length === 0) {
        return $.toast('请输入联系人手机号码', 'forbidden')
    }
    data['customerPhoneNo'] = phone_no;

    typeId = '#' + type;
    if (type === 'delivery') {
        type = 16002; // 16002为配送
        // 送餐位置检测
        if (!address) {
            return $.toast('请选择收货位置', 'forbidden')
        }
        data['deliverAddress'] = address;

        // 送达时间
        timeId = '#' + $('#receipt-time').find(':checked').attr('id');
        time = $('#receipt-time').find(':checked').parents('label').find('span.font-orange').text();
        if (time === 30) {
            afterTime = 30;
        }
        data['latestServiceTime'] = getTime(time, 30, hour, min);
        confirm_str = '送餐时间为:' + data['latestServiceTime'];
    } else {
        type = 16001; // 16001为自提
        // 自提时间检测
        timeId = '#' + $('#time').find(':checked').attr('id');
        time = $('#time').find(':checked').parents('label').find('span.font-orange').text();
        if (time === 10) {
            afterTime = 10;
        }
        data['latestServiceTime'] = getTime(time, 10, hour, min);
        confirm_str = '取货时间为:' + data['latestServiceTime'];
    }
    data['deliverType'] = type; // 设置配送方式
    data['listProduct'] = productList; // 设置购买的产品

    // 保存当前用户信息
    routeData = JSON.parse(JSON.stringify(data));
    routeData['timeId'] = timeId;
    routeData['typeId'] = typeId;
    routeData['afterTime'] = afterTime;

    // 是否存在行程
    if (!sessionStorage.route) {
        sessionStorage.Order = JSON.stringify(routeData);
        return $.alert('您尚未设置行程，点击确认后开始行程设置', '提示', function () {
            return window.location.href = 'route.html';
        });
    }

    if (!compareTime(data['latestServiceTime'], afterTime)) {
        return $.confirm('发车时间不足' + (type === 16001 ? 10 : 30) + '分钟,不允许下单,或者修改行程', '警告', function () {
            sessionStorage.Order = JSON.stringify(routeData);
            window.location.href = 'route.html';
        });
    }

    $.confirm(confirm_str, "<span style='font-weight:bold;'>确认支付?</span>", function () {
        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeSend: function () {
                $.showLoading();
            },
            success: function (resb) {
                $.hideLoading();
                if (resb.success) {
                    var data = resb.data;
                    setTimeout(function () {
                        WeixinJSBridge.invoke(
                            'getBrandWCPayRequest', {
                                "appId": data.appId,     //公众号名称，由商户传入
                                "timeStamp": data.timeStamp,         //时间戳，自1970年以来的秒数
                                "nonceStr": data.nonceStr, //随机串
                                "package": data.packAge,
                                "signType": data.signType,         //微信签名方式：
                                "paySign": data.paySign //微信签名
                            },
                            function (res) {
                                // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                                if (res.err_msg === "get_brand_wcpay_request:ok") {
                                    $.toast('支付成功', "success");
                                    setTimeout(function () {
                                        sessionStorage.setItem('orderId', data.orderId);
                                        window.location.href = '/RailwayService/wechat/orderDetail.html';
                                    }, 2500);
                                } else {
                                    $.toast('支付失败', 'cancel');
                                }
                            }
                        );
                    }, 300);
                } else {
                    $.toast(resb.message, "forbidden");
                }
            },
            error: function (xml) {
                $.hideLoading();
                $.toast(xml.statusText, "forbidden");
            }
        })
    });
});

// 解决安卓虚拟键盘问题
var oHeight = $(document).height(); //屏幕当前的高度
$(window).resize(function () {
    if ($(document).height() !== oHeight) {
        $(".footer-wrap").css("display", "none");
    } else {
        $(".footer-wrap").css("display", "block");
    }
});

$('#location-select,#last-time,#time-select').click(function () {
    document.activeElement.blur();
});

// 小推车
$("#location-select,#last-time,#time-select").click(function () {
    $('.pay-mask').fadeIn('fast');
});

$(document).on('click', 'a.close-picker', function () {
    $('.pay-mask').fadeOut('fast');
});