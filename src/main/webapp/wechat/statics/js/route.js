var obj = {},
    height = $(document).height();

var toast = function (message, type) {
    $.toast(message, type ? type : 'forbidden');
    setTimeout(function () {
        $('a.active').prev().click();
    }, 2000);
};

var get_last_num = function (a) {
    var b = a % 4;
    return b ? b : 5;
};

$(document).on('click', function (e) {
    if (e.target !== $('ul') && e.target !== $('.change-type')[0] && e.target !== $('.change-type span')[0] && e.target !== $('.change-type img')[0]) {
        $('ul').hide();
    }
});

$('input').focus(function () {
    if ($(window).height() != height) {
        $('footer').hide();
    }
});

$('input').blur(function () {
    $('footer').show();
});

// 导航点击
$('nav a').click(function () {
    var $el = $('nav a.active'),
        $hide_div = $($el.attr('href')),
        $input = $hide_div.find('input'),
        $show_div = $($(this).attr('href'));

    if (this === $el || $(this).prev().find('.nav-value span').css('display') === 'none') {
        return false;
    } else {
        $el.removeClass('active');
        $hide_div.hide();
        query($input.val('')); // 还原搜索框
        $(this).addClass('active');
        $show_div.show();
    }

    // 取消锚点进入历史记录
    location.replace(this.href);
    return false;
});

// 获取当前站点
var getCurrentStation = function () {
    var clicked = false;

    // 同步获取所有站点
    renderAllStations();

    $('#start i').hide();

    // 获得当前位置
    $.ajax({
        url: '/RailwayService/entrance/getCurrentStation',
        type: 'GET',
        dataType: 'json',
        async: false,
        success: function (res) {
            if (res.success) {
                obj.startStation = res.data.stationName;
                obj.startStationId = res.data.stationId;
                clicked = true;
            }
        }
    });

    // 未获得位置默认长沙南出发
    if (!clicked) {
        obj.startStation = "深圳北";
        obj.startStationId = 'IOQ';
    }

    $('#start span').text(obj.startStation).show();
    $('#train').click();
    renderTrain();
};

// 获取出发站名
var renderAllStations = function (stationName) {
    var render = function (data) {
            var html_not_found = '<div class="not-found" style="display: none;">未查询到发车的车次</div>';
            $('#start-city .city-content').empty().append(html_not_found);
            $.each(data, function () {
                $('#start-city .city-content').append(
                    '<div class="box-block block" data-target="#start" data-name="' +
                    this.stationName + '" data-abbr="' + this.stationNameAbbr +
                    '" data-spell="' + this.spell + ' " data-stationId="' + this.stationId + '"><div><span>' + this.stationName + '</span></div></div>');
            });

            // 取消占位class
            $('#start-city .city-content').find('.box-block:not(:nth-last-of-type(n+' + get_last_num(data.length) + '))').removeClass('block');
            rendered = true;
        },
        rendered = false;

    // 获取最新出发站名
    $.ajax({
        url: '/RailwayService/railwayStation/queryAllSpeedRailWayStations',
        type: 'GET',
        dataType: 'json',
        async: false,
        success: function (resb) {
            if (resb.success) {
                render(resb.data);
            }
        },
        complete: function (XHR) {
            if (!rendered) {
                toast(XHR.status === 200 ? XHR.responseJSON.message : XHR.statusText);
            }
        }
    });

    // 修改触发动作
    if (stationName) {
        $('.box-block[data-name="' + stationName + '"]').click();
    }
};

