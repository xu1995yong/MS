function remaintime() {
	var starttime = $("#starttime").html();
	var endtime = $("#endtime").html();
	var s1 = new Date(starttime);
	var s2 = new Date(endtime);
	var now = new Date();
	var t1 = now - s1.getTime();
	var t2 = now - s2.getTime();
	if (t1 >= 0 && t2 <= 0) {
		$("#seckillStatus").html("秒杀正在进行");
	} else if (t1 >= 0) {
		$("#seckillStatus").html("秒杀已结束");
	} else {
		$("#seckillStatus").html("秒杀未开始");
	}
}
function getPath() {
	var starttime = $("#starttime").html();
	var endtime = $("#endtime").html();
	var s1 = new Date(starttime);
	var s2 = new Date(endtime);
	var now = new Date();
	var t1 = now - s1.getTime();
	var t2 = now - s2.getTime();
	if (t1 >= 0 && t2 <= 0) {
		$.ajax({
			type : "get",
			url : "/seckill/getPath/",
			data : {
				goodsId : $("#goodsId").html()
			},
			success : function(data) {
				if (data.code == 0) {//
					var path = data.data;
					domiaosha(path);
				} else {
					window.location='/login/login'
				}
			},
			error : function() {
				layer.msg("获取秒杀路径失败")
			}
		});

	}

}
function domiaosha(path) {
	$.ajax({
		url : "/seckill/" + path + "/do_seckill",// 安全优化，带着这个path去访问
		type : "POST",
		data : {
			goodsId : $("#goodsId").html()
		},
		success : function(data) {
			if (data.code == 0) {
				layer.msg("请稍后......");

			} else {
				layer.msg(data.data)
			}
		},
		error : function() {
			layer.msg("客户端错误")
		}
	})
}
function getResult(path) {
	$.ajax({
		url : "/seckill/" + path + "/do_seckill",// 安全优化，带着这个path去访问
		type : "POST",
		data : {
			goodsId : $("#goodsId").html()
		},
		success : function(data) {
			if (data.code == 0) {
				layer.msg("请稍后......");

			} else {
				layer.msg(data.data)
			}
		},
		error : function() {
			layer.msg("客户端错误")
		}
	})
}

setInterval('remaintime()', 500);
