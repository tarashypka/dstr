<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 01.06.16
  Time: 1:44
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">Customer</jsp:attribute>

  <jsp:body>
    <c:if test="${customer ne null}">
      <c:url var="customerController" value="/controller/customer">
        <c:param name="id" value="${customer.id}"/>
      </c:url>
      <table class="table table-bordered table-striped">
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
              ${requestScope.nOrders}
            </a>
          </td>
        </tr>
        <tr>
          <th>Items</th>
          <td>
            <a href="${customerController}&action=showItems">
              ${requestScope.nItems}
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
      <c:if test="${sessionScope.customer.role eq 'admin'}">
        <c:url var="swapStatusURL" value="/controller/admin">
          <c:param name="action" value="swapCustomerStatus"/>
          <c:param name="id" value="${customer.id}"/>
        </c:url>
        <a href="${swapStatusURL}">
          <c:choose>
            <c:when test="${customer.enabled}">Ban</c:when>
            <c:otherwise>Unban</c:otherwise>
          </c:choose>
        </a>
      </c:if>
    </c:if>
  </jsp:body>
</t:genericpage>