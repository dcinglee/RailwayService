
var rep = JSON.parse(sessionStorage.infos),
      ind = JSON.parse(sessionStorage.ind),
      par = JSON.parse(sessionStorage.paras),
      dat = JSON.parse(sessionStorage.data),
      ticket =  $.fn.ticket,
      param = {
            lineNo: par.station_train_code,
            aboardStation: par.from_station_name,
            arrivedStation: par.to_station_name
       },
      params = {
            lineNo: dat[ind].queryLeftNewDTO.station_train_code
       };

$('.today').text(rep.showDate + rep.week);
$('.startTime').text(dat[ind].queryLeftNewDTO.start_time);
$('.endTime').text(dat[ind].queryLeftNewDTO.arrive_time);
$('.startStation').text(dat[ind].queryLeftNewDTO.from_station_name);
$('.endStation').text(dat[ind].queryLeftNewDTO.to_station_name);
$('.trainNo').text(dat[ind].queryLeftNewDTO.station_train_code);
$('.day').text(rep.showDate);

getTicketInfos(param);
// $(document).ready(function(){
//
// });

function ajax(url, param, type) {
    // 利用了jquery延迟对象回调的方式对ajax封装，使用done()，fail()，always()等方法进行链式回调操作
    // var data = {}
    return $.ajax({
        url: url,
        data: param,
        type: type || 'GET',
    });
};

