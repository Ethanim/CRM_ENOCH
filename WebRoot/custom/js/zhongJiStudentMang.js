/*禁止文字复制*/
document.oncontextmenu=function(evt){ 
    evt.preventDefault(); 
} 

document.onselectstart=function(evt){ 
    evt.preventDefault(); 
};
/* 初始化时间插件*/
$('#scoretime').datetimepicker({
	lang: 'ch',
	format:"Y-m-d",      //格式化日期
	validateOnBlur:false //删除选中的时间
});
$('#arrive_time').datetimepicker({
	lang: 'ch',
	format:"Y-m-d",      //格式化日期
	validateOnBlur:false //删除选中的时间
})
$('#mailTime').datetimepicker({
	lang: 'ch',
	 format:"Y-m-d",      //格式化日期
	validateOnBlur:false //删除选中的时间
});
/*获取销售姓名*/
saleName('#belongName');	
function saleName(saleId){
	$.ajax({
		type: "GET",
		url: '../resource/queryAllXiaoShou.do',
		cache: false,
		success: function(data) {
			var dataObj=eval("("+data+")");//转换为json对象			   
			var list = dataObj.rows;			
			$.each(list, function (index, item) {  
                //循环获取数据					
	            var userid = list[index].userid|| "";  
	            var username = list[index].username|| "";
	            var option = $('<option value="'+userid+'">' + username + '</option>');
	            $(saleId).append(option);           
               
            });           
		}
	})
}
/*获取课程*/
queryCourse('#courseid');	
function queryCourse(tagId){
	$.ajax({
		type: "GET",
		url: '../course/queryCourse.do',
		cache: false,
		success: function(data) {			
				var dataObj=eval("("+data+")");//转换为json对象	   		   
			var list = dataObj.rows;			
			$.each(list, function (index, item) {  
                //循环获取数据					
	            var courseid = list[index].courseid|| "";  
	            var courseName = list[index].courseName|| "";
	            var option = $('<option value="'+courseid+'">' + courseName + '</option>');
	            $(tagId).append(option);
            });     
		}
	})
}
/*根据课程ID，查询课程下的科目*/
subject('#subject')
function subject(Id) {
    $.ajax({        			
	   type: 'GET',
	   url: '../subject/querySubject.do',		   
	   success: function(data) {
		   var dataObj=eval("("+data+")");//转换为json对象
		   var list = dataObj.rows;			   
	        $.each(list, function(index, array) {
	        	//循环获取数据
                var subjectid = array.subjectid|| "";	
                var subjectname = array.subjectname || "";
	        	var checkbox = $('<option value="'+subjectid+'">' + subjectname + '</option>');
	        	$(Id).append(checkbox);
		    });
	   }
	});
}
/*所有客服人员*/
customName('#chooseCustomer');	
function customName(Id){
	$.ajax({
		type: "GET",
		url: '../custom/queryAllCustoms.do',
		cache: false,
		success: function(data) {			
			var dataObj=eval("("+data+")");//转换为json对象			   
			var list = dataObj.rows;			
			$.each(list, function (index, item) {  
                //循环获取数据					
				var userid = list[index].userid|| "";  
	            var username = list[index].username|| "";
	            var option = $('<option value="'+userid+'">' + username + '</option>');
	            $(Id).append(option);
            });   
		}
	})
}

//数据分页
var userid = document.getElementById("userid").value;//用户Id
var deptid = document.getElementById("deptid").value;//部门Id
var roleid = document.getElementById("roleid").value;//角色Id
//展示iframe
function showiframe(studentId,tag,currentPage){
	var dataParams = {
		deptid:deptid,
		studentId:studentId,
		menuCode:"zhongjihf"
	}
	$('#backcurrent').val(currentPage)
	var params = $.param(dataParams);
	
	var url = '../custom/hfDetails.do'+"?"+params;
	$(tag).attr('href',url)
	$('#showdeiatil').css('display','block');
}

