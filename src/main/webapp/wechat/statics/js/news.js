/**
 * Created by xz on 2017/3/2.
 */


/*
* 调用函数查询用户信息并更新列表
 */
var obj;
var isClear = false;
var isRefresh = false;
getNews();

//下拉刷新
// $(".main").pullToRefresh().on("pull-to-refresh", function() {
//     //回调刷新
//     getNews(function () {
//         $(".main").pullToRefreshDone();
//         // $.toptip('操作成功', 'success');
//         $.toast("刷新成功",500);
//     });
// });

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
        getNews(function () {
            me.resetload();
            $.toast("刷新成功",500);
            // isRefresh = true;
        });
    }
});

/*
* 清空消息
*/
$(".clearAll p").on("click",function(){
    if(isClear == false){
        $.confirm("是否清空所有消息", "确认清空", function() {
            //点击确认后的回调函数
            clearData(obj);
        }, function() {
            //点击取消后的回调函数
            return false;
        });
    }else{
        return false
    };
});

//跳转到订单详情
$(document).on("click",'.newsLink',function () {
    sessionStorage.setItem("orderId",$(this).attr('data-order'));
    window.location.href = 'orderDetail.html'
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

/*
* 查询用户信息
 */
function getUser() {
    ajax("../entrance/getCurrentUser",'','POST').done(function(resp){
        var data = $.parseJSON(resp);
        if(data.data){
            obj = {receiverId:data.data.userId};
            getNews(obj);
        }else{
            $.toast("获取用户失败，请刷新", "forbidden");
        };
    });
};

/*
* 清空消息
 */
function clearData() {
    ajax("../notice/clean",' ','POST').done(function(resp){
        var data = $.parseJSON(resp);
        if(data.success==true){
            isClear = true;
            $(".clearAll").css("display","none");
            $(".weui-panel__bd").empty();
            $(".weui-panel__bd").append("<div class='newsNo'><img src='statics/images/indexImg/noNews.jpg' alt=''><div><p class='text'>暂无消息</p></div></div>");
            $.toast("清空完成");
        }else{
            $.toast("清空失败，请重试", "forbidden");
        };
    });
};

/*
 * 消息查询
 * @param data       查询参数
 * @param callback 回调函数
 */
function  getNews(callback) {
    ajax('../notice/query','','POST').done(function(resp){
        var data = $.parseJSON(resp);
        console.log(data);
        $(".weui-panel__bd").empty();
        if(data.data.length != 0){
            $(".clearAll").css("display","block");
            for(var i = data.data.length-1;i>=0;i--){
                // alert(JSON.stringify(data.data[i]));
                // alert(data.data[i].content);
                var content = JSON.parse(data.data[i].content);
                // var url = "orderDetails?" + data.data[i].orderId;
                var str = $("#newsTemplate").clone().html();
                //显示商家图片
                var html;
                if(content.merchantLogoUrl.indexOf('http')>-1){
                    html = str.replace("statics/images/indexImg/orderImg01.png",content.merchantLogoUrl);
                }else{
                    html = str.replace("statics/images/indexImg/orderImg01.png",'..'+content.merchantLogoUrl);
                };
                //订单号
                html = html.replace("orderId",content.mainOrderId);
                //     显示订单状态
                // alert(content.orderStatusName)
                html = html.replace("商家已接单",content.orderStatusName);
                //     显示商家名称
                html = html.replace("肯德基餐厅",content.merchantName);
                 //   显示消息时间
                html = html.replace("2017-02-14 14:00",content.createTime);
                 //   显示商品名称
                html = html.replace("本宫鸡腿堡(全翅+琵琶腿+可乐)",content.productDetail);
                //   显示商品数量
                var arr = content.productDetail.split(',');
                if(arr.length>=2){
                    html = html.replace("goodsNumber",arr.length);
                }else{
                    html = html.replace('<span>等</span><span class="number">goodsNumber</span><span>件商品</span>',' ');
                };
                $(".weui-panel__bd").append(html);
            };
        }else{
            isClear = true;
            $(".clearAll").css("display","none");
            $(".weui-panel__bd").append("<div class='newsNo'><img src='statics/images/indexImg/noNews.jpg' alt=''><div><p class='text'>暂无消息</p></div></div>");
        }
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