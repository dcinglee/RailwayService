/**
 * Created by xz on 2017/4/5.
 */

// var isRefresh = false;

(function () {
    var orderId = sessionStorage.getItem("orderId");
    var url = "../order/queryOrderRecordsByOrderId?orderId="+orderId;
    getTrack(url);

    /*
     * 下拉刷新
     */
    // $('.main').dropload({
    //     // scrollArea : window,
    //     domUp : {
    //         domClass   : 'dropload-up',
    //         domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
    //         domUpdate  : '<div class="dropload-update">↑释放更新</div>',
    //         domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
    //     },
    //     loadUpFn : function(me){
    //         isRefresh = true;
    //         console.log(1);
    //         getTrack(url,function () {
    //             console.log(2);
    //             me.resetload();
    //             $.toast("刷新成功",500);
    //         });
    //     }
    // });
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

function getTrack(url,callback) {
  ajax(url).done(function (resp) {
      var data = $.parseJSON(resp);
      $('.main').empty();
      var cancelStatus = {"9010":"等待商家确认","9020":"商家同意取消订单","9030":"商家拒绝取消订单"}
      var status = {"7001":"新建待支付","7010":"等待商家接单","7020":"商家已接单","7030":"商家拒绝接单","7040":"配送员已接单","7050":"配送员已取货","7060":"订单已完成","7070":"订单已取消","7080":"订单已失效","7090":"订单已超时"};
      if(data.data.length!=0){
          for(var i = 0;i<data.data.length;i++){
              var temp = $('#orderTemplate').clone().html();
              var html;
              if(data.data[i].orderStatus){
                  html = temp.replace('orderStatus01',status[data.data[i].orderStatus]);
              }else{
                  html = temp.replace('orderStatus01',cancelStatus[data.data[i].orderCancelStatus]);
              };
              html = html.replace('time01',dealTime(data.data[i].createDate));
              if(data.data[i].remark==null){
                  data.data[i].remark=' ';
              };
              if(data.data[i].orderStatus==7070){
                  html = html.replace("reason01","商家同意了您的取消订单申请("+sessionStorage.getItem("refund")+"元退款将于两个工作日内原路退回)");
              }else if(data.data[i].orderStatus==7030){
                  html = html.replace("reason01",'拒绝原因：'+data.data[i].remark);
              }else if(data.data[i].orderCancelStatus==9010){
                  html = html.replace("reason01",' 退款金额：'+sessionStorage.getItem("refund")+"      取消原因："+data.data[i].remark);
              }else if(data.data[i].orderCancelStatus==9030){
                  html = html.replace("reason01",'拒绝原因：'+data.data[i].remark);
              }else{
                  html = html.replace("reason01",' ');
              };
              $(".main").append(html);
          }
      }
      if(sessionStorage.getItem("payDate")){
          var payDate = sessionStorage.getItem("payDate");
          payDate = dealTime(payDate);
      };
      if(sessionStorage.getItem("orderDate")){
          var orderDate = sessionStorage.getItem("orderDate");
          orderDate = dealTime(orderDate);
      };
      var str = "<div class='trackInfo'><div class='left'><i class='iconfont icon-yuan'></i></div><p class='line'></p><div class='title'><p class='fl orderStatus'>订单已支付</p><p class='fr time fz13'><i class='iconfont icon-clock'></i>"+payDate+"</p></div><p class='detail fz12'> </p></div>";
      var str1 = "<div class='trackInfo'><div class='left'><i class='iconfont icon-yuan'></i></div><div class='title'><p class='fl orderStatus'>订单提交时间</p><p class='fr time fz13'><i class='iconfont icon-clock'></i>"+orderDate+"</p></div></div>";
      $(".main").append(str).append(str1);
      //是否存在回调函数
      if(callback){
          callback();
      };
  }).fail(function (err) {
      // if(isRefresh==true){
      //     me.resetload();
      //     isRefresh = false;
      // };
  });
};

function dealTime(str) {
    var timeStr = str.substring(0,str.length-3);
    var timeArr = timeStr.split(" ");
    timeStr = timeArr[1]+" "+timeArr[0];
    return timeStr;
};