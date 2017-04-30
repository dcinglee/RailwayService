var orders=$.fn.ticket;
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
	
$(document).ready(function(){
	var userInfo = JSON.parse(sessionStorage.userInfo);
	
	$('a.query').attr('href', 'tel:' + sessionStorage.telphone);
	var params={
			userId:userInfo.userId
	};
	getOrderList(params);
});

function nvl(val){
	
	if(val == null){
		return '';
	}
	
	return val;
}

function getOrderList(params){
	  //获取订单列表
	  ajax('/RailwayService/ticketOrder/getHistoryOrders',params,"POST").done(function(resp){
		 var  data=JSON.parse(resp);
		 if(data.data !=null &&data.data.length !=0){
			$('.train_order').empty();
			 for(var i=0;i<data.data.length;i++){
				// console.log(data.data[i].isDelete);
				 if(data.data[i].isDelete != '1'){
				 var createDate=data.data[i].createDate;
				 var timestamp2 = Date.parse(new Date(createDate));
			     var cc=new Date(timestamp2);
			     switch (cc.getDay()) {
				  case 0:weeks="星期天";break
				  case 1:weeks="星期一";break
				  case 2:weeks="星期二";break
				  case 3:weeks="星期三";break
				  case 4:weeks="星期四";break
				  case 5:weeks="星期五";break
				  case 6:weeks="星期六";break
				 } 
				 var template=$("#orderList").clone().html();
				 var html=template.replace("city1",nvl(data.data[i].aboardStation));
				  html=html.replace("2345",nvl(data.data[i].ticketOrderId));
				  html=html.replace("2333",nvl(data.data[i].ticketOrderId));
				  html=html.replace("city2",nvl(data.data[i].arrivedStation));
				  html=html.replace("2017-3-14",nvl(data.data[i].createDate));
				  html=html.replace("星期二",nvl(weeks));
				  if( data.data[i].totalPrice == null){
					  html=html.replace('<p class="price">￥603.5</p>',"");
				  }else{
					  html=html.replace("603.5",nvl(data.data[i].totalPrice));
				  }
				  html=html.replace("G6025",nvl(data.data[i].lineNo));
				  html=html.replace("08车08D",nvl(data.data[i].seatDetail));
				  
				  console.log(data.data[i].aboardTime);
				  if( data.data[i].aboardTime == null ){
					  html=html.replace('<p class="timeInfo"><span class="startTime">18:16</span>-<span class="endTime">21:45</span></p>','<p class="timeInfo"><span class="startTime"> </span><span class="endTime"> </span> </p>');
				  }else{
					  html=html.replace("18:16",data.data[i].aboardTime);
					  html=html.replace("21:45",data.data[i].arrivedTime);
				  }
				  
				  
				  $(".train_order").append(html);
				  
				 if(data.data[i].orderStatus=="18502"){
					$('.bottomBox').eq(i).append('<a href="javascript:void(0);" data-orderId="'+data.data[i].ticketOrderId+'" class="cancel btn">取消订单</a>');
					 
				 }
				 if(data.data[i].orderStatus=="18503"){
					 $('.bottomBox').eq(i).append('<a href="javascript:void(0);" data-orderId="'+data.data[i].ticketOrderId+'" class="stopTickets btn">停止抢票</a>');
					 
				 }
				}
			 }			 
		 }
	  });
}
$(document).on('click','.page_bdnews .topBox',function(){
	
	ticketOrderId=$(this).attr('data-orderId');
	sessionStorage.setItem("ticketOrderId",ticketOrderId);
	
	if($(this).next().find('a:eq(2)').text()=="停止抢票"){
		
	window.location.href="grabTicketing.html";
	}
	if($(this).next().find('a:eq(2)').text()=="取消订单"){
		
	window.location.href="bookSuccess.html";
	}
	if($(this).next().find('a:eq(1)').text()=="删除"){
		
		window.location.href="bookSuccess.html";
    }
});
var ticketOrderId;
$(document).on('click','.delete',function(){
	ticketOrderId=$(this).attr('data-orderId');
	console.log(ticketOrderId);
});
$(document).on('click','.cancel',function(){
	ticketOrderId=$(this).attr('data-orderId');
	console.log(ticketOrderId);
});
$(document).on('click','.stopTickets',function(){
	ticketOrderId=$(this).attr('data-orderId');
	console.log(ticketOrderId);
});
$(document).on('click','.orderDetele .train-dialog__ok',function(){
	console.log(ticketOrderId);
	var data = {
		ticketOrderId:ticketOrderId
	}
	deleteOrders(data);
});
var ticketOrderId;
$(document).on('click','.orderCancle .train-dialog__ok',function(){
	console.log(ticketOrderId);
	var data = {
		ticketOrderId:ticketOrderId,
		orderid:8888
	}
	cancelOrders(data);
});
$(document).on('click','.orderStop .train-dialog__ok',function(){
	
	console.log(ticketOrderId);
	var data = {
		ticketOrderId:ticketOrderId
	}
	stopOrders(data);
});
/*$(".train-dialog__ok").on('click',function(){
	console.log(ticketOrderId);
	var data = {
		orderId:ticketOrderId
	}
	deleteOrders(data);
});*/

