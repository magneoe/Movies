<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movies</title>
<style>
th {
	padding-top: 12px;
	padding-bottom: 12px;
	text-align: center;
	background-color: #737373;
	color: #e6e6e6;
}

tr {
	background-color: #f2f2f2;
}

#commentsContainer {
	width: 25%;
	height: auto;
	background-color: #e6e6e6;
	border-radius: 10px;
	overflow: auto;
	border: 1px solid #d9d9d9;
}

#commentInfo {
	color: #bfbfbf;
	font-style: italic;
	font-size: 12px;
}

#commentTitle {
	font-size: 17px;
	font-weight: bold;
}

#commentText {
	font-size: 14px;
	min-height: 50px;
}

#paddingContainer {
	margin-left: 20px;
	margin-right: 20px;
	margin-top: 10px;
	margin-bottom: 10px;
}

.commentText {
	font-family: Arial, Helvetica, sans-serif;
}
</style>
</head>
<body>
	<h1>Details</h1>
	<p style="color:red">${errorMessage}</p>
	<table style="border: 1px">
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
			<td>${movie.averageRating}/10<br/>Total of ${movie.ratings.size()} votes</td>
			<td><img src="${movie.posterUrl}" width="150" alt="poster" /></td>
			<td>${movie.plot}</td>
			<td>[<c:forEach items="${movie.actors}" var="actor">
					${actor.name},
				</c:forEach>]
			</td>
		</tr>
	</table>
	<h2>Vote:</h2>
	<form action="${contextPath}/movies/vote" method="POST">
	<input type="hidden" name="movieId" value="${movie.id}" />
		<select name="rating">
			<c:forEach begin="1" end="10" step="1" var="i">
				<c:choose>
					<c:when test="${currentRating != null && currentRating.rating == i}">
						<option value="${i}" label="${i}" selected="selected"/>
					</c:when>
					<c:otherwise>
						<option value="${i}" label="${i}"/>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<input type="submit" value="Vote"/>
	</form>
	<c:if test="${currentRating != null}">
		You rated this movie: ${currentRating.rating}
	</c:if>
	<h3>Comments:</h3>
	<c:forEach items="${movie.comments}" var="comment">
		<div id="commentsContainer">
			<div id="paddingContainer">
				<p class="commentText" id="commentTitle">${comment.title}</p>
				<hr />
				<p class="commentText" id="commentInfo">
					Author: ${comment.author.name} ${comment.author.lastname}<br />
					Date:
					<fmt:formatDate pattern="hh:mm dd/MM/yyyy"
						value="${comment.created}" />
				</p>
				<p class="commentText" id="commentText">${comment.comment}</p>
			</div>
		</div>
	</c:forEach>
	Add comment:
	<form action="${contextPath}/movies/submitComment" method="POST">
		<input type="hidden" name="movieId" value="${movie.id}" />
		<table>
			<tr>
				<td><textarea rows="1" cols="50" name="title" id="title"
						placeholder="Title..."></textarea></td>
			</tr>
			<tr>
				<td><textarea rows="8" cols="50" name="comment" id="comment"
						placeholde="Comment..."></textarea></td>
			</tr>
			<tr>
				<td><input type="submit" value="Post comment" /></td>
			</tr>
		</table>
	</form>
</body>
</html>

