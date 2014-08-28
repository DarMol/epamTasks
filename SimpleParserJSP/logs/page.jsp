<%@  page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE  html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Title</title>
</head>
<body>
	<h2 style="text-align: center;">${inputFail}</h2>
	<!-- Comment1  -->
	<%!private int i = 1000;%>
	<%=++i%>
	<%
		while (i != 1010) {
			out.println(i);
	%>
	<!-- Comment2  -->
	<c:choose>
		<c:when test="${user.role eq 'ADMIN'}">
			<input name="option" type="radio" checked="checked" value="addUser" />AddUser<br />
		</c:when>
		<c:when test="${user.role eq 'STUDENT'}">
			<input name="option" type="radio" checked="checked" value="showTests" />Show Tests<br />
			<input name="option" type="radio" value="getMyResults" />My Results<br />
		</c:when>
	</c:choose>
	<%
		}
	%>
	<table style="margin: auto; border: 4px double black;">
		<tr>
			<td>
				<form action="Test" method="post">
					<input name="page" type="hidden" value="signup" />
					<fieldset>
						<legend>First Time Here? : </legend>
						<p>
							First Name:<input name="firstName" type="text" /><br /> Last
							Name:<input name="lastName" type="text" /><br /> Login: <input
								name="login" type="text" /><br /> Password: <input
								name="password" type="password" /><br /> <input type="submit"
								value="signUp" />
						</p>
					</fieldset>
				</form>
			</td>
		</tr>
	</table>
</body>
</html>