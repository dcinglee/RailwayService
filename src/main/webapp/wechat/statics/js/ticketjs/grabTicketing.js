/**
 * Created by xz on 2017/4/17.
 */
(function () {
    // if ($('#loadingToast').css('display') != 'none') return;

    // $('#loadingToast').fadeIn(100);
    // var num=$('#loadingToast').find('.custom-txt').text();
    // var timer=setInterval(function () {
    //     if(num>0){
    //         num--;
    //         $('#loadingToast').find('.custom-txt').text(num);
    //     }else{
    //         clearInterval(timer);
    //         $('#loadingToast').hide();
    //     }
    // }, 1000);
    //
    // $('#stopGrab_show').on('click',function(){
    //     $('#stop_grab').fadeIn(200);
    // });
    //
    // function close_alert(){
    //     $('#stop_grab').fadeOut(200);
    // }

    var trainInfo = JSON.parse(sessionStorage.paras),
          timeInfo = JSON.parse(sessionStorage.infos),
          ticket = $.fn.ticket;
    var ticketOrderId = sessionStorage.getItem("ticketOrderId");
    getTicketing("../../ticketOrder/queryTicketOrder",{'ticketOrderId':ticketOrderId});

    /*
     * 下拉刷新
     */
    $('.trainLogin').dropload({
        // scrollArea : window,
        domUp : {
            domClass   : 'dropload-up',
            domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
            domUpdate  : '<div class="dropload-update">↑释放更新</div>',
            domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
        },
        loadUpFn : function(me){
            isRefresh = true;
            getTicketing("../../ticketOrder/queryTicketOrder",{'ticketOrderId':ticketOrderId},function () {
                me.resetload();
                $.toast("刷新成功",500);
                // isRefresh = true;
            });
        }
    });

    $(document).on('click','#stopGrab_show',function () {
       $(".orderStop").fadeIn(200);
    }).on('click','.train-dialog__cancel',function () {
        $(".orderStop").fadeOut(200);
    }).on('click','.train-dialog__ok',function () {
        $(".orderStop").fadeOut(200);
        ticket.doAjax({
            url: ticket.url.stopTicketing,
            data:{'ticketOrderId':ticketOrderId},
            async: false,
            success: function (resb) {
                if(resb.error=='0'){
                    $.toast("停止抢票成功");
                    //跳转到订单详情页
                    window.location.href = "bookSuccess.html";
                    sessionStorage.setItem("isTicketingStop",true);
                }else{
                    $.toast("暂时无法停止抢票");
                };
            },
            error: function (xhr) {
                $.alert(xhr.statusText);
            }
        })
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

function getTicketing(url,param,callback) {
  ajax(url,param,'POST').done(function (resp) {
      var data = JSON.parse(resp);
      if(data.data){
          $('.No').html(data.data.ticketOrder.lineNo);
          $('.startStation').html(data.data.ticketOrder.aboardStation);
          $('.endStation').html(data.data.ticketOrder.arrivedStation);
          $('.startTime').html(data.data.ticketOrder.aboardTimeFromStation);
          $('.endTime').html(data.data.ticketOrder.arrivedTimeToStation);
          $('.day').html(data.data.ticketOrder.estimatedAboardTime);
          var dayStr = data.data.ticketOrder.estimatedAboardTime;
          dayStr = dayStr.replace(/年/g, "/");
          dayStr = dayStr.replace(/月/g, "/");
          dayStr = dayStr.replace(/日/g, "");
          var weeks = {'1':'星期一','2':'星期二','3':'星期三','4':'星期四','5':'星期五','6':'星期六','0':'星期天'};
          var myDate = new Date(Date.parse(dayStr));
          $('.week').html(weeks[myDate.getDay()]);
          $('.seatType').html(data.data.ticketOrder.seatType);
          if(data.data.ticketOrder.noticePhoneNo){
              var phone = data.data.ticketOrder.noticePhoneNo;
              var phoneArr = phone.split('');
              phoneArr.splice(3,4,"*","*","*","*");
              phone = phoneArr.join("");
              $('.phoneNum').html(phone);
          }else{
              $('.phoneNum').html(' ');
          };
          //添加乘车人信息
          console.log(typeof data.data.ticketOrder.passengerIds)
          $(".passenger").remove();
          var arr = data.data.listPassenger;
          for(var i = 0;i<arr.length-1;i++){
             var numArr = arr[i].identityCardNo.split('');
             numArr.splice(6,8,"*","*","*","*","*","*","*","*");
             var temp = $('#passengerTemp')  .clone().html();
             var html = temp.replace("证件号码",numArr.join(""));
              html = html.replace('乘客姓名',arr[i].passengerName);
              html = html.replace("车票类型",arr[i].passengerType);
              $('.other').before(html);
              if(data.data.orderStatus==18507){
                  $.toast("抢票成功");
                  //跳转到订单详情页
                  window.location.href = "bookSuccess.html";
              };
          };
      };
      //是否存在回调函数
      if(callback){
          callback();
      };
  }).fail(function (err) {
     if(isRefresh){
         me.resetload();
     };
  });
};
