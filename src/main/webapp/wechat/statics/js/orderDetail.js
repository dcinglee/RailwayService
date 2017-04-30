/**
 * Created by xz on 2017/3/3.
 */

//解决下拉刷新多次重复添加数据
var hasRefresh = false;

var isRefresh = false;
/*
* 获取订单号
*/
(function(){
    var isRefresh = true;
    var orderId;
    var userId;
    if(sessionStorage.orderId){
        orderId = sessionStorage.getItem("orderId");
        userId = JSON.parse(sessionStorage.getItem("userInfo")).userId;
    }else{
        orderId = getUrlParams.orderId;
        userId = getUrlParams.userId;
    };
    getOrder("../order/queryOrdersByOrderId.do?orderId="+orderId+"&userId="+userId);

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
            getOrder("../order/queryOrdersByOrderId.do?orderId="+orderId+"&userId="+userId,function () {
                me.resetload();
                $.toast("刷新成功",500);
                // isRefresh = true;
            });
        }
    });

    //点击跳转到商家页
    $(".merchantLink").on("click",function () {
       if(isRefresh==true){
           window.location.href = "merchandise.html";
       };
    });

    //跳转到订单跟踪
    $(".merchantStatus").on("click",function () {
        window.location.href = "orderTrack.html";
    });

    $(document).on("click",'.detailImg',function () {
       showMask();
    });

    $("#mask").on("click",function () {
       hideMask();
    });

    /*
     * 遮罩层出现
     */
    function showMask(){
        $("#mask").css("height",$(document).height());
        $("#mask").css("width",$(document).width());
        $("#mask").show();
    };
    //隐藏遮罩层
    function hideMask(){
        $("#mask").hide();
        $(".tripOut").hide();
    };

    /*
     * 智能机浏览器版本信息:
     *
     */
    var browser={
        versions:function(){
            var u = navigator.userAgent, app = navigator.appVersion;
            return{//移动终端浏览器版本信息
                trident: u.indexOf('Trident') > -1,//IE内核
                presto: u.indexOf('Presto') > -1,//opera内核
                webKit: u.indexOf('AppleWebKit') > -1,//苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/)||!!u.match(/AppleWebKit/),//是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),//ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1,//是否iPad
                webApp: u.indexOf('Safari') == -1//是否web应该程序，没有头部与底部
            };
        }(),
        language:(navigator.browserLanguage || navigator.language).toLowerCase()
    }
    // alert(" 是否为iPhone: "+browser.versions.iPhone);
    if(browser.versions.iPhone){
        $('.copy').hide();
    }else{
        $('.copy').show();
    };


    /*
    *  复制功能实现
     */
    function Copy(str){
        var save = function(e){
            e.clipboardData.setData('text/plain', str);
            e.preventDefault();
        }
        document.addEventListener('copy', save);
        document.execCommand('copy');
        document.removeEventListener('copy',save);
        alert('复制成功！');
    }
    $('.copy').on('click', function(){
        Copy($(".orderNo").html().replace(/\s/ig,''));
    });


})();

