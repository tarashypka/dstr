<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 17.05.16
  Time: 21:31
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>My Items</title>
  </jsp:attribute>

  <jsp:attribute name="style">
    <style>
      #body table {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
      #body table, tr, th, td {
        border: 1px solid black;
        text-align: center;
      }
    </style>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${items ne null}">
      <table>
        <tr>
          <th>ID</th>
          <th>Category</th>
          <th>Price</th>
          <th>Currency</th>
          <th>Amount</th>
          <th>Total price</th>
        </tr>
        <c:forEach var="item" items="${items}">
          <c:url var="itemURL" value="/controller">
            <c:param name="action" value="showItem"/>
            <c:param name="id" value="${item.key.id}"/>
          </c:url>
          <tr>
            <td><a href="${itemURL}">${item.key.id}</a></td>
            <td>${item.key.category}</td>
            <td>${item.key.price}</td>
            <td>${item.key.currency}</td>
            <td>${item.value}</td>
            <td>${item.value * item.key.price}</td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>