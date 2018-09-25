<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create movie</title>
<style type="text/css">
	.formError {
		color: red;
	}
</style>
</head>
<body>
	<h1>Create movie</h1>
	<p class="formError">${error}</p>
	<c:forEach items="${formErrors}" var="formError">
		<p class="formError">${formError}</p>
	</c:forEach>
	<form action="${contextPath}/movies/newMovie" method="POST">
		<table>
			<tr>
				<td>Title</td>
				<td>
					<input type="text" name="title" value="${newMovie.title}"/>
				</td>
			</tr>
			<tr>
				<td>Year</td>
				<td>
					<input type="text" name="year" value="${newMovie.year}"/>
				</td>
			</tr>
			<tr>
				<td>Actors</td>
				<td>
					<!-- <select name="actors">
						<c:forEach items="${actors}" var="actor">
							<option value="${actor.id}">${actor.name}</option>
						</c:forEach>
					</select> -->
					<form:checkboxes items="${actors}" path="newMovie.actors" title="Actors:"/>
				</td>
			</tr>
			<tr>
				<td>Genres</td>
				<td>
					<select name="genres">
						<c:forEach items="${genres}" var="genre">
							<option value="${genre.id}">${genre.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Created (DD-MM-YYYY)</td>
				<td>
					<input type="text" name="createdDate" value="${newMovie.createdDate}"/>
				</td>
			</tr>
			<tr>
				<td>Duration (mins)</td>
				<td>
					<input type="text" name="duration" value="${newMovie.duration}"/>
				</td>
			</tr>
			<tr>
				<td>IMDB rating</td>
				<td>
					<input type="text" name="imdbRating" value="${newMovie.imdbRating}"/>
				</td>
			</tr>
			<tr>
				<td>Orginal title</td>
				<td>
					<input type="text" name="orginalTitle" value="${newMovie.orginalTitle}"/>
				</td>
			</tr>
			<tr>
				<td>Plot</td>
				<td>
					<input type="text" name="plot" value="${newMovie.plot}"/>
				</td>
			</tr>
			<tr>
				<td>Poster url</td>
				<td>
					<input type="text" length="60" name="posterUrl" value="${newMovie.posterUrl}"/>
				</td>
			</tr>
			<tr>
				<td>Release date (DD-MM-YYYY)</td>
				<td>
					<input type="text" name="releaseDate" value="${newMovie.releaseDate}"/>
				</td>
			</tr>
			<tr>
				<td>Story line</td>
				<td>
					<input type="text" name="storyLine" value="${newMovie.storyLine}"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="Create"/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>