//第一次加载
var firstData = {        
		userid: userid,
    	deptid:deptid,
    	roleid:roleid,
    	menuCode:"zhongjihf"
}
load('pagination',firstData,'../custom/queryhfStudents.do',1)
function load(tag,data,url,currentPage){
	$('#'+tag).sjAjaxPager({
		type: "GET",
	    url: url,
	    pageSize: 15, 
	    currentPage:currentPage,
	    preText: "上一页",
	    nextText: "下一页",
	    firstText: "首页",
	    lastText: "尾页",
	    searchParam: data,
	    beforeSend: function () {
	    },
	    success: function (data) {    	
	    	var list = data.rows;
	    	var currentPage = data.currentPage;
	    	$('#filterTotal').html(data.total)
	    	//清空数据 
		   clearDate(); 
	       //插入头部
	       $("#resourceTable").append(tableHeader); 
		   fillTable(list,currentPage)
	    },
	    complete: function () {
	    }
	});
}
var tableHeader = "<tr class='henglan'>"+
		"<th class='check' style='width: 60px;position: fixed;background:#fff;'><input type='checkbox' class='all' /></th>"+
		"<th class='details' style='width: 60px;position: fixed;left: 59px;background:#fff;'>详情</th>"+
		"<th class='serialnumber' style='width: 60px;position: fixed;left: 118px;background:#fff;'>序号</th>"+
		"<th class='studentName' style='width: 80px;position: fixed;left: 177px;background:#fff;'>姓名</th>"+
		"<th class='phone' style='width: 120px;position: fixed;left: 256px;background:#fff;'>手机</th>"+
		"<th class='idCard' style='width: 140px;position: fixed;left: 375px;background:#fff;'>身份证</th>"+
		"<th class='studentstate' style='width: 600px;padding-left: 520px;'>学员状态</th>"+
		"<th class='subjectname'>所报科目</th>"+		
		"<th class='customerName'>班主任</th>"+
		"<th class='address'>地区</th>"+   
		"<th class='email'>邮箱</th>"+
		"<th class='belongName'>招生老师</th>"+
		"<th class='arrive_money'>收款金额</th>"+
		"<th class='arrive_time'>收款日期</th>"+
		"<th class='jjjczd'>经济基础知识</th>"+
		"<th class='jjjczdScore'>考试成绩</th>"+
		"<th class='jrzy'>金融专业知识与实务</th>"+
		"<th class='jrzyScore'>考试成绩</th>"+
		"<th class='gsgl'>工商管理专业知识与实务</th>"+
		"<th class='gsglScore'>考试成绩</th>"+
		"<th class='czss'>财政税收专业知识与实务</th>"+
		"<th class='czssScore'>考试成绩</th>"+			
		"<th class='weixin'>微信</th>"+
		"<th class='courseversion'>课件打印</th>"+			
	"</tr>";
