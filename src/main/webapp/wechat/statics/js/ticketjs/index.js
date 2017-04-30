//页面加载就执行
$(document).ready(function () {
    var userInfo;
    var telphone = "18229899455";
    if (sessionStorage.userInfo !== undefined) {
        userInfo = JSON.parse(sessionStorage.userInfo);
        var data = {
            userId: userInfo.userId
        };
        getOrderHistory(data);
    }
    if (sessionStorage.infos !== undefined) {

        var rep = JSON.parse(sessionStorage.infos);

        console.log(rep);
        $("#startDate").text(rep.showDate);
        $("#weeks").text(rep.week);
        $("#start-City span").text(sessionStorage.fromStation);
        $("#end-City span").text(sessionStorage.toStation);

        var obj;

        if ($('.weui-switch').prop('checked')) {
            obj = JSON.parse(sessionStorage.infos);
            obj['switchOn'] = true;
        }
        else {
            obj = JSON.parse(sessionStorage.infos);
            obj['switchOn'] = false;

        }
        sessionStorage.setItem('infos', JSON.stringify(obj));
        sessionStorage.setItem("telphone", telphone);
    } else {
        var date = new Date();
        //var times=nowDate.pattern("yyyy-MM-dd hh:mm:ss");
        var timeStamp = date.getTime();
        //var cc=timeStamp.getDay();
        //console.log(cc);
        //var showdate=(parseInt(nowDate) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');
        var startDate = date.getFullYear() + "-" + ("0" + (date.getMonth() + 1)).slice(-2) + "-" + ("0" + date.getDate()).slice(-2);
        var splitDate = startDate.split("-");
        var startDate = splitDate[0] + '-' + splitDate[1] + '-' + splitDate[2];
        var showStartDate = (splitDate[1].replace(/\b(0+)/gi, "")) + '月' + splitDate[2] + '日';
        var fullTime = (splitDate[0] + '年' + splitDate[1].replace(/\b(0+)/gi, "")) + '月' + splitDate[2] + '日';
        switch (date.getDay()) {
            case 0:
                weeks = "星期天";
                break
            case 1:
                weeks = "星期一";
                break
            case 2:
                weeks = "星期二";
                break
            case 3:
                weeks = "星期三";
                break
            case 4:
                weeks = "星期四";
                break
            case 5:
                weeks = "星期五";
                break
            case 6:
                weeks = "星期六";
                break
        }
        var obj = {
            week: '今天',
            weeks: weeks,
            timeStamp: timeStamp,
            startDate: startDate,
            fullTime: fullTime,
            showDate: showStartDate,
            switchOn: false
        };

        sessionStorage.setItem('infos', JSON.stringify(obj));
        sessionStorage.setItem("fromStation", '请选择');
        sessionStorage.setItem("toStation", '请选择');
        sessionStorage.setItem("telphone", telphone);
        $("#startDate").text(obj.showDate);
        $("#weeks").text(obj.week);
        $("#start-City span").text(obj.fromStation);
        $("#end-City span").text(obj.toStation);
    }
});

//多个div的点击事件
$(document).on('click', '.dateTime', function () {
    //var id = $(this).data('id');

    window.location.href = "dateTime.html";
});
$('.weui-btn-area .btn_orange').click(function () {
    window.sessionStorage.removeItem('data');
    window.location.href = "trainNum.html";

});

// 交换两个城市
$('#switch-City').click(function () {
    var $start = $('#start-City span'),
        $end = $('#end-City span'),
        start = $start.text(),
        end = $end.text();

    $start.fadeOut('normal',
        function () {
            $start.text(end).fadeIn();
            sessionStorage.setItem("fromStation", end);
        }
    );

    $end.fadeOut('normal',
        function () {
            $end.text(start).fadeIn();
            sessionStorage.setItem("toStation", start);
        }
    );
});

// 跳转选择城市页面
$('#start-City,#end-City').click(function () {
    window.location.href = "city.html";
});

$('.weui-switch').click(function () {
    var obj;
    if ($('.weui-switch').prop('checked')) {
        obj = JSON.parse(sessionStorage.infos);
        obj['switchOn'] = true;
    }
    else {
        obj = JSON.parse(sessionStorage.infos);
        obj['switchOn'] = false;

    }
    ;
    sessionStorage.setItem('infos', JSON.stringify(obj));
});
/*
 * ajax函数
 * @param url      url地址
 * @param param   需要提交的数据，对象格式
 * @param type    类型post或get，默认get
 */
function ajax(url, param, type) {
    // 利用了jquery延迟对象回调的方式对ajax封装，使用done()，fail()，always()等方法进行链式回调操作
    return $.ajax({
        url: url,
        data: param || {},
        type: type || 'GET'
    });
};

function getOrderHistory(data) {
    ajax('/RailwayService/ticketOrder/getHistoryTravels', data, "POST").done(function (resp) {
        var data = $.parseJSON(resp);

        if (data.success == true) {
            if (data.data.length != 0) {
                var count = 0;
                var str = '';
                for (var i = 0; i < data.data.length; i++) {
                    if (data.data[i].isHistoryDelete != '1') {
                        if (count < 2) {
                            count++;
                            str = str + '<div class="weui-flex__item"><span class="form">' + data.data[i].arrivedStation + '</span>' + "--" + '<span class="to">' + data.data[i].aboardStation + '</span></div>';
                        }
                    }
                }
                if (count > 0) {
                    $('.history').prepend(str);
                    $('.history').append('<div class="lishi"><span>删除历史</span></div>');
                } else {
                    $('.history').prepend('<div class="weui-flex__item"><span class="from">' + "没有找到历史记录 " + '</span></div>');
                }
            } else {
                $('.history').prepend('<div class="weui-flex__item"><span class="from">' + "没有找到历史记录 " + '</span></div>');
            }
        }
    });
};


$(document).on('click', '.history .lishi', function () {
    if (sessionStorage.userInfo != undefined) {

        var userInfo = JSON.parse(sessionStorage.userInfo);
        var data = {
            userId: userInfo.userId
        };
        deleteHistoryOrders(data);
    }
});
$(document).on('click', '.history .weui-flex__item', function () {
    var fromStation = $(this).find('.form').text();
    var toStation = $(this).find('.to').text();
    sessionStorage.setItem('fromStation', fromStation);
    sessionStorage.setItem('toStation', toStation);

    $("#start-City").text(sessionStorage.fromStation);
    $("#end-City").text(sessionStorage.toStation);
});

function deleteHistoryOrders(data) {

    ajax('/RailwayService/ticketOrder/deleteHistoryOrders', data, "POST").done(function (resp) {
        var data = $.parseJSON(resp);
        console.log(data);
        if (data.success == true) {
            $.toast("删除历史成功");
            window.location.href = "index.html";
        }
    });
}