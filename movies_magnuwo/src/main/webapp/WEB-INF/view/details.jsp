<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movies</title>
<style>
	table, tr, td {
		border: 1px solid black;
	}
</style>
</head>
<body>
	<h1>Details</h1>
	<c:if test="${error != null}">
<p style="color:red">${error}</p>
</c:if>
	<table style="border:1px">
		<tr>
			<th>Title</th>
			<th>Year</th>
			<th>Rating</th>
			<th>Poster</th>
			<th>Plot</th>
			<th>Actors</th>
		</tr>
		<tr>
			<td>${movie.title}</td>
			<td>${movie.year}</td>
			<td>${movie.averageRating}/10</td>
			<td><img src="${movie.posterUrl}" width="150" alt="poster" /></td>
			<td>${movie.plot}</td>
			<td>
				[<c:forEach items="${movie.actors}" var="actor">
					${actor.name},
				</c:forEach>]
			</td>
		</tr>
	</table>
</body>
</html>