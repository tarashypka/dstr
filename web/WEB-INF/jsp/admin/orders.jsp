<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 01.06.16
  Time: 1:04
  To change this template use File | Settings | File Templates.
--%>

<% request.setAttribute("today", new java.util.Date()); %>

<t:genericpage>
  <jsp:attribute name="title">Orders</jsp:attribute>

  <jsp:body>
    <fmt:formatDate var="today" value="${today}" pattern="yyyy-MM-dd"/>
    <c:url var="controller" value="/controller/admin"/>
    <form action="${controller}" method="get">
      <input type="hidden" name="action" value="showOrders">
      <input type="hidden" name="filter" value="date"/>
      <input type="date" name="from" value="${today}"/>
      <input type="date" name="till" value="${today}"/>
      <input type="submit" value="Show"/>
    </form>
    <c:if test="${orders ne null}">
      <table>
        <tr>
          <th>Order №</th>
          <th>Date</th>
          <th>Customer</th>
          <th>Items</th>
          <th>Receipt</th>
          <th>Status</th>
        </tr>
        <c:forEach var="order" items="${orders}">
          <c:url var="orderURL" value="/controller/customer">
            <c:param name="action" value="showOrder"/>
            <c:param name="id" value="${order.id}"/>
          </c:url>
          <c:url var="changeStatusURL" value="/controller/admin">
            <c:param name="action" value="changeOrderStatus"/>
            <c:param name="id" value="${order.id}"/>
          </c:url>
          <c:url var="customerURL" value="/controller/customer">
            <c:param name="action" value="showCustomer"/>
            <c:param name="id" value="${order.customer.id}"/>
          </c:url>
          <tr>
            <td><a href="${orderURL}">${order.orderNumber}</a></td>
            <td>${order.date}</td>
            <td>
              <a href=${customerURL}>
                ${order.customer.name} ${order.customer.surname}
              </a>
            </td>
            <td>
              <c:forEach var="item" items="${order.items}">
                <c:url var="itemURL" value="/controller">
                  <c:param name="action" value="showItem"/>
                  <c:param name="id" value="${item.key.id}"/>
                </c:url>
                ${item.value} <a href="${itemURL}">${item.key.id}</a><br>
              </c:forEach>
            </td>
            <td>
              <c:forEach var="price" items="${order.receipt}">
                ${price.value} ${price.key}<br>
              </c:forEach>
            </td>
            <td>${order.status.name}</td>
            <td><a href="${changeStatusURL}&status=REJECTED">Reject</a></td>
            <td><a href="${changeStatusURL}&status=IN_PROCESS">Put in process</a></td>
            <td><a href="${changeStatusURL}&status=PROCESSED">Process</a></td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>
