<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta charset="UTF-8">
<title>Registration</title>
<style type="text/css">
	.error {
		color: red;
	}
</style>
</head>
<body>
	<h1>Registration:</h1>
<p class="error">
	<c:forEach items="${formErrors}" var="formError">
		${formError}<br/>
	</c:forEach>
	</p>
	<form action="${contextPath}/createUser" method="POST">
		<table>
			<tr>
				<td><label>Email:</label></td>
				<td><input type="text" name="email" value="${user.email}"/></td>
			</tr>
			<tr>
				<td><label>First name:</label></td>
				<td><input type="text" name="name" value="${user.name}"/></td>
			</tr>
			<tr>
				<td><label>Last name:</label></td>
				<td><input type="text" name="lastname" value="${user.lastname}"/></td>
			</tr>
			<tr>
				<td><label>Password:</label></td>
				<td><input type="password" name="password" value="${user.password}"/></td>
			</tr>
			<tr>
				<td><label>Password:</label></td>
				<td><input type="password" name="passwordRepeated" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" name="submitRegistration"
					value="Create user" /></td>
			</tr>
		</table>
	</form>
</body>
</html>