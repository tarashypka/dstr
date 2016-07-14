<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 4:26
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>My Orders</title>
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
    <c:if test="${requestScope.orders ne null}">
      <table>
        <tr>
          <th>Order №</th>
          <th>Date</th>
          <th>Item</th>
          <th>Amount</th>
          <th>Price</th>
          <th>Status</th>
        </tr>
        <c:forEach var="order" items="${requestScope.orders}">
          <c:url var="orderURL" value="/controller/customer">
            <c:param name="action" value="showOrder"/>
            <c:param name="id" value="${order.id}"/>
          </c:url>
          <tr>
            <td><a href="${orderURL}">${order.orderNumber}</a></td>
            <td>${order.date}</td>
            <td>
              <c:forEach var="item" items="${order.items}">
                <c:url var="itemURL" value="/controller">
                  <c:param name="action" value="showItem"/>
                  <c:param name="id" value="${item.key.id}"/>
                </c:url>
                <a href="${itemURL}">${item.key.id}</a><br>
              </c:forEach>
            </td>
            <td>
              <c:forEach var="item" items="${order.items}">
                ${item.value}<br>
              </c:forEach>
            </td>
            <td>
              <c:forEach var="price" items="${order.receipt}">
                ${price.value} ${price.key}<br>
              </c:forEach>
            </td>
            <c:set var="status" value="${order.status}"/>
            <td>${status.name}</td>
            <c:url var="swapStatusURL" value="/controller/customer">
              <c:param name="action" value="changeOrderStatus"/>
              <c:param name="id" value="${order.id}"/>
            </c:url>
            <c:choose>
              <c:when test="${status.value eq -1}">
                <td><a href="${swapStatusURL}">Put in process</a></td>
              </c:when>
              <c:when test="${status.value eq 0}">
                <td><a href="${swapStatusURL}">Reject</a></td>
              </c:when>
            </c:choose>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>