$(document).on('click','.bottomBox .btn',function(){
    if($(this).text()=='取消订单'){
        $('#orderCancle .weui-dialog__title').text('取消订单');
        $('#orderCancle .weui-dialog__bd').text('您确定取消订单吗？');
        $('.orderCancle').fadeIn(200);
    }else if($(this).text()=='删除'){
        $('.orderDetele .weui-dialog__title').text('删除');
        $('.orderDetele .weui-dialog__bd').text('您确定删除订单吗？');
        $('.orderDetele').fadeIn(200);
    }else if($(this).text()=='停止抢票'){
        $('#orderStop .weui-dialog__title').text('停止抢票');
        $('#orderStop .weui-dialog__bd').text('您确定停止抢票吗？');
        $('.orderStop').fadeIn(200);
    }
    //$('#orderDetele').fadeIn(200);
   // $('#stop_grab').css({display:block});
    //.find('.weui-btn-area .weui-btn').attr('data-orderId', $(this).data('orderId'));
    
//    $.ajax({
//        url: '/RailwayService/ticketOrder/deleteOrder',
//        type: 'GET',
//        data:{
//        	ticketOrderId:ticketOrderId
//        },
//        dataType: 'json',
//        async: false,
//        success: function (resb) {
//            if (resb.success) {
//               console.log(resb);	              
//               return true;
//            }
//        },
//        error: function (XMLHttpRequest, textStatus, errorThrown) {
//            alert(textStatus);
//        }
//    });
    //return true;
});

function close_alert(){
    $('.orderDetele').fadeOut(200);
}

$(document).on('click','.train-dialog__cancel',function(){
	$('.orderDetele').fadeOut(200);
	$('.orderCancle').fadeOut(200);
	$('.orderStop').fadeOut(200);
});
$('.train_order .weui-cell').on('click',function(){
    location.href='grabTicketing.html';
});
function deleteOrders(data){
	  ajax('/RailwayService/ticketOrder/deleteOrder',data,"POST").done(function(resp){
		  var data = $.parseJSON(resp);
          if(data.success == true){
              $.toast("删除成功");
              $('.orderDetele').fadeOut(200);
              var userInfo = JSON.parse(sessionStorage.userInfo);
              var params={
              		userId:userInfo.userId
              }
              getOrderList(params);
          }else{
              window.location.href="trainOrder.html";
          };
	  });
};
function cancelOrders(data){ 
	orders.doAjax({
		url:orders.url.cancel_order,
		data:data,
	    success: function (resb) {
	    	if(resb.error== '0'){
	    		$('.orderCancel').fadeOut(200);	
	    		$.alert("取消订单成功",function(){window.location.reload();});
	    	}else if(resb.error== '1'){
	    		$.alert("请先登录");
	    	}     
	    },
	    error: function (xhr) {
	        $.alert(xhr.statusText, '错误');
	    }
	});
};
function stopOrders(data){
	 orders.doAjax({
		url:orders.url.stopTicketing,
		data:data,
	    success: function (resb) {
	    	if(resb.error== '0'){
	    		$('.orderStop').fadeOut(200);		
	    		$.alert("停止抢票成功",function(){window.location.reload();});
	    	}	     
	    },
	    error: function (xhr) {
	        $.alert(xhr.statusText, '错误');
	    }		
	});
	  
}