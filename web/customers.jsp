<%@ page contentType="text/html" pageEncoding="UTF-8" %>
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
  <jsp:attribute name="title">
    <title>Клієнти</title>
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
    <c:if test="${sessionScope.customer ne null &&
                  sessionScope.customer.role eq 'admin' &&
                  sessionScope.customers ne null}">
      <table>
        <tr>
          <th>Ініціали</th>
          <th>Електронна пошта</th>
          <th>Статус</th>
        </tr>
        <c:forEach items="${sessionScope.customers}" var="customer">
          <c:url value="/customer" var="customerURL">
            <c:param name="email" value="${customer.email}"></c:param>
          </c:url>
          <tr>
            <td>
              <a href='<c:out value="${customerURL}" escapeXml="false"></c:out>'>
                <c:out value="${customer.name} ${customer.surname}"></c:out>
              </a>
            </td>
            <td><c:out value="${customer.email}"></c:out></td>
            <td>
              <c:choose>
                <c:when test="${customer.enabled}">
                  ОК
                </c:when>
                <c:otherwise>
                  Забанений
                </c:otherwise>
              </c:choose>
            </td>
            <c:if test="${sessionScope.customer.role eq 'admin'}">
              <c:url value="/customer/status" var="customerDeleteURL">
                <c:param name="email" value="${customer.email}"></c:param>
              </c:url>
              <td>
                <a href='<c:out value="${customerDeleteURL}" escapeXml="false"></c:out>'>
                  <c:choose>
                    <c:when test="${customer.enabled}">Забанити</c:when>
                    <c:otherwise>Розбанити</c:otherwise>
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