//清空数据 
//清空数据 
function clearDate(){ 
    $('#resourceTable').html("");
}  
//填充数据 
function fillTable(list,currentPage){ 
	$.each(list, function(index, array) {
		//循环获取数据
		var resourceId = array. resourceId|| "";
		var studentId = array. studentId|| "";
	    var studentName = array.studentName|| "";
	    var phone = array.phone|| "";
	    var studentstate = array.studentstate|| "";
	    var customerName = array.customerName|| "";
	    var address = array.address|| "";
	    var idCard = array.idCard|| "";
	    var email = array.email|| "";
	    var belongName = array.belongName|| "";
	    var arrive_time = array.arrive_time|| "";
	    var arrive_money = array.arrive_money|| "";
	    var weixin = array.weixin|| "";
	    var subjectname = array.subjectname|| "";	    
	    var jjjczd = array.jjjczd|| "";
	    var jrzy = array.jrzy|| "";	    
	    var gsgl = array.gsgl|| "";
	    var czss = array.czss|| "";
	    var jjjczdScore = array.jjjczdScore|| "";
	    var jrzyScore = array.jrzyScore|| "";
	    var gsglScore = array.gsglScore|| "";
	    var czssScore = array.czssScore|| "";	   
	    var courseversion = array.courseversion|| "";
	    var customerName = array.customerName || "";
	    var studentColor = array.studentColor|| "";
	    
	    if (studentColor == ""||studentColor==null) {            	       
	    	studentColor = "#fff";
	    }else{
	    	studentColor = studentColor;
	    }
	    
	    if (studentstate == ""||studentstate==null) {            	       
	    	studentstate = "";
	    } 
	    else if (0 == studentstate) {
	    	studentstate = '新增'
	    } 
	    else if (1 == studentstate) {
	    	studentstate = '已成交'
	    } 
	    else if (2 == studentstate) {
	    	studentstate = '已提交'
	    } 
	    else if (3 == studentstate) {
	    	studentstate = '已到账'
	    } 
	    else if (4 == studentstate) {
	    	studentstate = '已分配'
	    } 
	    else if (5 == studentstate) {
	    	studentstate = '已转入'
	    } 
	    else if (6 == studentstate) {
	    	studentstate = '已通过考试'
	    } 
	    else if (7 == studentstate) {
	    	studentstate = '已关课'
	    }
	    else if (8 == studentstate) {
	    	studentstate = '已退回'
	    }
	    var menuCode = "zhongjihf";
	    tableItem ="<tr data-id="+studentId+" style='background-color:"+studentColor+"'>"+
	    "<td class='check' style='width: 60px;position: fixed;background-color:"+studentColor+"'><input type='checkbox' class='"+studentId+"'  data-id='"+resourceId+"'/></td>"+
        "<td class='details' style='width: 60px;position: fixed;left:59px;background-color:"+studentColor+"'><a onclick='showiframe("+studentId+",this,"+currentPage+")' target='detailiframe'><span class='glyphicon glyphicon-edit edit' data-id="+studentId+"></span></a></td>"+ 
        "<td class='serialnumber' style='width: 60px;position: fixed;left: 118px;background-color:"+studentColor+"'>"+((currentPage-1)*15+(index+1))+"</td>"+
        "<td class='studentName' style='width: 80px;position: fixed;left: 177px;background-color:"+studentColor+"'>"+studentName+"</td>"+
        "<td class='phone' style='width: 120px;position: fixed;left: 256px;background-color:"+studentColor+"'>"+phone+"</td>"+
        "<td class='idCard' style='width: 140px;position: fixed;left: 375px;background-color:"+studentColor+"'>"+idCard+"</td>"+	
        "<td class='studentstate' style='width: 600px;padding-left: 520px;'>"+studentstate+"</td>"+
        "<td class='subjectname'>"+subjectname+"</td>"+		
        "<td class='customerName' >"+customerName+"</td>"+
        "<td class='address'>"+address+"</td>"+  
        "<td class='email'>"+email+"</td>"+
        "<td class='belongName'>"+belongName+"</td>"+
        "<td class='arrive_money'>"+arrive_money+"</td>"+
        "<td class='arrive_time'>"+arrive_time+"</td>"+
        "<td class='jjjczd'>"+jjjczd+"</td>"+
        "<td class='jjjczdScore'>"+jjjczdScore+"</td>"+
        "<td class='jrzy'>"+jrzy+"</td>"+
        "<td class='jrzyScore'>"+jrzyScore+"</td>"+
        "<td class='gsgl'>"+gsgl+"</td>"+
        "<td class='gsglScore'>"+gsglScore+"</td>"+
        "<td class='czss'>"+czss+"</td>"+
        "<td class='czssScore'>"+czssScore+"</td>"+
        "<td class='weixin'>"+weixin+"</td>"+
        "<td class='courseversion'>"+courseversion+"</td>"+
	    "</tr>";
		$("#resourceTable").append(tableItem);
	}); 
}
//全选
$(document).on('click','.all',function() {
	if(this.checked) {
		$("input[type='checkbox']").prop("checked", true);
	} else {
		$("input[type='checkbox']").prop("checked", false);
	}
});

/*根据条件进行查询资源--筛选*/
var filterData;
function filter(){	
	var backcurren = "1" ;//只要点击了筛选或者查询
	var backcurrentdata = (backcurren++);
	$('#backcurrent').attr('data-id',backcurrentdata)
	$('#pagination').html('');
	var studentName = $('#studentName').val();	
	var phone = $('#phone').val().trim();	
	var source = $('#source').val();
	var courseid = $('#courseid option:selected').val();
	var belongName = $('#belongName option:selected').val();
	var subject = $('#subject option:selected').val();	
	var studentstate = $('#studentstate option:selected').val();
	var studentEvaluate = $('#studentEvaluate option:selected').val();
	var ispass = $('#ispass option:selected').val();
	var address = $('#address option:selected').val();
	var scoretime = $('#scoretime').val();
	var arrive_time = $('#arrive_time').val();
	var studentColor = $('#studentColor option:selected').val();
	var customerId = $('#chooseCustomer option:selected').val();
	filterData = {
		userid:userid,
		deptid:deptid,
    	roleid:roleid,
    	studentName:studentName,
    	phone:phone,
		source:source,		
		courseid:courseid,
		belongid:belongName,
		subjectid:subject,
		studentEvaluate:studentEvaluate,
		ispass:ispass,
		address:address,
		scoretim:scoretime,
		arrive_time:arrive_time,
		studentColor:studentColor,
		customerId:customerId,
		menuCode:"zhongjihf"
    }
	$('#filter').css('display','none')	
	load('pagination',filterData,'../custom/queryCustomStudentBySceen.do',1)
}

