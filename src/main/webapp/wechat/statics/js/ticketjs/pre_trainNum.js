/**
 * Created by xz on 2017/4/14.
 */
var show = function (html, className) {
    className = className || "";
    var mask = $("<div class='weui-mask_transparent'></div>").appendTo(document.body);

    var tpl = '<div class="weui-toast ' + className + '">' + html + '</div>';
    var dialog = $(tpl).appendTo(document.body);

    dialog.show();
    dialog.addClass("weui-toast--visible");
};

var hide = function () {
    $(".weui-mask_transparent").remove();
    $(".weui-toast--visible").removeClass("weui-toast--visible");
};

$.showLoading = function (text) {
    var html = '<div class="weui_loading">';
    html += '<i class="weui-loading weui-icon_toast"></i>';
    html += '</div>';
    html += '<p class="weui-toast_content">' + (text || "数据加载中") + '</p >';
    show(html, 'weui_loading_toast');
}

$.hideLoading = function () {
    hide();
};

function getTrain(arr) {
    for(var i = 0;i<arr.length;i++){
        var template = $("#trainNum").clone().html();
        var trainData = arr[i].queryLeftNewDTO;
        var html = template.replace("trainNo1",trainData.station_train_code);
        html = html.replace("city1",trainData.from_station_name)
            .replace("city2",trainData.to_station_name)
            .replace("usedTime",trainData.lishi)
            .replace("startTime",trainData.start_time)
            .replace("endTime1",trainData.arrive_time)
            .replace("idList",'s'+i).replace("idList",'s'+i);
        if(trainData.station_train_code.indexOf('G')>-1||trainData.station_train_code.indexOf('D')>-1||trainData.station_train_code.indexOf('C')>-1){
            html = html.replace("seat1","商务座："+trainData.swz_num)
                .replace("seat2","一等座："+trainData.zy_num)
                .replace("seat3","二等座："+trainData.ze_num)
                .replace("seat4","无座："+trainData.wz_num);
        }else{
            html = html.replace("seat1","软卧："+trainData.rw_num)
                .replace("seat2","硬卧："+trainData.yw_num)
                .replace("seat3","硬座："+trainData.yz_num)
                .replace("seat4","无座："+trainData.wz_num);
        };
        $(".trainList").append(html);
        // $.hideLoading();
        // $(".trainList li").eq(i)
        //将已选择的车次选中
        if(sessionStorage.getItem('trainInfo')){
            var trainNum = sessionStorage.getItem('trainInfo');
            if(trainNum.indexOf(arr[i].queryLeftNewDTO.station_train_code)>-1){
                $('li').eq(i).find('.weui-check').attr('checked','checked')
            };
        }
    };
    $.hideLoading();
}

(function () {
    // $(document).on("click", "#show-loading", function() {
    //         $.showLoading();
    //         setTimeout(function() {
    //             $.hideLoading();
    //         }, 3000);
    //     });
    $.showLoading();
    var ticket = $.fn.ticket,
        rep = JSON.parse(sessionStorage.infos),
        param = {
            fromStation:sessionStorage.fromStation,
            toStation:sessionStorage.toStation,
            startDate:rep.startDate
        };
    if(sessionStorage.trainAll){
        var data = JSON.parse(sessionStorage.trainAll);
        getTrain(data);
    }else{
        // getSelect(ticket,param);
        getSelect(ticket.url.queryTrain,param);
    };
    var focusDate = rep.showDate,
        focusWeek = rep.week;
    $('.today').text(focusDate + focusWeek);

    $(".weui-btn").on('click',function () {
        var trainStr='';
        $('.weui-check').each(function(i,obj){
            if($(this).is(':checked')){
                trainStr+=','+$(this).parent().prev().find('.trainNo').text();
            }
        });
        trainStr = trainStr.substring(1,trainStr.length);
        sessionStorage.setItem('trainNo',trainStr);
        sessionStorage.setItem('trainInfo',trainStr);
        if(trainStr.split(",").length<=11){
            window.location.href = 'grabTicket.html';
        }else{
            alert("请选择小于十一项");
        };
    });
})();

/*
 * @param url          url地址
 * @param param   需要提交的数据，对象格式
 * @param type       类型post或get，默认get
 */
function ajax(url, param, type) {
    // 利用了jquery延迟对象回调的方式对ajax封装，使用done()，fail()，always()等方法进行链式回调操作
    return $.ajax({
        url: url,
        data: param || {},
        type: type || 'GET'
    });
};

function getSelect(url,param) {
    // ticket.doAjax({
    //     url: ticket.url.queryTrain,
    //     data:param,
    //     async: false,
    //     success: function (resb) {
    //         console.log(resb);
    //         var data = resb;
    //         if(data.data!=null&&data.data.length != 0){
    //             $(".trainList").empty();
    //             sessionStorage.setItem("trainAll",JSON.stringify(data.data));
    //             getTrain(data.data);
    //         };
    //     },
    //     error: function (xhr) {
    //         $.alert(xhr.statusText);
    //     }
    // })
    ajax(url,param,"POST").done(function(resp){
        var data = resp;
        if(data.data!=null&&data.data.length != 0){
            $(".trainList").empty();
            sessionStorage.setItem("trainAll",JSON.stringify(data.data));
            getTrain(data.data);
            // for(var i = 0;i<data.data.length;i++){
            //     var template = $("#trainNum").clone().html();
            //     var trainData = data.data[i].queryLeftNewDTO;
            //     var html = template.replace("trainNo1",trainData.station_train_code);
            //     html = html.replace("city1",trainData.from_station_name)
            //                         .replace("city2",trainData.to_station_name)
            //                         .replace("usedTime",trainData.lishi)
            //                         .replace("startTime",trainData.start_time)
            //                         .replace("endTime1",trainData.arrive_time)
            //                         .replace("idList",'s'+i).replace("idList",'s'+i);
            //     if(trainData.station_train_code.indexOf('G')>-1||trainData.station_train_code.indexOf('D')>-1||trainData.station_train_code.indexOf('C')>-1){
            //         html = html.replace("seat1","商务座："+trainData.swz_num)
            //                             .replace("seat2","一等座："+trainData.zy_num)
            //                             .replace("seat3","二等座："+trainData.ze_num)
            //                             .replace("seat4","无座："+trainData.wz_num);
            //     }else{
            //         html = html.replace("seat1","软卧："+trainData.rw_num)
            //                              .replace("seat2","硬卧："+trainData.yw_num)
            //                              .replace("seat3","硬座："+trainData.yz_num)
            //                              .replace("seat4","无座："+trainData.wz_num);
            //     };
            //     $(".trainList").append(html);
            //     //将已选择的车次选中
            //     if(sessionStorage.getItem('trainInfo')){
            //         var trainNum = sessionStorage.getItem('trainInfo');
            //         if(trainNum.indexOf(data.data[i].queryLeftNewDTO.station_train_code)>-1){
            //             $('li').eq(i).find('.weui-check').attr('checked','checked')
            //         };
            //     }
            // };
        };
    });
};
