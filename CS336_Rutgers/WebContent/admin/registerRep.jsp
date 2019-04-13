<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Register a Representative</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form action="${pageContext.request.contextPath}/admin/registerRepResult.jsp" method="post">

	<%@include file="../registerCommon.jsp" %>

</form>

</body>

</html>
