<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>Customer</title>
  </jsp:attribute>

  <jsp:body>
    <c:set var="customer" value="${requestScope.customer}"/>
    <c:if test="${customer ne null}">
      <c:url var="customerController" value="/controller/customer">
        <c:param name="email" value="${customer.email}"/>
      </c:url>

      <table>
        <tr>
          <th>Name</th>
          <td>${customer.name}</td>
        </tr>
        <tr>
          <th>Surname</th>
          <td>${customer.surname}</td>
        </tr>
        <tr>
          <th>Email</th>
          <td>${customer.email}</td>
        </tr>
        <tr>
          <th>Orders</th>
          <td>
            <a href="${customerController}&action=showOrders">
              ${fn:length(requestScope.customer.orders)}
            </a>
          </td>
        </tr>
        <tr>
          <th>Items</th>
          <td>
            <a href="${customerController}&action=showItems">
              ${fn:length(requestScope.customer.items)}
            </a>
          </td>
        </tr>
        <tr>
          <th>Status</th>
          <td>
            <c:choose>
              <c:when test="${customer.enabled}">ОК</c:when>
              <c:otherwise>Banned</c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>