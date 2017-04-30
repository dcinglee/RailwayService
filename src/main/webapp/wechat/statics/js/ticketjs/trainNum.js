
$.showLoading();
var rep = JSON.parse(sessionStorage.infos),
    checkTime = Date.parse(new Date()),
    ticket =  $.fn.ticket,
    param = {
        data: {
            fromStation: sessionStorage.fromStation,
            toStation: sessionStorage.toStation,
            startDate:rep.startDate
        },
        url: "http://thisa:8888/query_train"
    };
console.log(checkTime);

//对请求的ajax数据进行存储
$(document).ready(function(){
    var focusDate = rep.showDate,
        focusWeek = rep.week;
    $('.today').text(focusDate +' '+ focusWeek);
    if(sessionStorage.data != undefined){
        loadCache();
        $.hideLoading();
    }else{
        getSelect(param);
    };
});

function getSelect(param) {
        ticket.doAjax(param).done(function(resp){
        var data = resp;
        console.log(resp);
        if(data.data!=null&&data.data.length != 0){
            $.hideLoading();
            var listdata = data.data;
            sessionStorage.setItem('data', JSON.stringify(listdata));
            var goodsLength = data.data.length;
            $(".trainList").empty();
            for(var i = 0;i<goodsLength;i++){
                var  switcher = rep.switchOn;
                console.log(switcher);
                if(switcher){
                    if(data.data[i].queryLeftNewDTO.station_train_code.indexOf('G') > -1){
                        var template = $("#gaoTicketsList").clone().html().replace('-1', i);
                        var html = template.replace("G77",data.data[i].queryLeftNewDTO.station_train_code);
                        html = html.replace("changshan",data.data[i].queryLeftNewDTO.from_station_name);
                        html = html.replace("shenzhenb",data.data[i].queryLeftNewDTO.to_station_name);
                        html = html.replace("2时55分",data.data[i].queryLeftNewDTO.lishi);
                        html = html.replace("10:21",data.data[i].queryLeftNewDTO.start_time);
                        html = html.replace("20:21",data.data[i].queryLeftNewDTO.arrive_time);
                        html = html.replace("11张",data.data[i].queryLeftNewDTO.swz_num);
                        html = html.replace("12张",data.data[i].queryLeftNewDTO.zy_num);
                        html = html.replace("13张",data.data[i].queryLeftNewDTO.ze_num);
                        html = html.replace("10张",data.data[i].queryLeftNewDTO.tz_num);
                        html = html.replace("no1",data.data[i].queryLeftNewDTO.from_station_no);
                        html = html.replace("no2",data.data[i].queryLeftNewDTO.to_station_no);
                        html = html.replace("GO",data.data[i].queryLeftNewDTO.train_no);
                        $(".trainList").append(html);
                        if(data.data[i].queryLeftNewDTO.start_station_name != data.data[i].queryLeftNewDTO.from_station_name){
                            $('.startLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                            $('.startLogo').eq(i).text('过');
                        };
                        if(data.data[i].queryLeftNewDTO.end_station_name != data.data[i].queryLeftNewDTO.to_station_name){
                            $('.endLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                            $('.endLogo').eq(i).text('过');
                        };
                    };
                }else{
                    if(data.data[i].queryLeftNewDTO.station_train_code.indexOf('G') > -1){
                        var template = $("#gaoTicketsList").clone().html().replace('-1', i);
                        var html = template.replace("G77",data.data[i].queryLeftNewDTO.station_train_code);
                        html = html.replace("changshan",data.data[i].queryLeftNewDTO.from_station_name);
                        html = html.replace("shenzhenb",data.data[i].queryLeftNewDTO.to_station_name);
                        html = html.replace("2时55分",data.data[i].queryLeftNewDTO.lishi);
                        html = html.replace("10:21",data.data[i].queryLeftNewDTO.start_time);
                        html = html.replace("20:21",data.data[i].queryLeftNewDTO.arrive_time);
                        html = html.replace("11张",data.data[i].queryLeftNewDTO.swz_num);
                        html = html.replace("12张",data.data[i].queryLeftNewDTO.zy_num);
                        html = html.replace("13张",data.data[i].queryLeftNewDTO.ze_num);
                        html = html.replace("10张",data.data[i].queryLeftNewDTO.tz_num);
                        html = html.replace("no1",data.data[i].queryLeftNewDTO.from_station_no);
                        html = html.replace("no2",data.data[i].queryLeftNewDTO.to_station_no);
                        html = html.replace("GO",data.data[i].queryLeftNewDTO.train_no);
                        $(".trainList").append(html);
                        if(data.data[i].queryLeftNewDTO.start_station_name != data.data[i].queryLeftNewDTO.from_station_name){
                            $('.startLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                            $('.startLogo').eq(i).text('过');
                        };
                        if(data.data[i].queryLeftNewDTO.end_station_name != data.data[i].queryLeftNewDTO.to_station_name){
                            $('.endLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                            $('.endLogo').eq(i).text('过');
                        };
                    }else{
                        var template = $("#ticketsList").clone().html().replace('-1', i);
                        var html = template.replace("G77",data.data[i].queryLeftNewDTO.station_train_code);
                        html = html.replace("changshan",data.data[i].queryLeftNewDTO.from_station_name);
                        html = html.replace("shenzhenb",data.data[i].queryLeftNewDTO.to_station_name);
                        html = html.replace("2时55分",data.data[i].queryLeftNewDTO.lishi);
                        html = html.replace("10:21",data.data[i].queryLeftNewDTO.start_time);
                        html = html.replace("20:21",data.data[i].queryLeftNewDTO.arrive_time);
                        html = html.replace("11张",data.data[i].queryLeftNewDTO.rw_num);
                        html = html.replace("12张",data.data[i].queryLeftNewDTO.yw_num);
                        html = html.replace("13张",data.data[i].queryLeftNewDTO.yz_num);
                        html = html.replace("14张",data.data[i].queryLeftNewDTO.wz_num);
                        html = html.replace("10张",data.data[i].queryLeftNewDTO.rz_num);
                        html = html.replace("no1",data.data[i].queryLeftNewDTO.from_station_no);
                        html = html.replace("no2",data.data[i].queryLeftNewDTO.to_station_no);
                        html = html.replace("GO",data.data[i].queryLeftNewDTO.train_no);
                        $(".trainList").append(html);
                        if(data.data[i].queryLeftNewDTO.start_station_name != data.data[i].queryLeftNewDTO.from_station_name){
                            $('.startLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                            $('.startLogo').eq(i).text('过');
                        };
                        if(data.data[i].queryLeftNewDTO.end_station_name != data.data[i].queryLeftNewDTO.to_station_name){
                            $('.endLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                            $('.endLogo').eq(i).text('过');
                        };
                    };
                };
            };
            $(".selectList .select-box").eq(goodsLength/2-1).css("margin-right","0");
        };
        if(data.data.length == 0){
            $.hideLoading();
            $('.trainList').text("选择的查询日期不在预售日期范围内");
        };
    });
};

function loadCache() {
    $('.trainList').empty();
    var cache = JSON.parse(sessionStorage.data);
    console.log(cache[0]);
    var cacheLength = cache.length;
    for(var i = 0; i<cacheLength; i++){
        var  switcher = rep.switchOn;
        console.log(switcher);
        if(switcher){
            if(cache[i].queryLeftNewDTO.station_train_code.indexOf('G') > -1){
                var template = $("#gaoTicketsList").clone().html().replace('-1', i);
                var html = template.replace("G77",cache[i].queryLeftNewDTO.station_train_code);
                html = html.replace("changshan",cache[i].queryLeftNewDTO.from_station_name);
                html = html.replace("shenzhenb",cache[i].queryLeftNewDTO.to_station_name);
                html = html.replace("2时55分",cache[i].queryLeftNewDTO.lishi);
                html = html.replace("10:21",cache[i].queryLeftNewDTO.start_time);
                html = html.replace("20:21",cache[i].queryLeftNewDTO.arrive_time);
                html = html.replace("11张",cache[i].queryLeftNewDTO.swz_num);
                html = html.replace("12张",cache[i].queryLeftNewDTO.zy_num);
                html = html.replace("13张",cache[i].queryLeftNewDTO.ze_num);
                html = html.replace("10张",cache[i].queryLeftNewDTO.tz_num);
                html = html.replace("no1",cache[i].queryLeftNewDTO.from_station_no);
                html = html.replace("no2",cache[i].queryLeftNewDTO.to_station_no);
                html = html.replace("GO",cache[i].queryLeftNewDTO.train_no);
                $(".trainList").append(html);
                if(cache[i].queryLeftNewDTO.start_station_name != cache[i].queryLeftNewDTO.from_station_name){
                    $('.startLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                    $('.startLogo').eq(i).text('过');
                };
                if(cache[i].queryLeftNewDTO.end_station_name != cache[i].queryLeftNewDTO.to_station_name){
                    $('.endLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                    $('.endLogo').eq(i).text('过');
                };
            };
        }else{
            if(cache[i].queryLeftNewDTO.station_train_code.indexOf('G') > -1){
                var template = $("#gaoTicketsList").clone().html().replace('-1', i);
                var html = template.replace("G77",cache[i].queryLeftNewDTO.station_train_code);
                html = html.replace("changshan",cache[i].queryLeftNewDTO.from_station_name);
                html = html.replace("shenzhenb",cache[i].queryLeftNewDTO.to_station_name);
                html = html.replace("2时55分",cache[i].queryLeftNewDTO.lishi);
                html = html.replace("10:21",cache[i].queryLeftNewDTO.start_time);
                html = html.replace("20:21",cache[i].queryLeftNewDTO.arrive_time);
                html = html.replace("11张",cache[i].queryLeftNewDTO.swz_num);
                html = html.replace("12张",cache[i].queryLeftNewDTO.zy_num);
                html = html.replace("13张",cache[i].queryLeftNewDTO.ze_num);
                html = html.replace("10张",cache[i].queryLeftNewDTO.tz_num);
                html = html.replace("no1",cache[i].queryLeftNewDTO.from_station_no);
                html = html.replace("no2",cache[i].queryLeftNewDTO.to_station_no);
                html = html.replace("GO",cache[i].queryLeftNewDTO.train_no);
                $(".trainList").append(html);
                if(cache[i].queryLeftNewDTO.start_station_name != cache[i].queryLeftNewDTO.from_station_name){
                    $('.startLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                    $('.startLogo').eq(i).text('过');
                };
                if(cache[i].queryLeftNewDTO.end_station_name != cache[i].queryLeftNewDTO.to_station_name){
                    $('.endLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                    $('.endLogo').eq(i).text('过');
                };
            }else{
                var template = $("#ticketsList").clone().html().replace('-1', i);
                var html = template.replace("G77",cache[i].queryLeftNewDTO.station_train_code);
                html = html.replace("changshan",cache[i].queryLeftNewDTO.from_station_name);
                html = html.replace("shenzhenb",cache[i].queryLeftNewDTO.to_station_name);
                html = html.replace("2时55分",cache[i].queryLeftNewDTO.lishi);
                html = html.replace("10:21",cache[i].queryLeftNewDTO.start_time);
                html = html.replace("20:21",cache[i].queryLeftNewDTO.arrive_time);
                html = html.replace("11张",cache[i].queryLeftNewDTO.rw_num);
                html = html.replace("12张",cache[i].queryLeftNewDTO.yw_num);
                html = html.replace("13张",cache[i].queryLeftNewDTO.yz_num);
                html = html.replace("14张",cache[i].queryLeftNewDTO.wz_num);
                html = html.replace("10张",cache[i].queryLeftNewDTO.rz_num);
                html = html.replace("no1",cache[i].queryLeftNewDTO.from_station_no);
                html = html.replace("no2",cache[i].queryLeftNewDTO.to_station_no);
                html = html.replace("GO",cache[i].queryLeftNewDTO.train_no);
                $(".trainList").append(html);
                if(cache[i].queryLeftNewDTO.start_station_name != cache[i].queryLeftNewDTO.from_station_name){
                    $('.startLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                    $('.startLogo').eq(i).text('过');
                };
                if(cache[i].queryLeftNewDTO.end_station_name != cache[i].queryLeftNewDTO.to_station_name){
                    $('.endLogo').eq(i).css({"color":"#a0a0a0","border":"1px solid #a0a0a0"});
                    $('.endLogo').eq(i).text('过');
                };
            };
        };
    };
};

//跳转到车票详情
$(document).on('click', 'li',function(){
    location.href="trainDetail.html";
    var obj = {
        train_no: $(this).find('p[name = "train_no"]').text(),
        from_station_no: $(this).find('p[name = "fromStation"]').text(),
        to_station_no: $(this).find('p[name = "toStation"]').text(),
        seat_types: "134OMP9",
        start_time: $(this).find('p[class = "startTime"]').text(),
        arrive_time: $(this).find('p[class = "endTime"]').text(),
        from_station_name: $(this).find('p[name = "from"]').text(),
        to_station_name: $(this).find('p[name = "to"]').text(),
        station_train_code: $(this).find('p[class = "trainNum"]').text(),
        rw_num: $(this).find('span[name = "RW"]').text(),
        yw_num: $(this).find('span[name = "YW"]').text(),
        rz_num: $(this).find('span[name = "RZ"]').text(),
        yz_num: $(this).find('span[name = "YZ"]').text(),
        wz_num: $(this).find('span[name = "WZ"]').text(),
        swz_num: $(this).find('span[name = "SW"]').text(),
        tz_num: $(this).find('span[name = "TD"]').text(),
        zy_num: $(this).find('span[name = "YD"]').text(),
        ze_num: $(this).find('span[name = "ED"]').text()
    };
    sessionStorage.setItem('ind',$(this).data('idx'));
    sessionStorage.setItem('paras',JSON.stringify(obj));
});

$(document).
on('click', '.weui-flex__item', function() {
    window.location.href = "dateTimeTwo.html";
});


$(document).
on('click', '.iconRight, .iconLeft', function() {
    $(".trainList").empty();
    $.showLoading();
    var lastDay = Date.parse(rep.startDate),
          direct = $(this).hasClass('iconRight') ? 1 : -1,
          detract = new Date(lastDay + 24 * 60 * 60 * 1000 * direct),
          changeDay = [detract.getFullYear(), String(detract.getMonth() + 1).padStart(2,'0'), String(detract.getDate()).padStart(2,'0')];
    console.log(lastDay);
    var changed =  changeDay[0] + '-' + changeDay[1] + '-' + changeDay[2],
          changeTo = new Date(changed),
          date = changed.split('-'),
          dates = date[1].replace(/\b(0+)/gi,"") + '月' + date[2].replace(/\b(0+)/gi,"") + '日';
    console.log(dates);
    var weekday = ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
          weeks = weekday[changeTo.getDay()];
    console.log(weeks);
    rep.startDate=changed;
    rep.week=weeks;
    rep.showDate=dates;
    sessionStorage.setItem('infos',JSON.stringify(rep));
    var focusDate = rep.showDate,
        focusWeek = rep.week;
    $('.today').text(focusDate +' '+ focusWeek);
    param.data.startDate = rep.startDate;
    getSelect(param);
});


