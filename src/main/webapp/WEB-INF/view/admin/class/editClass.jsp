<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div style="margin: 5%">
		<h1>Add Class Form</h1>

		<form class="form-group" action="<%=request.getContextPath()%>/admin/class/saveEditClass"
			method="POST" style="width: 50%;">
			<input class="form-control" type="hidden" name="classId" value="${data.classId}">
			Class Name: <input class="form-control" type="text" name="className" value="${data.className}"> <br>
			Teacher Id: 
			<select class="form-control" id="teacherId" name="teacherId"> 
				 <c:forEach items="${user}" var="user">
					<option value="${user.userId}" ${user.userId == data.teacherId ? 'selected="selected"' : ''}>${user.getUsername()}</option>

			     </c:forEach>
			</select><br>

			<input class="btn btn-success" type="submit" value="Save"
				style="margin-top: 30px; width: 100%">

		</form>
	</div>

</body>
</html>