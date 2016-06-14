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
    <title>Користувач</title>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${requestScope.customer ne null}">
      <c:url value="/orders" var="ordersURL">
        <c:param name="email" value="${requestScope.customer.email}"></c:param>
      </c:url>
      <c:url value="/items" var="itemsURL">
        <c:param name="email" value="${requestScope.customer.email}"></c:param>
      </c:url>

      <table>
        <tr>
          <th>Ім'я</th>
          <td><c:out value="${requestScope.customer.name}"></c:out></td>
        </tr>
        <tr>
          <th>Прізвище</th>
          <td><c:out value="${requestScope.customer.surname}"></c:out></td>
        </tr>
        <tr>
          <th>Електронна пошта</th>
          <td><c:out value="${requestScope.customer.email}"></c:out></td></tr>
        <tr>
          <th>Зробленo замовлень</th>
          <td>
            <a href='<c:out value="${ordersURL}" escapeXml="false"></c:out>'>
              <c:out value="${fn:length(requestScope.customer.orders)}"></c:out>
            </a>
          </td>
        </tr>
        <tr>
          <th>Замовлено товарів</th>
          <td>
            <a href='<c:out value="${itemsURL}" escapeXml="false"></c:out>'>
              <c:out value="${fn:length(requestScope.customer.items)}"></c:out>
            </a>
          </td>
        </tr>
        <tr>
          <th>Статус</th>
          <td>
            <c:choose>
              <c:when test="${requestScope.customer.enabled eq true}">
                ОК
              </c:when>
              <c:otherwise>
                Забанений
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>