<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 01.06.16
  Time: 1:04
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Orders</title>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${requestScope.orders ne null}">
      <table>
        <tr>
          <th>Order №</th>
          <th>Date</th>
          <th>Customer</th>
          <th>Item</th>
          <th>Quantity</th>
          <th>Price</th>
          <th>Currency</th>
          <th>Status</th>
        </tr>
        <c:set var="orders" value="${requestScope.orders}"/>
        <c:forEach var="order" items="${orders}">
          <c:url var="orderURL" value="/controller/admin">
            <c:param name="action" value="showOrder"/>
            <c:param name="id" value="${order.id}"/>
          </c:url>
          <c:url var="customerURL" value="/controller/customer">
            <c:param name="action" value="showCustomer"/>
            <c:param name="email" value="${order.customer.email}"/>
          </c:url>
          <c:url var="changeOrderStatusURL" value="/controller/admin">
            <c:param name="action" value="changeOrderStatus"/>
            <c:param name="id" value="${order.id}"/>
          </c:url>
          <tr>
            <td><a href="${orderURL}">${order.orderNumber}</a></td>
            <td>${order.date}</td>
            <td><a href=${customerURL}>${order.customer.name} ${order.customer.surname}</a></td>
            <td>
              <c:forEach var="item" items="${order.items}">
                <c:url var="itemURL" value="/controller">
                  <c:param name="action" value="showItem"/>
                  <c:param name="id" value="${item.key.id}"/>
                </c:url>
                <a href="${itemURL}">${item.key.id}</a><br>
              </c:forEach>
            </td>
            <td><c:forEach var="item" items="${order.items}">${item.value}<br></c:forEach></td>
            <td><c:forEach var="price" items="${order.receipt}">${price.value}<br></c:forEach></td>
            <td><c:forEach var="price" items="${order.receipt}">${price.key}<br></c:forEach></td>
            <td>${order.status.name}</td>
            <td><a href="${changeOrderStatusURL}&status=REJECTED">Reject</a></td>
            <td><a href="${changeOrderStatusURL}&status=IN_PROCESS">In process</a></td>
            <td><a href="${changeOrderStatusURL}&status=PROCESSED">Processed</a></td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>
