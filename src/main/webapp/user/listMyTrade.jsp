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
   TableData dataTable = null;
   //
   String userID = (String) session.getAttribute(SESSION_ATTRIBUTE_USER);
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
   int lookbackdays = getIntFromParamMap("lookbackdays", request.getParameterMap());
   if (lookbackdays < 1) {
      lookbackdays = 30;
   }
   //
   int limit = getIntFromParamMap("limit", request.getParameterMap());
   if (limit < 1) {
      limit = 10;
   }
   //
   if (data == null) {
      data = Trade.selectMyTrade(userID, lookbackdays, limit);
      request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
      //
      dataTable = (TableData) (data.get(DATA_NAME_DATA));
   }
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form method="post">
   <%
      out.println("<input type='hidden' name='action' value='buyerSummary'/>");
      //
      out.println("<table>");
      out.println("<tr>");
      out.println("<td>");
      out.println("<div align='left' class='allField'>Lookback Days");
      out.println("<input type='NUMBER' name='lookbackdays'value='" + lookbackdays + "' /></div>");
      out.println("</td>");
      //
      out.println("<td>");
      out.println("<div align='left' class='allField'>Limit");
      out.println("<input type='NUMBER' name='limit'value='" + limit + "' /></div>");
      out.println("</td>");
      //
      out.println("<td>");
      out.println("<input type='submit' value='Submit'>");
      out.println("</td>");
      out.println("</tr");
      out.println("</table>");
   %>
</form>

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
