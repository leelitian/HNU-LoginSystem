<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="entity.Person"%>
<%@ page language="java" import="entity.Users"%>

<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<style type="text/css">
table {
	border-collapse: collapse;
	margin: 0 auto;
	text-align: center;
	width: 500px;
}

table caption {
	font-size: 18px;
	caption-side: top;
	padding: .5em;
	color: #0080ff;
}

table td, table th {
	border: 1px solid #cad9ea;
	color: #666;
	height: 30px;
}

table thead th {
	background-color: #CCE8EB;
	width: 100px;
}

table tr:nth-child(odd) {
	background: #fff;
}

table tr:nth-child(even) {
	background: #F5FAFA;
}
</style>
</head>

<body>
	<table class="table">
		<caption>个人信息</caption>
		<tr>
			<th>用户名</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>电话</th>
		</tr>

		<%
			Person u = (Person) request.getAttribute("person");
		%>
		<tr>
			<td align="center"><%=u.getUsername()%></td>
			<td align="center"><%=u.getName()%></td>
			<td align="center"><%=(u.getAge() == null) ? "" : u.getAge()%></td>
			<td align="center"><%=(u.getTeleno() == null) ? "" : u.getTeleno()%></td>
		</tr>

	</table>
	<p style="text-align: center">
		<a href="login.jsp"> 退出登录 </a>
	</p>
</body>
</html>
