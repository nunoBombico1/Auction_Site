<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="auction.db.Trade" %>
<%@ page import="auction.gui.TableData" %>
<%@ page import="static auction.servlet.IConstant.*" %>
<%@ page import="static auction.db.DBBase.*" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Search Offers</title>
   <link rel="stylesheet" href='../style.css'/>
</head>

<body>

<%
   Map data = null;
   TableData dataTable;
   //
   String action = getStringFromParamMap("action", request.getParameterMap());
   if (action.equals("sort")) {
      data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
      //
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
   //
   if (data == null) {
      data = Trade.summaryBy(-1, true, false, false, false, false);
      request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   }
   //
   dataTable = (TableData) (data.get(DATA_NAME_DATA));
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<table>
   <thead>
   <tr>
      <%
         out.println(dataTable.printDescriptionForTable(false));
      %>
   </tr>
   <tr>
      <%
         out.println(dataTable.printHeaderForTable());
      %>
   </tr>
   </thead>
   <tbody>
   <%
      if (dataTable.rowCount() > 0) {
         for (int i = 0; i < dataTable.rowCount(); i++) {
            out.println("<tr>");
            out.println(dataTable.printOneRowInTable(i));
            out.println("</tr>");
         }
      }
   %>
   </tbody>
</table>

</body>

</html>
