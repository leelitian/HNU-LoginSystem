<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>登录界面</title>

<style type="text/css">
@charset "UTF-8";

/*统一页面字体和大小*/
body {
	font-family: "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial,
		Helvetica, sans-serif;
	font-size: 12px;
}
/*清除常用元素的边界、补白、边框默认样式*/
p, h1, form, button {
	border: 0;
	margin: 0;
	padding: 0;
}
/*定义一个强制换行显示类*/
.spacer {
	clear: both;
	height: 1px;
}
/*定义表单外框样式*/
.myform {
	margin: 0 auto;
	width: 400px;
	padding: 14px;
}

/*定制当前表单样式*/
#stylized {
	border: solid 2px #b7ddf2;
	background: #ebf4fb;
}

/*设计表单内div和p通用样式效果*/
#stylized h1 {
	font-size: 14px;
	font-weight: bold;
	margin-bottom: 8px;
}

#stylized p {
	font-size: 11px;
	color: #666666;
	margin-bottom: 20px;
	border-bottom: solid 1px #b7ddf2;
	padding-bottom: 10px;
}

#stylized label { /*定义表单标签样式*/
	display: block;
	font-weight: bold;
	text-align: right;
	width: 140px;
	float: left;
}

/*定义小字体样式类*/
#stylized .small {
	color: #666666;
	display: block;
	font-size: 11px;
	font-weight: normal;
	text-align: right;
	width: 140px;
}

/*统一输入文本框样式*/
#stylized input {
	float: left;
	font-size: 12px;
	padding: 4px 2px;
	border: solid 1px #aacfe4;
	width: 200px;
	margin: 2px 0 20px 10px;
}

/*定义图形化按钮样式*/
#stylized button {
	clear: both;
	margin-left: 150px;
	width: 125px;
	height: 31px;
	background: #666666 url(images/button.png) no-repeat;
	text-align: center;
	line-height: 31px;
	color: #FFFFFF;
	font-size: 11px;
	font-weight: bold;
}

/*设计表单内文本框和按钮在被激活和获取焦点状态下时，轮廓线的宽、样式和颜色*/
input:focus, button:focus {
	outline: thick solid #b7ddf2;
}

input:active, button:active {
	outline: thick solid #aaa;
}
/*通过outlineoffset属性放大轮廓线*/
input:active, button:active {
	outline-offset: 4px;
}

input:focus, button:focus {
	outline-offset: 4px;
}

</style>
<script type="text/javascript" src="check.js"></script>
</head>

<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

<body background="1.jpg">
	<div id="stylized" class="myform">
		<form id="form1" name="form1" method="post" action="LoginServlet">
			<h1>登录</h1>
			<p>请准确填写个人信息...</p>
			
			<label>Username <span class="small">姓名</span></label> 	
			<input type="text" name="user" id="usr">
			
			<label>Password <span class="small">密 码</span></label>
			<input type="password" name="pwd" id="pass">
			
			<button type="submit" onclick=loginCheck()>登 录</button>
			
			<a href="register.jsp"> 注册账号 </a>
			<br/>
			<a href="reset.jsp"> 忘记密码? </a>
			
			
			<div class="spacer"></div>
		</form>
	</div>
	
	<%
		String msg = (String) request.getAttribute("msg");		// 获取错误属性
		if(msg != null) {
	%>
	<script type="text/javascript">
		alert("<%= msg %>");
	</script>
	<%
		}
	%>
	
</body>
</html>