/*查询*/
var search = null;
var phonedata;
function fastquery(){
	search = $('#search').val();
	$('#pagination').html('');
	phonedata = {        
		userid: userid,
    	deptid:deptid,
    	roleid:roleid,
    	studentName:search,
    	idCard:search,
    	phone:search,
    	menuCode:"zhongjihf"
	}
	if(search=="" || search == null){
		$('#backcurrent').attr('data-id','');
		load('pagination',firstData,'../custom/queryhfStudents.do')//等于空加载所有
	}else {
		var backcurren = "1" ;//只要点击了筛选或者查询
		var backcurrentdata = (backcurren++);
		$('#backcurrent').attr('data-id',backcurrentdata)
		phoneFill (phonedata,'../custom/queryCustomStudentBySceen.do')
	}
}
function phoneFill(data,url){
	$.ajax({
		url: url,
		type: 'GET',
		data: data,			
		success: function(data) {
			var dataObj=eval("("+data+")");//转换为json对象			   
			var list = dataObj.rows;
			var currentPage = dataObj.currentPage;
			//清空数据 
			clearDate(); 
			//插入头部
			$("#resourceTable").append(tableHeader); 
			fillTable(list,currentPage);
		}
	})
}


/*导出*/
function exportExcel(){	
	
	var allStudent = [];	
	var studentId = [];
	$("#resourceTable input[type='checkbox']:not(:first):checked").each(function() {
		var boxid = $(this).attr('class');		
		studentId.push(boxid);
	});
	
	studentIds = studentId.join(',');
	var exportExcel = "export_excel";	
	if(studentId == 0){
		if(filterData != null){
			execlurl(filterData)
		}else{
			$.winTip({
				title: "提示~~~",
				msg: "由于资源太多请筛选导出",
				src:"./system/img/tishi.png"
			});
		}
	}else {
		dataParams = {
			userid:userid,			 			
 			roleid:roleid,
			deptid:deptid,
			studentIds:studentIds,
			menuCode:"zhongjihf"
		};
		execlurl(dataParams)
	}	
};

function execlurl(dataParams){
	var params = $.param(dataParams);
	
	var url = '../custom/StudentExport.do'+"?"+params;
	
	$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
	
    delete dataParams[exportExcel];
}
var iframe = $(window.parent.document).find("#iframepage");
iframe.height(0);
//子窗口操作返回当前列表页
function showdeiatil(){
	$('#showdeiatil').css('display','none');
	iframe.height(0);
	window.setInterval("reinitIframe()", 200);
	var curret = $('#backcurrent').val();
	var filtercurrent = $('#backcurrent').attr('data-id');
	
	if((curret != "" && curret != null) && (filtercurrent == "" || filtercurrent == null)){ //如果当前页不等于空，就重新加载返回curret的列表数据
		load('pagination',firstData,'../custom/queryhfStudents.do')
	}else if((search != "" && search != null) && (filtercurrent != "" || filtercurrent != null)){//如果是手机查询的，就重新加载返回curret的列表数据
		phoneFill (phonedata,'../custom/queryCustomStudentBySceen.do')
	}else if((search == "" || search == null) && (filtercurrent != "" || filtercurrent != null) ){//如果是多条件查询的，就重新加载返回curret的列表数据
		load('pagination',filterData,'../custom/queryCustomStudentBySceen.do')
	}
}
//定时检查页面长度
function reinitIframe() {          
    try {               
           var bHeight =document.body.scrollHeight;               
           var dHeight =document.documentElement.scrollHeight;              
           var height = Math.max(bHeight, dHeight);
           iframe.height(bHeight);
       } catch (ex) { }       
}       
window.setInterval("reinitIframe()", 200);