<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="b361asd.auction.db.Bid" %>
<%@ page import="static b361asd.auction.servlet.IConstant.*" %>
<%@ page import="static b361asd.auction.db.DBBase.*" %>

<html>

<head>
    <meta charset="utf-8">
    <title>BuyMe - List Bids</title>
    <link rel="stylesheet" href='../style.css'/>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form id="form-sort" target="_self" method="post">
    <input id="input-sort" type="hidden" name="sort" value="_"/>

    <%
        Map data;
        TableData dataTable = null;
        //
        String action = getStringFromParamMap("action", request.getParameterMap());
        if (action.equals("repSearchBid")) {
            data = Bid.searchBid(request.getParameterMap(), null, null);
            request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
        }
        else if (action.equals("repBrowseBid")) {
            data = Bid.searchBid(request.getParameterMap(), null, null);
            request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
        }
        else {
            data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
            if (data != null) {
                dataTable = (TableData) (data.get(DATA_NAME_DATA));
            }
            //
            if (dataTable == null) {
            }
            else {
                String sort = getStringFromParamMap("sort", request.getParameterMap());
                dataTable.sortRowPerHeader(sort);
            }
        }
        //
        //
        dataTable = (TableData) (data.get(DATA_NAME_DATA));
        //
        request.setAttribute("dataTable", dataTable);
    %>


    <%@include file="../showTableTwo.jsp" %>

</form>

</body>

</html>
