<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 14.07.16
  Time: 18:24
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">Order</jsp:attribute>

  <jsp:body>
    <c:if test="${order ne null}">
      <c:set var="customer" value="${order.customer}"/>
      <c:url var="customerURL" value="/controller/customer">
        <c:param name="action" value="showCustomer"/>
        <c:param name="id" value="${customer.id}"/>
      </c:url>
      <table class="table table-bordered table-striped">
        <tr>
          <th>Order №</th>
          <td>${order.orderNumber}</td>
        </tr>
        <tr>
          <th>Date</th>
          <td>${order.date}</td>
        </tr>
        <tr>
          <th>Customer</th>
          <td>
            <a href="${customerURL}">
              ${customer.name} ${customer.surname}
            </a>
          </td>
        </tr>
          <th>Items</th>
          <td>
            <c:forEach var="item" items="${order.items}">
              <c:url var="itemURL" value="/controller">
                <c:param name="action" value="showItem"/>
                <c:param name="id" value="${item.key.id}"/>
              </c:url>
              ${item.value} <a href="${itemURL}">${item.key.name}</a><br>
            </c:forEach>
          </td>
        </tr>
        <tr>
          <th>Receipt</th>
          <td>
            <c:forEach var="price" items="${order.receipt}">
              ${price.value} ${price.key}<br>
            </c:forEach>
          </td>
        </tr>
        <tr>
          <th>Status</th>
          <td>${order.status.name}</td>
        </tr>
      </table>
      <c:if test="${sessionScope.customer.role eq 'admin'}">
        <c:url var="changeStatusURL" value="/controller/admin">
          <c:param name="action" value="changeOrderStatus"/>
          <c:param name="id" value="${order.id}"/>
        </c:url>
        <a href="${changeStatusURL}&status=REJECTED">Reject</a>
        <a href="${changeStatusURL}&status=IN_PROCESS">Put in process</a>
        <a href="${changeStatusURL}&status=PROCESSED">Process</a>
      </c:if>
    </c:if>
  </jsp:body>
</t:genericpage>
