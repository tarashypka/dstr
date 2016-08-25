<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
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
  <jsp:attribute name="title">Orders</jsp:attribute>

  <jsp:body>
    <c:if test="${orders ne null}">
      <table class="table table-bordered table-striped">
        <tr>
          <th>Order №</th>
          <th>Date</th>
          <th>Items</th>
          <th>Receipt</th>
          <th>Status</th>
        </tr>
        <c:forEach var="order" items="${orders}">
          <c:url var="orderURL" value="/controller/customer">
            <c:param name="action" value="showOrder"/>
            <c:param name="id" value="${order.id}"/>
          </c:url>
          <c:url var="changeStatusURL" value="/controller/customer">
            <c:param name="action" value="changeOrderStatus"/>
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
                ${item.value} of <a href="${itemURL}">${item.key.name}</a><br>
              </c:forEach>
            </td>
            <td>
              <c:forEach var="cash" items="${order.receipt}">
                ${cash.value} ${cash.key}<br>
              </c:forEach>
            </td>
            <td>${order.status.name}</td>
            <c:choose>
              <c:when test="${order.status.value eq -1}">
                <td><a href="${changeStatusURL}" class="btn btn-default">Put in process</a></td>
              </c:when>
              <c:when test="${order.status.value eq 0}">
                <td><a href="${changeStatusURL}" class="btn btn-default">Reject</a></td>
              </c:when>
            </c:choose>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>