function getTicketInfos(param) {
    ajax("../../ticketOrder/getTicketByLineNoAndStation",param,"POST").done(function(resp){
        var data = JSON.parse(resp),
              trainNo = $('.trainNo').text();
        console.log(resp);
        console.log(data);
        if(data.data!=null&&data.data.length != 0){
            var goodsLength = data.data.length;
            $('.seatDetail').empty();
            if(trainNo.indexOf('G') > -1){
                var template = $('#seatListGao').clone().html();
                var html = template.replace('￥600',data.data.businessSeat);
                html = html.replace('￥500',data.data.specialSeat);
                html = html.replace('￥400',data.data.firstSeat);
                html = html.replace('￥300',data.data.secondSeat);
                html = html.replace('X张',par.swz_num);
                html = html.replace('Y张',par.tz_num);
                html = html.replace('Q张',par.zy_num);
                html = html.replace('QQ张',par.ze_num);
                $('.seatDetail').append(html);
                if(par.swz_num == '无' || par.swz_num < 88 || par.swz_num == '--'){
                    $('.train_data:eq(0)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(0)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>')
                };
                if(par.tz_num == '无' || par.tz_num < 88 || par.tz_num == '--'){
                    $('.train_data:eq(1)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(1)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>')
                };
                if(par.zy_num == '无' || par.zy_num < 88 || par.zy_num == '--'){
                    $('.train_data:eq(2)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(2)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>')
                };
                if(par.ze_num == '无' || par.ze_num < 88 || par.ze_num == '--'){
                    $('.train_data:eq(3)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(3)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                };
            }else{
                var template = $('#seatList').clone().html();
                var html = template.replace('￥600',data.data.softBerth);
                html = html.replace('￥500',data.data.hardBerth);
                html = html.replace('￥400',data.data.softSeat);
                html = html.replace('￥300',data.data.hardSeat);
                html = html.replace('￥200',data.data.noSeat);
                html = html.replace('X张',par.rw_num);
                html = html.replace('Y张',par.yw_num);
                html = html.replace('Q张',par.rz_num);
                html = html.replace('QQ张',par.yz_num);
                html = html.replace('QQQ张',par.wz_num);
                $('.seatDetail').append(html);
                if(par.rw_num == '无' || par.rw_num < 88 || par.rw_num == '--'){
                    $('.train_data:eq(0)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(0)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                };
                if(par.yw_num == '无' || par.yw_num < 88 || par.yw_num == '--'){
                    $('.train_data:eq(1)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(1)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                };
                if(par.rz_num == '无' || par.rz_num < 88 || par.rz_num == '--'){
                    $('.train_data:eq(2)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(2)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                };
                if(par.yz_num == '无' || par.yz_num < 88 || par.yz_num == '--'){
                    $('.train_data:eq(3)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(3)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                };
                if(par.wz_num == '无' || par.wz_num < 88 || par.wz_num == '--'){
                    $('.train_data:eq(4)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                }else{
                    $('.train_data:eq(4)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                };
                if(data.data.A4 == undefined){
                    $('.train_data:eq(0)').hide();
                };
            };
            $(".selectList .select-box").eq(goodsLength/2-1).css("margin-right","0");
        };
    });
};

function getSelect(param) {
    ajax(ticket.url.queryTrain,param,"POST").done(function(resp){
        var data = resp;
        console.log(resp);
        if(data.data!=null&&data.data.length != 0){
            var goodsLength = data.data.length;
            for(var i = 0;i<goodsLength;i++){
                var  switcher = rep.switchOn;
                console.log(switcher);
                if(data.data[i].queryLeftNewDTO.station_train_code == par.station_train_code){
                    if(data.data[i].queryLeftNewDTO.station_train_code.indexOf('G') > -1){
                        $('.train_data:eq(0)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.swz_num);
                        $('.train_data:eq(1)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.tz_num);
                        $('.train_data:eq(2)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.zy_num);
                        $('.train_data:eq(3)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.ze_num);
                        var swz = data.data[i].queryLeftNewDTO.swz_num,
                              tz = data.data[i].queryLeftNewDTO.tz_num,
                              zy = data.data[i].queryLeftNewDTO.zy_num,
                              ze = data.data[i].queryLeftNewDTO.ze_num;
                        if(swz == '无' || swz < 88 || swz == '--'){
                            $('.train_data:eq(0)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(0)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>')
                        };
                        if(tz == '无' || tz < 88 || tz == '--'){
                            $('.train_data:eq(1)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(1)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>')
                        };
                        if(zy == '无' || zy < 88 || zy == '--'){
                            $('.train_data:eq(2)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(2)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>')
                        };
                        if(ze == '无' || ze < 88 || ze == '--'){
                            $('.train_data:eq(3)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(3)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                        };
                        console.log(data.data[i].queryLeftNewDTO.station_train_code);
                    }else{
                        $('.train_data:eq(0)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.rw_num);
                        $('.train_data:eq(1)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.yw_num);
                        $('.train_data:eq(2)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.rz_num);
                        $('.train_data:eq(3)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.yz_num);
                        $('.train_data:eq(3)').find('li:eq(2)').text(data.data[i].queryLeftNewDTO.wz_num);
                        var rw = data.data[i].queryLeftNewDTO.rw_num,
                              yw = data.data[i].queryLeftNewDTO.yw_num,
                              rz =  data.data[i].queryLeftNewDTO.rz_num,
                              yz = data.data[i].queryLeftNewDTO.yz_num,
                              wz = data.data[i].queryLeftNewDTO.wz_num;
                        if(rw == '无' || rw < 88 || rw == '--'){
                            $('.train_data:eq(0)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(0)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                        };
                        if(yw == '无' || yw < 88 || yw == '--'){
                            $('.train_data:eq(1)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(1)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                        };
                        if(rz == '无' || rz < 88 || rz == '--'){
                            $('.train_data:eq(2)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(2)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                        };
                        if(yz == '无' || yz < 88 || yz == '--'){
                            $('.train_data:eq(3)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(3)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                        };
                        if(wz== '无' || wz < 88 || wz == '--'){
                            $('.train_data:eq(4)').find('li:eq(3)').html('<a href="javascript:void(0);">抢票</a>');
                        }else{
                            $('.train_data:eq(4)').find('li:eq(4)').html('<a href="javascript:void(0);">预定</a>');
                        };
                    };
                };
            };
            $(".selectList .select-box").eq(goodsLength/2-1).css("margin-right","0");
        };
    });
};

function timeList(params) {
    ajax("/railwayStation/getLineStationBylineNo.do",params,"POST").done(function(resp){
        var data = JSON.parse(resp);
        if(data.data != null&&data.data.length != 0){
          var goodsLength = data.data.length;
            $('.train_table').empty();
            var template = $('#trainTimeList').clone().html();
            $('.train_table').append(template);
            for(var i = 0;i< goodsLength;i++){
                var templates = $('#timeList').clone().html(),
                      html = templates.replace('1',data.data[i].sortNo);
                html = html.replace('武汉',data.data[i].station);
                html = html.replace('始发',data.data[i].arriveTime == "----"?'始发站':data.data[i].arriveTime);
                console.log(typeof data.data[i].departTime);
                console.log(toString(data.data[i].arriveTime));
                html = html.replace('08:00',data.data[i].departTime == data.data[i].arriveTime?'终点站':data.data[i].departTime);
                html = html.replace('——',data.data[i].stopTime);
                $('.train_table').append(html);
            };
        };
    });
};

$('#timeTable').on('click',function(){
    $('#time_show').fadeIn(200);
    timeList(params);
});
$('.alert .train_mask, .alert .train_dialog').on('click',function(){
    $('.alert').fadeOut(200);
});
$('#buyImg').on('click',function(){
    $('#buyQu_show').fadeIn(200);
    if($('#buyQu_show .content').height()>$(window).height()){
        $('#buyQu_show .content').css('overflowY','scroll');
//            $('#buyQu_show .content').css('height',$('#buyQu_show .train_dialog').height());
    }
});
$('.btn_buy').on('click',function(){
    if($(this).hasClass('btn_book')){
        location.href='trainBook.html';
    }else{
        location.href='grabTicket.html';
    }
});

$(document).
on('click', '.weui-flex__item', function() {
    window.location.href = "dateTimeDetail.html";
});

$(document).
on('click', '.iconLeft', function() {
    $('.buy,.book').empty();
    var lastDay = Date.parse(rep.startDate),
        detract = new Date(lastDay - 3600*24*1000).toLocaleString(),
        detract_Day = detract.split(' '),
        changeDay = detract_Day[0].split('/');
    if(changeDay[1]<10){
        changeDay[1] = '0'+changeDay[1]
    };
    if(changeDay[2]<10){
        changeDay[2] = '0'+changeDay[2]
    };
    var changed =  changeDay[0] + '-' + changeDay[1] + '-' + changeDay[2],
        changeToo = new Date(changed),
        date = changed.split('-'),
        dates = date[1].replace(/\b(0+)/gi,"") + '月' + date[2].replace(/\b(0+)/gi,"") + '日';
    console.log(dates);
    var weekday=new Array(7);
        weekday[0]="星期日",
        weekday[1]="星期一",
        weekday[2]="星期二",
        weekday[3]="星期三",
        weekday[4]="星期四",
        weekday[5]="星期五",
        weekday[6]="星期六";
    var weeks = weekday[changeToo.getDay()];
    console.log(weeks);
    rep.startDate = changed;
    rep.week = weeks;
    rep.showDate = dates;
    sessionStorage.setItem('infos',JSON.stringify(rep));
    var focusDate = rep.showDate,
        focusWeek = rep.week;
    $('.today').text(focusDate +' '+ focusWeek);
    $('.day').text(focusDate);
    var param = {
        fromStation: sessionStorage.fromStation,
        toStation: sessionStorage.toStation,
        startDate:rep.startDate
    };
    getSelect(param);
});

$(document).
on('click', '.iconRight', function() {
    $('.buy,.book').empty();
    var lastDay = Date.parse(rep.startDate),
        detract = new Date(lastDay + 3600*24*1000).toLocaleString(),
        detract_Day = detract.split(' '),
        changeDay = detract_Day[0].split('/');
    if(changeDay[1]<10){
        changeDay[1] = '0'+changeDay[1]
    };
    if(changeDay[2]<10){
        changeDay[2] = '0'+changeDay[2]
    };
    var changed =  changeDay[0] + '-' + changeDay[1] + '-' + changeDay[2],
        changeToo = new Date(changed),
        date = changed.split('-'),
        dates = date[1].replace(/\b(0+)/gi,"") + '月' + date[2].replace(/\b(0+)/gi,"") + '日';
    console.log(dates);
    var weekday=new Array(7);
        weekday[0]="星期日",
        weekday[1]="星期一",
        weekday[2]="星期二",
        weekday[3]="星期三",
        weekday[4]="星期四",
        weekday[5]="星期五",
        weekday[6]="星期六";
    var weeks = weekday[changeToo.getDay()];
    console.log(weeks);
    rep.startDate = changed;
    rep.week = weeks;
    rep.showDate = dates;
    sessionStorage.setItem('infos',JSON.stringify(rep));
    var focusDate = rep.showDate,
          focusWeek = rep.week;
    $('.today').text(focusDate +' '+ focusWeek);
    $('.day').text(focusDate);
    var param = {
        fromStation: sessionStorage.fromStation,
        toStation: sessionStorage.toStation,
        startDate:rep.startDate
    };
    getSelect(param);
});

$(document).
on('click', '.train_data .buy a' ,function () {
    rep.seatStyle = $(this).parent().parent().find('li').eq(0).text();
    rep.price = $(this).parent().parent().find('li').eq(1).text();
    rep.seatStyle_code = $(this).parent().parent().find('li').eq(5).text();
    sessionStorage.setItem('infos' , JSON.stringify(rep));
    window.location.href = 'grabTicket.html';
});

$(document).
on('click', '.train_data .book a' ,function () {
    rep.seatStyle = $(this).parent().parent().find('li').eq(0).text();
    rep.price = $(this).parent().parent().find('li').eq(1).text();
    rep.seatStyle_code = $(this).parent().parent().find('li').eq(5).text();
    sessionStorage.setItem('infos' , JSON.stringify(rep));
    window.location.href = 'trainBook.html';
});