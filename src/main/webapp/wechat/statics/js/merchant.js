/**
 * Created by xz on 2017/3/7.
 */

(function () {
    //调用core.js内的getUrlParams方法获得url内包含的参数
    var serviceType = sessionStorage.getItem("serviceType");
    var stationId = sessionStorage.getItem("stationId");
    var data = {stationId:stationId,serviceType:serviceType};
    getMerchant("../merchant/queryMerchantByStation.do",data);
})();

/*
* 点击获取商家id并存到sessionStorage中
 */
$(document).on("click",".merchantLink",function () {
   sessionStorage.setItem("merchantId",$(this).attr('data-id'));
    window.location.href = "merchandise.html";
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
function getMerchant(url,data) {
    ajax(url,data,'POST').done(function(resp){
        var data = $.parseJSON(resp);
        console.log(data);
        var status = {"8001":"营业中","8002":"歇业","8003":"商家休息"};
        $(".weui-panel__bd").empty();
        if(data.data.length != 0){
            for(var i = 0;i<data.data.length;i++){
                 var template = $("#merchantTemplate").clone().html();
                 var html = template.replace("merchantId",data.data[i].merchantId);
                 //替换商家logo
                if(data.data[i].imageUrl.indexOf('http')>-1){
                    html = html.replace("statics/images/indexImg/orderImg01.png",data.data[i].imageUrl);
                }else{
                    html = html.replace("statics/images/indexImg/orderImg01.png",'..'+data.data[i].imageUrl);
                };
                 //替换商家名
                 html = html.replace("麦当劳",data.data[i].name);
                // evaluate = String(data.data[i].evaluate).split('.');
                // console.log(evaluate);
                // if(data.data[i].evaluate){
                //     for (var j = 0; j < evaluate[0]; j++) {
                //         html = html.replace("icon-xing1","icon-xing2-copy");
                //     };
                //     if (evaluate.length > 1) {
                //         html = html.replace("icon-xing1","icon-banxing1");
                //     };
                // }else{
                //       return false;
                // };
                 //显示评分
                 // html = html.replace("评分",data.data[i].evaluate?data.data[i].evaluate:'0');
                 //显示月销量
                 html = html.replace("销售额",data.data[i].sailsInMonth?data.data[i].sailsInMonth:'0');
                 //显示营业状态
                 html = html.replace("营业中",status[data.data[i].status]);

                 if(data.data[i].status==8001){
                     html = html.replace("merchantStatus","merchantOpen");
                 }else{
                     html = html.replace("merchantStatus","merchantClose");
                 };
                $(".weui-panel__bd").append(html);
                //评价星星
                var $star = $(".weui-panel__bd .merchantLink").eq(i).find(".star");
                if(data.data[i].evaluate){
                    evaluate = String(data.data[i].evaluate).split('.');
                    for (var j = 0; j < evaluate[0]; j++) {
                        $star.append('<i class="iconfont icon-xing1"></i>');
                    };
                    if (evaluate.length > 1) {
                        $star.append('<i class="iconfont icon-banxing1"></i>');
                    };
                }else{
                    $star.html("暂无评分信息");
                }
            };
        }else{
            $(".main").empty();
            $(".main").append("<div style='text-align: center;margin: 100px 0;color: #989898;'><i class='iconfont icon-wujilu' style='font-size: 120px'></i><p>当前站点暂无商家信息，请选择其他站点！</p></div>");
        };
    }).fail(function(err){
        alert("未查询到商家数据，请确认网络是否畅通并重试!!");
    });
};