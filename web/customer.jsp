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
  <jsp:attribute name="title">
    <title>Клієнт</title>
  </jsp:attribute>

  <jsp:attribute name="style">
    <style>
      #body table {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
      #body table, th, td {
        border: 1px solid black;
        text-align: center;
      }
    </style>
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
          <th>
            <a href='<c:out value="${ordersURL}" escapeXml="false"></c:out>'>
              <c:out value="${requestScope.nOrders}"></c:out>
            </a>
          </th>
        </tr>
        <tr>
          <th>Замовлено товарів</th>
          <th>
            <a href='<c:out value="${itemsURL}" escapeXml="false"></c:out>'>
              <c:out value="${requestScope.nItems}"></c:out>
            </a>
          </th>
        </tr>

      </table>
    </c:if>
  </jsp:body>
</t:genericpage>