$(".peisong").on("click",function () {
    if($(".peisong").attr('href')=="tel:null"){
        $.toast("未获取到配送员信息，请联系商家", "text");
        return false;
    };
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


function getOrder(url,callback) {
    ajax(url).done(function(resp){
        var data = $.parseJSON(resp);
        console.log(data);
        //清空商品列表
        $(".list").empty();
        var cancelStatus = {"9010":"(等待商家确认)","9020":"(商家同意取消订单)","9030":"(商家拒绝取消订单)"}
        var status = {"7001":"新建待支付 >","7010":"等待商家接单 >","7020":"商家已接单 >","7030":"已拒绝","7040":"配送员已接单 >","7050":"配送员已取货 >","7055":"等待用户取货 >","7060":"订单已完成","7070":"订单已取消","7080":"订单已失效","7090":"订单已超时"};
        if(data.success == true){
            $(".merchantBox").css("display","block");
            //将各个数据填入相应位置

            if(hasRefresh==false){
                if(data.data.merchant.imageUrl.indexOf('http')>-1){
                    //商家图片
                    $(".merchantLogo img").attr("src",data.data.merchant.imageUrl);
                    //商家小logo
                    $(".merchantName img").attr('src',data.data.merchant.imageUrl);
                }else{
                    $(".merchantLogo img").attr("src",'..'+data.data.merchant.imageUrl);
                    $(".merchantName img").attr('src','..'+data.data.merchant.imageUrl);
                };

                // $(".header").append('<style>.header:before{background: url(/RailwayService' + data.data.merchant.imageUrl + ');background-position: 0 50%;background-size: 100%;}</style>')
                // $(".header").css({"background":"url(/RailwayService" + data.data.merchant.imageUrl + ")","background-position":"0 50%","background-size":"100%"});
                //联系商家
                // $(".linkMerchant").attr('href',"tel:"+ data.data.merchant.phoneNo);
                //联系配送员
                $(".peisong").attr('href',"tel:"+ data.data.order.serviceProviderPhoneNo);
                //商家名称
                $(".name").html(data.data.merchant.name);
                //商品价格
                $(".priceTotal").html(data.data.order.orderTotalPrice);
                //订单号
                var orderNo = data.data.order.orderNo;
                var str = orderNo.substr(0,4),
                       str1 = orderNo.substr(4,4),
                       str2 = orderNo.substr(8,4),
                       str3 = orderNo.substr(11,orderNo.length),
                       orderNew = str+" "+str1+" "+str2+" "+str3;
                $(".orderNumber").html("订单号：<a href='javascript:void(0);' class='orderNo' style='color: #555555;'>"+orderNew+"</a>");
                //联系人
                $(".contacts").html("联系人："+data.data.order.customerName);
                //支付方式
                $(".payWay").html("支付方式：微信支付");
                // 支付时间
                if(data.data.order.payDate&&data.data.order.payDate!=null){
                    $(".orderTime").html("支付时间："+data.data.order.payDate.substring(0,data.data.order.payDate.length-3));
                }else{
                    $(".orderTime").html("支付时间： ");
                };
                //商家地址
                // $(".address").html(data.data.merchant.address);

                //将商家id及订单信息存入session中
                sessionStorage.setItem("merchantName",data.data.merchant.name);
                sessionStorage.setItem("merchantId",data.data.merchant.merchantId);
                sessionStorage.setItem("orderId",data.data.order.orderId);
                sessionStorage.setItem("phone",data.data.merchant.phoneNo);
                sessionStorage.setItem("refund",data.data.order.orderTotalPrice);
                sessionStorage.setItem("merchantLogo",data.data.merchant.imageUrl);
                sessionStorage.setItem("orderDate",data.data.order.createDate);
                sessionStorage.setItem("payDate",data.data.order.payDate);

                //是否配送员送，有则显示配送员按钮,隐藏商家地址，自取则隐藏配送员按钮，并显示商家地址,将送达时间改为预计取货时间
                if(data.data.order.deliverType==16002){
                    //配送员配送
                    if(data.data.order.orderStatus==7040||data.data.order.orderStatus==7050||data.data.order.orderStatus==7060){
                        $(".wayBtn").css("display","block");
                    }else{
                        $(".wayBtn").css("display","none");
                    };
                    $(".name").css('line-height','24px');
                    $(".address").css("display","none");
                    if(data.data.order.latestServiceTime){
                        $(".way").html('最迟送达时间：'+data.data.order.latestServiceTime.substring(11,16));
                    }else{
                        $(".way").html('最迟送达时间：');
                    };
                    $(".orderWay").html("送货方式：配送员送");
                    $(".deliverAddress").html("送货地址："+data.data.order.deliverAddress+"<button class='detailImg'>查看详细位置</button>");
                }else{
                    //自取
                    $(".wayBtn").css("display","none");
                    $(".name").css('line-height','1');
                    $(".address").css("display","black");
                    if(data.data.order.latestServiceTime){
                        $(".way").html('预计取货时间：'+data.data.order.latestServiceTime.substring(11,16));
                    }else{
                        $(".way").html('预计取货时间：');
                    };
                    $(".orderWay").html("送货方式：到店自取");
                    $(".deliverAddress").html(" ");
                };
                //添加小推车位置图片
                $(".carImg img").attr('src',data.data.stationForImageRelaList[0])

                hasRefresh = true;
            };

            //动态添加商品
            for(var i = 0;i<data.data.listSubOrder.length;i++){
                var temp = $('#goodsTemplate').clone().html();
                var html = temp.replace('本宫凤堡',data.data.listSubOrder[i].productName);
                html = html.replace('1',data.data.listSubOrder[i].productCount);
                html = html.replace('23',data.data.listSubOrder[i].totalPrice);
                $('.list').append(html);
            };
            if(data.data.order.distributionCosts){
                var temp1 = $('#goodsTemplate').clone().html();
                var html1 = temp1.replace('本宫凤堡',"配送费");
                html1 = html1.replace('23',data.data.order.distributionCosts);
                $('.list').append(html1);
            };

            //商家状态
            // if(data.data.order.orderStatus==7070){
            //     $(".merchantStatus").html('订单已取消');
            // }else{
            //     if(data.data.order.orderCancelStatus){
            //         $(".merchantStatus").html(cancelStatus[data.data.order.orderCancelStatus]);
            //     }else{
            //         $(".merchantStatus").html(status[data.data.order.orderStatus]);
            //     };
            // };

            $(".merchantStatus").html(status[data.data.order.orderStatus]);
            if(data.data.order.orderCancelStatus){
                $(".merchantCancleStatus").html(cancelStatus[data.data.order.orderCancelStatus]);
                $(".merchantStatus").css("margin-top","2%");
            }else{
                $(".merchantCancleStatus").hide();
                $(".merchantStatus").css("margin-top","7%");
            };

            // 拒绝理由
            if(data.data.order.refuseReason&&data.data.order.orderCancelStatus==9030){
                $(".reason").html("拒绝原因："+data.data.order.refuseReason);
                $(".merchantStatus").css("margin-top","2%");
            }else{
                $(".reason").html("");
                $(".merchantStatus").css("margin-top","7%");
            };

             //订单状态为已完成或已取消的时并且从未评论过出现评价按钮并添加链接到订单评价
            if(data.data.order.orderStatus==7060||data.data.order.orderStatus==7070){
                if(data.data.order.commentFlag == 0){
                    $('.evaluation').css("display","block");
                }else{
                    $('.evaluation').css("display","none");
                }
            }else{
                $('.evaluation').css("display","none");
            }

            //出现取消订单按钮
            if(data.data.order.orderStatus==7070){
                $(".cancelOrder").attr('href','javascript:void(0);');
                $(".cancelText").css("backgroundColor","#999999");
            }else{
                $(".cancelOrder").attr('href','cancelOrder.html');
                $(".cancelText").css("backgroundColor","#ff6600");
            };


            //取消按钮的出现与隐藏
            if(data.data.order.orderStatus==7030||data.data.order.orderStatus==7060||data.data.order.orderStatus==7070||data.data.order.orderStatus==7080||data.data.order.orderStatus==7090||data.data.order.orderCancelStatus==9020){
                $(".cancelBtn").css("display","none");
            }else{
                $(".cancelBtn").css("display","block");
            };


            //存在已取消订单状态
            if(data.data.order.orderCancelStatus==9010){
                $(".cancelOrder").css("backgroundColor","gray");
                $(".cancelOrder").attr("href","javascript:void(0);");
            }else{
                $(".cancelOrder").css("backgroundColor","#ff6600;");
                $(".cancelOrder").attr("href","cancelOrder.html");
            };
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
