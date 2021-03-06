<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
	integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
	integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
	crossorigin="anonymous"></script>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
	integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
	crossorigin="anonymous"></script>
<title>Movies</title>
<style>
table, tr, td {
	border: 1px solid black;
}
body {
	background-color: #666666;
	color: #d9d9d9;
	font-size: 12px;
	font-family: Arial,sans-serif;
}
.pageHeaderStyle {
	background-color: #80b3ff;
	color: black;
	margin: 10px;
	border-bottom: 2px solid black;
	border-right: 1px solid black;
	elevation: 3px higher;
}
</style>
</head>
<body>
	<div class="jumbotron jumbotron-fluid pageHeaderStyle">
		<div class="container">
			<h1 class="display-4">The Movie DB</h1>
			<p class="lead"><i>A new and exciting Movie database</i></p>
		</div>
	</div>
	<p>${errorMessage}</p>
	<table>
		<tr>
			<th>Title</th>
			<th>Year</th>
			<th>Rating</th>
			<th>Poster</th>
			<th>Details</th>
		</tr>
		<c:forEach items="${movies}" var="movie">
			<tr>
				<td>${movie.title}</td>
				<td>${movie.year}</td>
				<td>${movie.averageRating}/10</td>
				<td><img src="${movie.posterUrl}" width="150" alt="poster" /></td>
				<td><a href="details/${movie.id }">Details</a></td>
			</tr>
		</c:forEach>
	</table>
	<c:choose>
		<c:when test="${user == null }">
			<a href="/login" class="badge badge-primary">Login</a>
			<a href="/createUser" class="badge badge-primary">Create new user</a>
		</c:when>
		<c:when test="${user != null }">
			<p>You are logged in as: ${user.name} ${user.lastname}!</p>
			<c:if test="${user.hasAdminRights()}">
				<a href="/movies/createMovie">Create movie</a>
			</c:if>
			<form action="/logout" method="POST">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /> <input class="btn btn-warning"
					type="submit" value="Logout" />
			</form>
		</c:when>
	</c:choose>
</body>
</html>
