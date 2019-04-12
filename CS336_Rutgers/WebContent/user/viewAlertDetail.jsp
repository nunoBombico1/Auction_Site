<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Bid" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - View Alert Details</title>
	<link type="text/css" rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	Map data = null;
	TableData dataTable = null;
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	if (action.equals("sort")) {
		data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
		if (data != null) {
			dataTable = (TableData) (data.get(DATA_NAME_DATA));
			//
			if (dataTable != null) {
				String sort = getStringFromParamMap("sort", request.getParameterMap());
				dataTable.sortRowPerHeader(sort);
			}
			else {
				data = null;
			}
		}
	}
	else if (action.equals("viewAlertDetail")) {
	}
	//
	if (data == null) {
		data = Bid.searchBid(request.getParameterMap(), null);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%@include file="../listBidCommon.jsp" %>

</body>

</html>