var renderTrain = function (trip) {
// 根据当前站点名, 获取发车表
    var render = function (data) {
            var text, that,
                html = '<div class="box-block block" data-target="#train" data-trip="G1"><span class="trip">G1</span><div class="time">1:11</div></div>',
                html2 = '<div class="box-block more block" data-toggle=".checi">查看更多</div>',
                html3 = '<div class="not-found" style="display: none;">未查询到发车的车次</div>',
                max_num = 19,
                els = {
                    'G': [
                        $('.trip-content[data-name="content"] .gaotie'),
                        $('.trip-content[data-name="content-search"] .gaotie')
                    ],
                    'D': [
                        $('.trip-content[data-name="content"] .dongche'),
                        $('.trip-content[data-name="content-search"] .dongche')
                    ]
                };

            $.each(els, function (key, arr) {
                // 初始化
                $.each(arr, function (idx) {
                    this.empty().append(html3);
                    that = this;
                    // 渲染数据
                    $.each(data[key], function (num) {
                        text = html.replace(/G1/g, this.lineNo).replace('1:11', this.departTime);
                        if (idx === 0) {
                            // 显示页默认最大渲染数
                            if (num < max_num) {
                                that.append(text);
                            }

                            // 显示页追加更多按钮
                            if (num === max_num) {
                                that.append(html2.replace('checi', key === 'G' ? 'gaotie' : 'dongche'));
                            }
                        } else {
                            that.append(text);
                        }
                    });

                    // 是否显示not-found
                    if (that.find('.box-block').length === 0) {
                        that.find('.not-found').show();
                    }
                });
            });
            rendered = true;
        },
        rendered = false;

    // 获取最新发车表
    $.ajax({
        url: '/RailwayService/railwayStation/queryLineStationByStationName.do?stationName=' + obj.startStation,
        type: 'GET',
        dataType: 'json',
        async: false,
        success: function (resb) {
            if (resb.success) {
                // 动态渲染高铁、动车元素
                render(resb.data);
            }
        },
        complete: function (XHR) {
            if (!rendered) {
                toast(XHR.status === 200 ? XHR.responseJSON.message : XHR.statusText);
            }
        }
    });

    // 修改触发动作
    if (trip) {
        var $el = $('.box-block[data-trip="' + trip + '"]');
        if ($el.length !== 0) {
            $el.click();
        } else {
            $('#train').find('i.iconfont').hide();
            $('#train').find('span').text(trip).show();
            $('a.active').next().click();
            renderDestination(obj.destination);
        }
    }
};

$('div.change-type').click(function () {
    $('ul').show();
});

// 切换车次类别
$('ul li').click(function () {
    var $span = $(this).parents('.change-type').find('span'),
        text = $(this).text(),
        value = $span.text();

    if (text !== value) {
        $(this).parents('.weui-flex').find('input').val('').keyup();
        if (text === '高铁G') {
            $('.gaotie').show();
            $('.dongche').hide();
        } else {
            $('.dongche').show();
            $('.gaotie').hide();
        }
    }
    $span.text(text);
});

// 根据站点、车次, 获取终点站列表
var renderDestination = function (destination) {
    var render = function (data) {
            var html_not_found = '<div class="not-found" style="display: none;">未查询到发车的车次</div>';
            $('#terminal .city-content').empty().append(html_not_found);
            $.each(data, function () {
                $('#terminal .city-content').append('' +
                    '<div class="box-block block" data-target="#end" data-name="' + this.station +
                    '" data-id="' + this.cityId + '" data-time="' + this.arriveTime +
                    '" data-abbr="' + this.stationNameAbbr + '" data-spell="' +
                    this.spel + '"><span>' + this.station + '</span></div>');
            });

            // 取消占位class
            $('#terminal .city-content').find('.box-block:not(:nth-last-of-type(n+' + get_last_num(data.length) + '))').removeClass('block');
            rendered = true;
        },
        rendered = false;

    // 获取最新终点站
    $.ajax({
        url: '/RailwayService/railwayStation/getDestinationByStationAndNo.do?',
        type: 'GET',
        data: {stationName: obj.startStation, lineNo: obj.trip},
        dataType: 'json',
        async: false,
        success: function (resb) {
            if (resb.success) {
                render(resb.data);
            }
        },
        complete: function (XHR) {
            if (!rendered) {
                toast(XHR.status === 200 ? XHR.responseJSON.message : XHR.statusText);
            }
        }
    });

    // 修改触发动作
    if (destination) {
        $('.box-block[data-target="#end"][data-name="' + destination + '"]').click();
    }
};

// 点击block切换导航
$('[data-name^="content"]').on('click', '.box-block', function () {
    $(this).addClass('click');
}).on('transitionend', '.box-block', function () {
    var value = $(this).find('span:first-child').text().trim(),
        target = $(this).data('target'),
        $target = $(target),
        toggle = $(this).data('toggle'),
        $span = $target.find('span'),
        $icon = $target.find('i'),
        old_value = $span.text(),
        $a = $target.parents('a'),
        $divs = $a.nextAll().find('.nav-value'),
        $next_spans = $divs.find('span'),
        $next_icons = $divs.find('i.iconfont');

    $(this).removeClass('click');

    // 车次查看全部
    if (toggle) {
        var $trip = $(this).parents('.trip-content'),
            $tripSearch = $trip.next();

        $trip.hide();
        $tripSearch.show().children('div').hide();
        $tripSearch.find(toggle).show();
        return false;
    }

    // 更改导航对应的值
    $span.text(value).show();

    // 值更改时后面的导航值变为icon
    if (value !== old_value) {
        $icon.hide();
        $next_spans.hide();
        $next_icons.show();
    }

    // 导航切换
    $($divs[0]).click();

    // obj 更改
    if (target === '#start') {
        obj['startStation'] = value;
        obj['startStationId'] = $(this).data('stationid');
        delete obj.trip;
        delete obj.destination;
        renderTrain(obj.modify ? obj.trip : undefined);
    } else if (target === '#train') {
        delete obj.destination;
        obj['trip'] = value;
        obj['aboardTime'] = $(this).find('.time').text().trim();
        renderDestination(obj.modify ? obj.destination : undefined);
    } else {
        obj['destination'] = value;
        obj['cityId'] = $(this).data('id');
        obj['arriveTime'] = $(this).data('time');
    }
});

