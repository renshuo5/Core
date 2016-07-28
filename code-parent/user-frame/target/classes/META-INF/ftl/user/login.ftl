<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<form  action="${rc.contextPath}/dologin" method="post">
<table>
	<tr>
		<td>账号</td>
		<td><input type="text" name="username" value=""/></td>
		<td>密码</td>
		<td><input type="password" name="password" value=""/></td>
		<td>
		<button type="submit">提交</button>
		<button type="button">取消</button>
		</td>
	</tr>
</table>
</form>
</body>
</html>