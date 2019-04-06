<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.CategoryAndField" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Post an Offer</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange(value) {
           value.action = "${pageContext.request.contextPath}/user/postOffer.jsp";
           value.submit();
       }
	</script>
</head>

<body>

<%
	String message = "Welcome to BuyMe!";
	if (session == null) {
	}
	else {
		message = (String) session.getAttribute("message");
		if (message == null) {
			message = "Welcome to BuyMe.";
		}
	}
	//
	String categoryNameFromParam = "CAT=" + getStringFromParamMap("categoryName", request.getParameterMap());
	//String paramMap = getParamMap(request.getParameterMap());
	//
	Map data = CategoryAndField.getCategoryField(getStringFromParamMap("categoryName", request.getParameterMap()));
%>
<h1><%=message%>
</h1>

<form action="${pageContext.request.contextPath}/user/postOfferResult.jsp" method="post">

	<select name="categoryName" onchange="onCategoryChange(this.parentElement);">
		<%
			List lstCategory = (List) data.get(CategoryAndField.DATA_CATEGORY_LIST);
			for (Object o : lstCategory) {
				CategoryAndField.Category temp = (CategoryAndField.Category) o;
				out.println("<option " + (temp.isCurr() ? "selected " : "") + "value=\"" + temp.getCategoryName() + "\">" + temp.getCategoryName() + "</option>");
			}
		%>
	</select><br>

	<%
		List lstField = (List) data.get(CategoryAndField.DATA_FIELD_LIST);
		for (Object o : lstField) {
			String fieldName = ((CategoryAndField.Field) o).getFieldName();
			int    fieldID   = ((CategoryAndField.Field) o).getFieldID();
			out.println("<div class='allField'>" + fieldName + "<input type = \"text\" name = \"fieldID_" + fieldID + "\" / ></div><br >");
		}
	%>

	<div>Auction Days<input type="initPrice" name="initPrice" min="1" max="30"></div>
	<div>Auction Days<input type="increment" name="increment" min="1" max="30"></div>
	<div>Auction Days<input type="minPrice" name="minPrice" min="1" max="30"></div>
	<div><select name="conditionCode" onchange="onCategoryChange(this.parentElement);">
		<option value='conditionCode_1'>New</option>
		<option value='conditionCode_2'>Like New</option>
		<option value='conditionCode_3'>Manufacturer Refurbished</option>
		<option value='conditionCode_4'>Seller Refurbished</option>
		<option value='conditionCode_5'>Used</option>
		<option value='conditionCode_6'>For parts or Not Working</option>
	</select></div>
	<div>Item Description<input type="text" name="description"></div>
	<div>Auction Days<input type="number" name="auction_days" min="1" max="30"></div>

	<input type="submit" value="Submit">
</form>

</body>

</html>
