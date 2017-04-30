// $.ajax({
//     async:false,
//     url: "/RailwayService/entrance/getCurrentUser",
//     type: "GET",
//     dataType: 'jsonp',
//     jsonp: 'jsoncallback',
//     success: function (json) {//客户端jquery预先定义好的callback函数,成功获取跨域服务器上的json数据后,会动态执行这个callback函数
//         console.log(json);
//     },
//     complete: function(XMLHttpRequest, textStatus){
//         console.log(textStatus)
//     },
//     error: function(xhr){
//         //jsonp 方式此方法不被触发.原因可能是dataType如果指定为jsonp的话,就已经不是ajax事件了
//         //请求出错处理
//         alert("请求出错(请检查相关度网络状况.)");
//     }
//     });



// 全局变量，判断有无行程
    var isTrip =true,
           length,
           loadNodes,
           imgLeft,
           imgRight,
           logoLeft,
           logoRight,
           n = 0;
	/*
	* 获取行程信息
	 */
    var trip = JSON.parse(sessionStorage.getItem("route"));
    //如果从行程录入页跳转进来则显示遮罩层，其他页面则不显示遮罩层
    (function () {
        //获取当前站点
        if(sessionStorage.localStation){
            $(".stationName .localStation").html(sessionStorage.getItem("localStation"));
            var obj = {};
            obj.stationId = sessionStorage.getItem("localStationId");
            //获取热门商品
            getHot("../product/queryProductByRecommend",obj);
            //获取精选商品
            getSelect(obj);
            var nodes = $(".classifyLink");
            for (var i = 0; i < nodes.length; i++) {
                nodes.eq(i).attr('data-stationId',obj.stationId);
            };
        }else{
            getLocation();
        };
        //获取行程
        getTrip();
        //获取banner
        getBanner();

        /*
        * 图片懒加载
         */
        // lazyload();
        // var n = 0;
        $(".main").scroll(lazyload);
        function lazyload(event){
            for(var i = n;i < length;i++){
                if(loadNodes.eq(i).offset().top<(parseInt($('.main').height())+parseInt($('.main').scrollTop()))){
                    // if(imgLeft.eq(i).attr('src')=="statics/images/indexImg/defalt.gif"){
                        var src = imgLeft.eq(i).attr('data-src');
                        var src1 = imgRight.eq(i).attr('data-src');
                        var src2 = logoLeft.eq(i).attr('data-src');
                        var src3 = logoRight.eq(i).attr('data-src');
                        imgLeft.eq(i).attr('src',src);
                        imgRight.eq(i).attr('src',src1);
                        logoLeft.eq(i).attr('src',src2);
                        logoRight.eq(i).attr('src',src3);
                        n = i+1;
                    // }
                }
            }
        }

    })();



	/*
	 * 点击事件
	 */
    //
    // $(".stationName").on('click',function () {
    //     console.log(1);
    //     $(".stationList").css("display",'block');
    //     // event.stopPropagation();
    //     // $(document).on('click',function () {
    //     //     $(".stationList").css("display",'none');
    //     //     // document.onclick = null;
    //     // })
    // });

    // $(".stationList").on('click',function (event) {
    //     event.stopPropagation();
    // });


    $(document)
        .on('click', '.stationName ', function() {
            //显示或者隐藏站点列表
            $(".stationList").toggle();
        })
        .on("click",'.stationList li',function () {
            //点击更改为当前点击的站点信息
            $(".stationName .localStation").html($(this).find('span').html());
            sessionStorage.setItem("localStation",$(this).find('span').html());
            sessionStorage.setItem("localStationId",$(this).attr('stationId'));
            var obj = {};
            obj.stationId = $(this).attr('stationId');
            getHot("../product/queryProductByRecommend",obj);
            getSelect(obj);
            var nodes = $(".classifyLink");
            for (var i = 0; i < nodes.length; i++) {
                nodes.eq(i).attr('data-stationId',$(this).attr('stationId'));

                // var str = nodes.eq(i).attr('href');
                // var url = str.split("&")[0]+"&stationId="+ $(this).attr('stationId');
                // nodes.eq(i).attr('href',url);
            };
        })
        //关闭行程修改
        .on("click","#mask,.closeMask",function () {
            hideMask();
            if(trip){
                trip.toRoute = false;
                sessionStorage.setItem("route",JSON.stringify(trip));
            };
        })
        // 点击出现修改行程信息
        .on("click",".myTrips",function () {
            //若无行程则直接跳转到行程设置
            console.log(isTrip)
            if(isTrip==false){
                window.location.href = "route.html";
            }else{
                showMask();
            };
        })
        //获取商品id及商家id并跳转到商户
        .on("click",".hotLink,.select-box a",function () {
            sessionStorage.setItem("merchantId",$(this).attr('data-merchant'));
            sessionStorage.setItem("productId",$(this).attr('data-product'));
            sessionStorage.setItem("serviceType",$(this).attr('data-service'));
            sessionStorage.setItem("stationId",$(this).attr('data-station'));
            window.location.href = "merchandise.html";
        })
        //点击修改行程
        .on("click",".changeTrip",function () {
            // var route = {
            //     startStation: $(".startStation").html(),
            //     destination: $(".endStation").html(),
            //     trip: $(".trainNumber").html(),
            //     aboardTime:$(".startTime").html()?$(".startTime").html().substring(0,$(".startTime").html().length-1):null
            // };
            // if(route.startStation!=undefined){
            //     sessionStorage.setItem("route",JSON.stringify(route));
            // };
            window.location.href = "route.html";
        })
        .on("click",".classifyLink",function () {
            sessionStorage.setItem("stationId",$(this).attr('data-stationId'));
            sessionStorage.setItem("serviceType",$(this).attr('data-serviceType'));
            sessionStorage.setItem("classifyName",$(this).parent().find(".classifyList").html());
            window.location.href = "merchant.html";
        });

        $(document).click(function (e) {
            var drag = $(".stationList"),
                  dragel = $(".stationName")[0],
                  target = e.target;
            if (dragel !== target && !$.contains(dragel, target)) {
                drag.hide();
            }
        });


	/*
	* 遮罩层与行程修改出现
	 */
    function showMask(){
        $("#mask").css("height",$(document).height());
        $("#mask").css("width",$(document).width());
        $("#mask").show();
        // $(".tripOut").css({left:"0",top:"100px"})
        $(".tripOut").show();
    };
    //隐藏遮罩层与行程修改
    function hideMask(){
        $("#mask").hide();
        $(".tripOut").hide();
        // $(".tripOut").animate({
        //     height:'0px',
			// top:'0px',
			// left:"300px",
        //     width:'0px'
        // },1000,function () {
        //     $(".tripOut").hide();
        // });
    };


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

    function getTrip() {
        //记录行程信息
        if(trip&&trip.init){
            //判断是否从行程页进入
            if(trip.toRoute!=false){
                showMask();
            };
            // $(".myTrips").html("行程："+trip.startStation+"——"+trip.destination);
            $(".startStation").html(trip.startStation);
            $(".endStation").html(trip.destination);
            $(".trainNumber").html(trip.trip);
            if(trip.aboardTime){
                $(".startTime").html(trip.aboardTime+"开");
            }else{
                $(".startTime").html(" ");
            };
            if(trip.arriveTime){
                $(".endTime").html(trip.arriveTime+"到");
            }else{
                $(".endTime").html(" ");
            };
            var data = {
                station : trip.startStation,
                lineNo : trip.trip
            };
            getLate(data);
            getWeather({cityId:trip.cityId});
            //渲染站点列表
            $.each(trip.stations,function () {
                $(".stationList").append("<li stationid="+this.stationId+"><span class='other'>"+this.stationName+"</span></li>");
            });
            $(".changeTrip a").html("修改行程");
        }else{
            ajax("../travelRoute/get",'','POST').done(function(resp){
                var data = $.parseJSON(resp);
                console.log(data);
                if(data.data){
                    //录入行程信息
                    // $(".myTrips").html( "行程："+data.data.aboardStation+"——"+data.data.arrivedStation);
                    var route = {
                        startStation: data.data.aboardStation,
                        destination: data.data.arrivedStation,
                        trip: data.data.lineNo,
                        aboardTime:data.data.aboardTime?data.data.aboardTime.substring(0,data.data.aboardTime.length-1):null
                    };
                    if(route.startStation!=undefined){
                        sessionStorage.setItem("route",JSON.stringify(route));
                    };

                    $(".startStation").html(data.data.aboardStation);
                    $(".endStation").html(data.data.arrivedStation);
                    $(".trainNumber").html(data.data.lineNo);
                    if(data.data.aboardTime){
                        $(".startTime").html(data.data.aboardTime+"开");
                    }else{
                        $(".startTime").html(" ");
                    };
                    if(data.data.arrivedTime){
                        $(".endTime").html(data.data.arrivedTime+"到");
                    }else{
                        $(".endTime").html(" ");
                    }
                    var lateData = {
                        station : data.data.aboardStation,
                        lineNo : data.data.lineNo
                    };
                    //获取正晚点信息
                    getLate(lateData);
                    //获取目的地天气信息
                    getWeather({cityId:data.data.cityId});
                    //渲染站点列表
                    $.each(data.data.listStationOnTheWay,function () {
                        $(".stationList").append("<li stationid="+this.stationId+"><span class='other'>"+this.stationName+"</span></li>");
                    });
                    $(".changeTrip a").html("修改行程");
                }else{
                    isTrip = false;
                    //修改行程显示
                    // $(".myTrips").html("行程：如长沙南——深圳北");
                    $(".tripTop .fl").html("无当前行程");
                    $(".tripTop .fr").html(" ");
                    $(".tripInfo").html("<p style='margin: 0 auto;'>请设置行程！</p>");
                    $(".late").html("");
                    $(".changeTrip a").html("设置行程");
                    //清空沿途站点列表
                    // $(".stationList").empty();
                    //无行程则显示所有服务开通站点
                    getOnline();
                }
            }).fail(function(err){

            });
        };
    };


    // ajax("/RailwayService/entrance/getCurrentUser",'','POST').done(function(resp){
		// var data = $.parseJSON(resp);
		// if(data.data){
    //         sessionStorage.setItem("userInfo",JSON.stringify(data.data));
    //     }else{
    //         // window.location.href = "http://moledata.cn/RailwayService/entrance/index.do";
    //     }
    // }).fail(function (err) {
    //     // console.log(err);
    //     // window.location.href = "http://moledata.cn/RailwayService/entrance/index.do";
    // });

	/*
	* 获取当前用户所在站点
	*/
	function getLocation(obj) {
        ajax("../entrance/getCurrentStation",obj,'POST').done(function(resp){
            console.log($.parseJSON(resp));
            var data = $.parseJSON(resp);
            if(data.data!=null&&data.success==true){
                getLocationInfo(data);
            }else{
                // $.toast(data.message, "forbidden");
                getLocationInfo(data);
            };
        });
    };
	function getLocationInfo(data) {
        $(".stationName .localStation").html(data.data.stationName);
        var obj = {};
        obj.stationId = data.data.stationId;
        //获取热门商品
        getHot("../product/queryProductByRecommend",obj);
        //获取精选商品
        getSelect(obj);
        //获取服务类型
        // serviceType(obj);
        var nodes = $(".classifyLink");
        for (var i = 0; i < nodes.length; i++) {
            nodes.eq(i).attr('data-stationId',obj.stationId);
            // nodes.eq(i).attr('href',str);
        };
    };

	//获取目的地天气
	//
	function getWeather(obj) {
		ajax("../entrance/getWeather.do",obj,'POST').done(function (resp) {
            var data = $.parseJSON(resp);
            if(data.success == true){
                var str = data.data.weather+data.data.lowTemp+"-"+data.data.upTemp;
                $(".weather").html(str);
            }else{
                $(".weather").html("查询异常！");
            };
        });
    };

	//获取是否晚点及晚点时间
	function getLate(obj) {
        ajax("../entrance/checkTrain.do",obj,'POST').done(function (resp) {
            var data = $.parseJSON(resp);
            if(data.success == true){
                $(".late").html(data.data);
            }else{
                $(".late").html("暂无到达时间信息");
            };
        });
    };


    /*
     * 无行程则查询所有服务站点
     */
    function getOnline() {
        ajax("../railwayStation/getOnLineStation.do","","POST").done(function(resp){
            var data = $.parseJSON(resp);
            if(data.success == true){
                //渲染站点列表
                $.each(data.data,function () {
                    $(".stationList").append("<li stationid="+this.stationId+"><span class='other'>"+this.stationName+"</span></li>");
                });
            }else{
                $.toast(data.message, "forbidden");
            };
        }).fail(function () {

        });
    };

	/*
	* 服务类型的ajax
	*/
    // function serviceType(obj) {
    //     ajax("/RailwayService/serviceType/findAll",'','POST').done(function(resp){
    //         var data = $.parseJSON(resp);
    //         console.log(data);
    //         if(data.success==true){
    //             $(".classify").empty();
    //             //小于四项则显示一行
    //             if(data.data.length<=4){
    //                 $(".classify").append("<div class='weui-flex'></div>");
    //                 for(var i = 0;i < data.data.length;i++){
    //                     var template = $("#serviceType")	.clone().html();
    //                     var html = template.replace("javascript:void(0);","merchant.html?serviceType="+data.data[i].typeId+"&stationId="+obj.stationId);
    //                     html = html.replace("statics/images/indexImg/order.png",data.data[i].imageId);
    //                     html = html.replace("serviceName",data.data[i].name);
    //                     $(".classify .weui-flex").append(html);
    //                 };
    //             }else{
    //                 //大于四项则显示两行
    //                 $(".classify").empty();
    //                 $(".classify").append("<div class='weui-flex'></div><div class='weui-flex'></div>");
    //                 for(var i = 0;i<data.data.length;i++){
    //                     var template = $("#serviceType").clone().html();
    //                     var html = template.replace("javascript:void(0);","merchant.html?serviceType="+data.data[i].typeId+"&stationId="+obj.stationId);
    //                     html = html.replace("statics/images/indexImg/order.png",data.data[i].imageId);
    //                     html = html.replace("serviceName",data.data[i].name);
    //                     if(i<Math.ceil(data.data.length/2)){
    //                         $(".classify .weui-flex").eq(0).append(html);
    //                     }else{
    //                         $(".classify .weui-flex").eq(1).append(html);
    //                     };
    //                 };
		// 		}
    //         }
    //     }).fail(function(err){
    //
    //     });
    // };


	/*
	* banner图ajax
	*/
	function getBanner() {
        ajax("../AdBanner/queryAdBanner.action","","POST").done(function(resp){
        	var data = $.parseJSON(resp);
        	if(data.data.length!=0){
        		for(var i = 0;i<data.data.length;i++){
        			// $(".swiper-wrapper").append("<div class='swiper-slide'><a href="+data.data[i].linkUrl+"><img src=.."+data.data[i].imageUrl+" alt=''></a></div>");
                    $(".swiper-wrapper").append("<div class='swiper-slide'><a href="+data.data[i].linkUrl+" style='background:url(.."+data.data[i].imageUrl+") no-repeat;background-size:100% 100%;width:100%;height:100%;display:block'></a></div>");
        		};
                $(".swiper-slide").eq(0).find("a").css('background-image',data.data[0].imageUrl);
               setTimeout(function () {
                    for(var j = 1;j<data.data.length;j++){
                        $(".swiper-slide").eq(j).find("a").css('background-image',data.data[j].imageUrl);
                    };
                },5000);
                /*
                 * 初始化轮播图控件
                 */
                var mySwiper = new Swiper('.swiper-container', {
                    initialSlide:0,
                    loop:true,
                    autoplay:5000,
                    autoplayDisableOnInteraction : false,
                    pagination: '.swiper-pagination',
                    paginationClickable :true,
                    observer:true,                      //修改swiper自己或子元素时，自动初始化swiper
                    observeParents:true           //修改swiper的父元素时，自动初始化swiper
                });
        	};
        })
    };


	/*
	* 精选的ajax
	*/
	function getSelect(data) {
        ajax("../product/queryHotProductByRecommend",data,"POST").done(function(resp){
        	var data = $.parseJSON(resp);
        	console.log(data);
        	if(data.data!=null&&data.data.length != 0){
                var goodsLength;
        	    if(data.data.length%2==0){
                    goodsLength = data.data.length;
                }else{
                    goodsLength = (data.data.length-1);
                };
        	    //获取宽度
        	    var width = goodsLength/2*32.1-4.6;
                $(".select .selectBody .selectList").css("width",width+"vw");
        	    $(".selectList").empty();
        		for(var i = 0;i<goodsLength;i++){
                    var template = $("#selectList").clone().html();
                    var html = template.replace("merchantId",data.data[i].merchantId);
                    html = html.replace("productId",data.data[i].productId);
                    html = html.replace("serviceType",data.data[i].serviceTypeId);
                    html = html.replace("stationId",data.data[i].stationId);
                    if(data.data[i].imageUrl.indexOf('http')>-1){
                        html = html.replace("defaltImg",data.data[i].imageUrl);
                    }else{
                        html = html.replace("defaltImg",'..'+data.data[i].imageUrl);
                    };
                    html = html.replace("selectGoodsName1",data.data[i].name);
                    html = html.replace("selectGoodsPrice1","￥"+data.data[i].price);
                    html = html.replace("belongStation1",data.data[i].stationName);
                    $(".selectList").append(html);
        		};
                $(".selectList .select-box").eq(goodsLength/2-1).css("margin-right","0");
        	};
        })
    };


	/*
	* 热门的ajax
	*/
	function getHot(url,data) {
        ajax(url,data,'POST').done(function(resp){
        	var data = $.parseJSON(resp);
        	console.log(data);
            $(".hotGoodsLeft").empty();
            $(".hotGoodsRight").empty();
            var dataLength;
            if(data.data.length>=16){
                dataLength = 16
            }else{
                dataLength = data.data.length;
            };
        	if(data.data.length!=0){
        		for(var i = 0;i<dataLength;i++){
                    // var url = "merchandise.html?merchantId=" + data.data[i].merchantId+"&productId="+data.data[i].productId;
                    var str = $("#hotList").clone().html();
                    //修改商品链接地址
                    // var html = str.replace("javascript:void(0);",url);
                    var html = str.replace("merchantId",data.data[i].merchantId);
                    html = html.replace("productId",data.data[i].productId);
                    html = html.replace("serviceType",data.data[i].serviceTypeId);
                    html = html.replace("stationId",data.data[i].stationId);
                    //修改商品图片
                    if(data.data[i].imageUrl.indexOf('http')>-1){
                        html = html.replace("imgSrc",data.data[i].imageUrl+"?x-oss-process=image/resize,m_lfit,h_150");
                    }else{
                        html = html.replace("imgSrc",'..'+data.data[i].imageUrl);
                    };
                    //修改商家logo
                    if(data.data[i].logoImageUrl.indexOf('http')>-1){
                        html = html.replace("logoSrc",data.data[i].logoImageUrl);
                    }else{
                        html = html.replace("logoSrc",'..'+data.data[i].logoImageUrl);
                    };
                    //修改商品名
                    html = html.replace("goodsName",data.data[i].name);
                    //修改商品介绍
                    html = html.replace("goodsDescript",data.data[i].introduction);
                    //修改销量
                    html = html.replace("1000",data.data[i].sales?data.data[i].sales:'0');
                    //单数项则存入左边，双数项则存入右边
        			if(i%2!=0){
                        $(".hotGoodsRight").append(html);
					}else{
                        $(".hotGoodsLeft").append(html);
					};
        		};
                length = $(".hotGoodsLeft .hotLink").length;
                loadNodes = $(".hotGoodsLeft .hotLink");
                imgLeft = $('.hotGoodsLeft .goodsImg');
                imgRight = $('.hotGoodsRight .goodsImg');
                logoLeft = $(".hotGoodsLeft .logoImg img");
                logoRight = $(".hotGoodsRight .logoImg img");
                n = 0;
        	};
        });
    };