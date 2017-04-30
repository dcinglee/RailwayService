/**
 * Created by xz on 2017/3/2.
 */

var isRefresh = false;

/*
* 刷新界面
 */
getOrder();
//获取用户信息，确定存在用户
ajax("../entrance/getCurrentUser",'','POST').done(function(resp){
var data = $.parseJSON(resp);
if(data.data){
        sessionStorage.setItem("userInfo",JSON.stringify(data.data));
    }else{
        // window.location.href = "http://moledata.cn/RailwayService/entrance/index.do";
    }
}).fail(function (err) {
    // console.log(err);
    // window.location.href = "http://moledata.cn/RailwayService/entrance/index.do";
});


/*
* 下拉刷新
*/
$('.main').dropload({
    // scrollArea : window,
    domUp : {
        domClass   : 'dropload-up',
        domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
        domUpdate  : '<div class="dropload-update">↑释放更新</div>',
        domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
    },
    loadUpFn : function(me){
        isRefresh = true;
        getOrder(function () {
            me.resetload();
            $.toast("刷新成功",500);
            // isRefresh = true;
        });
    }
});

$(document).on("click",".orderLink",function () {
    sessionStorage.setItem("orderId",$(this).attr('data-orderId'));
    window.location.href = "orderDetail.html";
    // if(isRefresh==false){
    //     window.location.href = "orderDetail.html";
    // };
});


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

// getOrder();

/*
* 查询订单
 */
function  getOrder(callback) {
    ajax('../order/queryOrdersByUser.do','','POST').done(function(resp){
        var data = $.parseJSON(resp);
        console.log(data);
        var cancelStatus = {"9010":"等待商家确认","9020":"商家同意取消订单","9030":"商家拒绝取消订单"}
        var status = {"7001":"新建待支付","7010":"等待商家接单","7020":"商家已接单","7030":"商家拒绝接单","7040":"配送员已接单","7050":"配送员已取货","7055":"等待用户取货","7060":"订单已完成","7070":"订单已取消","7080":"订单已失效","7090":"订单已超时"};
        $(".weui-panel__bd").empty();
        if(data.data.length != 0){
            //用于判断是否全为新建待支付订单
            var orderNumber = false;
            for(var i = data.data.length-1;i>=0;i--){
                if(data.data[i].orderStatus==7001){
                    continue;
                };
                //如果有其他类型订单将其值变为true
                orderNumber = true;
                // var url = "orderDetail.html?orderId=" + data.data[i].orderId;
                var str = $("#orderTemplate").clone().html();
                var html = str.replace("orderid1",data.data[i].orderId);
                if(data.data[i].imageUrl.indexOf('http')>-1){
                    html = html.replace("statics/images/indexImg/orderImg01.png",data.data[i].imageUrl);
                }else{
                    html = html.replace("statics/images/indexImg/orderImg01.png",'..'+data.data[i].imageUrl);
                };
                // html = html.replace("statics/images/indexImg/orderImg01.png",data.data[i].imageUrl);
                html = html.replace("麦当劳",data.data[i].merchantName);
                html = html.replace("2017-2-30 17:30",data.data[i].createDate);
                if(data.data[i].orderStatus==7070){
                    html = html.replace("订单已取消 ","订单已取消");
                }else if(data.data[i].orderStatus==7060){
                    html = html.replace("订单已取消 ","订单已完成");
                }else{
                    if(data.data[i].orderCancelStatus){
                        html = html.replace("订单已取消 ",cancelStatus[data.data[i].orderCancelStatus]);
                    }else{
                        html = html.replace("订单已取消 ",status[data.data[i].orderStatus]);
                    };
                };
                if(data.data[i].listSubOrder.length>=2){
                    html = html.replace("goodsNumber",data.data[i].listSubOrder.length);
                }else{
                    html = html.replace('<span>等</span><span class="number">goodsNumber</span><span>件商品</span>','');
                };
                var goodsDetail = "";
                for(var j = 0;j<data.data[i].listSubOrder.length;j++){
                    goodsDetail+=(data.data[i].listSubOrder[j].productName+"、")
                };
                html = html.replace("本宫鸡腿堡(全翅+琵琶腿+可乐)",goodsDetail.substring(0,goodsDetail.length-1));
                html = html.replace("price1",data.data[i].totalPrice);
                $(".weui-panel__bd").append(html);

                //如果全为新建待支付订单则显示无订单状态
                if(orderNumber==false){
                    $(".weui-panel").css("height","100%");
                    $(".weui-panel__bd").append("<div class='orderNo'><img src='statics/images/indexImg/noOrder.jpg' alt=''><div><p class='text'>暂无订单</p><a href='index.html' class='toIndex'><span href='index.html'>点击进入首页</span></a></div></div>");
                };
            };
        }else{
            $(".weui-panel").css("height","100%");
            $(".weui-panel__bd").append("<div class='orderNo'><img src='statics/images/indexImg/noOrder.jpg' alt=''><div><p class='text'>暂无订单</p><a href='index.html' class='toIndex'><span>点击进入首页</span></a></div></div>");
        };
        //是否存在回调函数
        if(callback){
            callback();
        };
    }).fail(function(err){
        if(isRefresh==true){
            me.resetload();
            isRefresh = false;
        };
    });
};




