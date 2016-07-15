<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 01.06.16
  Time: 1:44
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Customers</title>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${customers ne null}">
      <table>
        <tr>
          <th>Initials</th>
          <th>Email</th>
          <th>Orders</th>
          <th>Items</th>
          <th>Status</th>
        </tr>
        <c:forEach var="customer" items="${customers}">
          <c:url var="customerController" value="/controller/customer">
            <c:param name="email" value="${customer.email}"/>
          </c:url>
          <tr>
            <td>
              <a href="${customerController}&action=showCustomer">
                ${customer.name} ${customer.surname}
              </a>
            </td>
            <td>${customer.email}</td>
            <td>
              <a href="${customerController}&action=showOrders">
                ${fn:length(customer.orders)}
              </a>
            </td>
            <td>
              <a href="${customerController}&action=showItems">
                ${fn:length(customer.items)}
              </a>
            </td>
            <td>
              <c:choose>
                <c:when test="${customer.enabled}">ОК</c:when>
                <c:otherwise>Banned</c:otherwise>
              </c:choose>
            </td>
            <c:if test="${sessionScope.customer.role eq 'admin'}">
              <c:url var="swapStatusURL" value="/controller/admin">
                <c:param name="action" value="swapCustomerStatus"/>
                <c:param name="id" value="${customer.id}"/>
              </c:url>
              <td>
                <a href="${swapStatusURL}">
                  <c:choose>
                    <c:when test="${customer.enabled}">Ban</c:when>
                    <c:otherwise>Unban</c:otherwise>
                  </c:choose>
                </a>
              </td>
            </c:if>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>
