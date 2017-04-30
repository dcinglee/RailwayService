var orders=$.fn.ticket;
$(document).ready(function(){
	//40283ccb5b84425a015b85acf2fc0010;
	var ticketOrderId = sessionStorage.getItem("ticketOrderId");
	$.ajax({  
	    type:"POST",
	    url:"../../ticketOrder/getBookOrder",
	    data:{
	    	ticketOrderId:ticketOrderId
	    },
	    dataType: 'json',
	    success: function (resb) {
	    	 var seatType = {"18401":"一等座","18402":"二等座","1840318403":"商务座","18404":"特等座","18405":"高铁无座",
                             "18406":"硬座","18407":"硬卧","18408":"软卧","18409":"火车商级软卧","18410":"无座"}
	         var passengerType = {"18001":"成人","18002":"儿童","18003":"学生","18004":"伤残军人和警察"};
	    	if(resb.success== true){
	    		var ticketsInfos=resb.data.ticketOrder;
	    		console.log(ticketsInfos);
	    		var splitDate=ticketsInfos.createDate.split("-");
	    		var fullTime=(splitDate[0]+'年'+splitDate[1].replace(/\b(0+)/gi,""))+'月'+splitDate[2]+'日';
	    		// 
	    		//$('.ticket-No').html(ticketsInfos.sequenceNo);
	    		// 发车日期
	    		$('.ticket-time>dd').text(fullTime);
	    		// 出发车站
	    		$('#fromStation').text(ticketsInfos.aboardStation);
	    		//目的车站
	    		$('#toStation').text(ticketsInfos.arrivedStation);
	    		//车次
	    		$('.ticket-line').text(ticketsInfos.lineNo);
	    		//出发时间
	    		$('#startDate').text(ticketsInfos.aboardTime);
	    		//到达时间	    		
	    		$('#endDate').text(ticketsInfos.arrivedTime);
	    		
	    	 	$('.passengers').remove();
	    		for(var i=0;i<resb.data.railwayTickets.length;i++){
                    console.log("444");
                    var template=$("#bookSuccessInfos").clone().html();
                    var identityNo=resb.data.railwayTickets[i].identityCardNo.split('');
                    identityNo.splice(6,8,"*","*","*","*","*","*","*","*");	    			
		    		var html=template.replace("乘客姓名",resb.data.railwayTickets[i].passengerName);
		    		var html=html.replace("详细座位",resb.data.railwayTickets[i].carriageNumber+"车厢"+resb.data.railwayTickets[i].seatNumber);
		    		var html=html.replace("车票种类",resb.data.railwayTickets[i].ticketType);
		    		var html=html.replace("座位种类",ticketsInfos.seatType);
		    		var html=html.replace("车票单价",parseInt(resb.data.railwayTickets[i].ticketPrice)/100);
		    		var html=html.replace("证件号码",identityNo.join(""));		    				    		
		    		$('.ticket-timeout').before(html);	
	    		}
	    		
	    		$('.btn-bottom').append('<a href="javascript:void(0);" class="weui-btn weui-btn_plain-default cancel" data-toggle="cancel" data-orderId="'+ticketsInfos.orderId+'"  data-ticketOrderId="'+ticketsInfos.ticketOrderId+'">取消订单</a>');
	    		
	    		if(ticketsInfos.orderStatus==='18505'){
	    			$('.ticket').removeClass("ticket-success");
	    			$('.ticket').addClass("ticket-timeout");
	    		}
	    		if(ticketsInfos.orderStatus==='18503'){
	    			$('.ticket').removeClass("ticket-success");
	    			$('.ticket').addClass("ticket-timeout");
	    		}
	    	}
	        
	    },
	    error: function (xhr) {
	        $.alert(xhr.statusText, '错误');
	        
	    }
	 });
}); 

$(document).on('click', '[data-toggle=cancel]', function () {
   cancelOrder($(this).data('id'));
   }).on('click', '.train-dialog__cancel,.train-mask', function () {
    $('.train-confirm').toggleClass('hide');
});

function cancelOrder(id) {
    $('.train-confirm').toggleClass('hide');
}

var tickerOrderId,orderid;
$(document).on('click','.cancel',function(){
	ticketOrderId=$(this).attr('data-ticketOrderId');
	orderid=$(this).attr('data-orderId');
	console.log(ticketOrderId);
});
$(document).on('click','.train-dialog__ok',function(){
	console.log(ticketOrderId);
	var data = {
		ticketOrderId:ticketOrderId,
		orderid:orderid
	}
	cancelOrders(data);
});
function cancelOrder(data){
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
$(function(){
    var m=29;
    var s=59;
    setInterval(function(){
        if(s<10){
            $('.pay-time').html(m+':0'+s);
        }else{
            $('.pay-time').html(m+':'+s);
        }
        s--;
        if(s<0){
            s=59;
            m--;
        }
    },1000)
})