// 模糊查询
function query(el) {
    var $that = $(el),
        value = $that.val().trim().toLowerCase(),
        $div = $that.parents('div.content').find('[data-name="content-search"]'),
        $notFound = $div.find('.not-found'),
        $block = $div.find('.box-block'),
        text = '',
        show = false,
        abbr, spell;

    if (value === "") {
        if ($(this).hasClass('trip')) {
            $div.hide().prev().show();
        }
        $block.filter(':hidden').show();
        show = true;
    } else {
        if ($(el).hasClass('trip')) {
            $div.show().prev().hide();
        }

        // 查询匹配
        $block.hide().each(function () {
            text = $(this).text().trim();
            if ($(this).data('target') === '#train') {
                text = $(this).find('.trip').text().trim().substring(1);
            } else {
                // 特殊属性检查
                try {
                    abbr = $(this).data('abbr').toLowerCase();
                    spell = $(this).data('spell').toLowerCase();
                    if (abbr.indexOf(value) !== -1 || spell.indexOf(value) !== -1) {
                        show = true;
                        return $(this).show();
                    }
                } catch (err) {
                    console.log(err)
                }
            }

            if (text.indexOf(value) !== -1) {
                show = true;
                return $(this).show();
            }
        });
    }

    if (!show) {
        $notFound.show();
    } else {
        $notFound.hide();
    }
}

$('input.weui-input').on('input paste propertychange', function () {
    query(this);
});

// 保存行程
function save_route(data, url) {
    var message;
    $.ajax({
        url: '/RailwayService/travelRoute/add',
        data: JSON.stringify({
            customerName: '',
            customerPhone: '',
            lineNo: data.trip,
            carriageNumber: '',
            seatNumber: '',
            aboardStation: data.startStation,
            arrivedStation: data.destination
        }),
        type: 'POST',
        async: false,
        contentType: 'application/json',
        dataType: 'json',
        success: function (resb) {
            if (resb.success) {
                // 记录中间站点
                data.stations = resb.data.listStationOnTheWay;
            } else {
                message = resb.message;
            }
        },
        error: function (XHR) {
            message = XHR.statusText;
        }
    });

    // 错误处理
    if (message) {
        return $.alert(message, '错误');
    }

    sessionStorage.setItem('route', JSON.stringify(data));
    $.toast('设置成功');
    setTimeout(function () {
        window.location.href = url;
    }, 2000)
}

// 表单提交
$('footer a').click(function () {
    // 判断是否保存行程信息
    if ($(this).data('name') === 'route') {
        if (obj.startStation && obj.trip && obj.destination) {
            obj.init = true;
        } else {
            $.toast('行程设置不正确', 'forbidden');
            return false;
        }

        // 是否从订单页面跳转过来的
        if (sessionStorage.Order) {
            if (obj.startStationId !== sessionStorage.stationId.trim()) {
                $.confirm('该商家所在站点不在该行程中, 是否继续?', '警告', function () {
                    save_route(obj, 'payOrder.html');
                });
            } else {
                save_route(obj, 'payOrder.html');
            }
        } else {
            save_route(obj, 'index.html');
        }
    } else {
        // 是否从订单页面跳转过来的
        if (sessionStorage.Order) {
            $.confirm('行程尚未设置完成是否跳过?', '提示', function () {
                window.location.href = 'payOrder.html';
            });
        } else {
            window.location.href = 'index.html';
        }
    }
});

// 聚焦隐藏提示
$('input').focus(function () {
    obj.text = $(this).attr('placeholder');
    $(this).attr('placeholder', '');
}).blur(function () {
    $(this).attr('placeholder', obj.text);
});

// 判断是新行程还是修改行程
if (!sessionStorage.route) {
    getCurrentStation();
} else {
    obj = JSON.parse(sessionStorage.route);
    obj.modify = true;
    // 渲染行程记录
    renderAllStations();
    renderTrain();
    renderDestination();
    $('i.icon-xiala').hide();
    $('#start span').text(obj.startStation).show();
    $('#train span').text(obj.trip).show();
    $('#end span').text(obj.destination).click().show();
    delete obj.modify;
}