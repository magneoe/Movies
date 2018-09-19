<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>

	<h1>Login</h1>
	
	<p style="color: red">${errorMessage}</p>
	<h2>${successMessage}</h2>
	<p>Log in with username/password</p>
	<form action="${contextPath}/login" name="f" method="POST">
		<table>
			<tr>
				<td><input type="text" name="username" /></td>
			</tr>
			<tr>
				<td><input type="password" name="password" /></td>
			</tr>
			<tr>
				<td><input type="hidden" 
					name="${_csrf.parameterName}"
					value="${_csrf.token}" /> <input type="submit" value="Login" /></td>
			</tr>
		</table>
	</form>
</body>
</html>