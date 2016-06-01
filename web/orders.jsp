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
    <title>Замовлення</title>
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
    <c:if test="${sessionScope.customer ne null && sessionScope.orders ne null}">
      <table>
        <tr>
          <th>№ замовлення</th>
          <th>Дата</th>
          <c:if test="${sessionScope.customer.role eq 'admin'}">
            <th>Замовник</th>
          </c:if>
          <th>Товар</th>
          <th>Кількість</th>
          <th>Вартість</th>
          <th>Валюта</th>
        </tr>
        <c:forEach items="${sessionScope.orders}" var="order">
          <tr>
            <td><c:out value="${order.orderNumber}"></c:out></td>
            <td><c:out value="${order.date}"></c:out></td>

            <c:if test="${sessionScope.customer.role eq 'admin'}">
              <c:url value="/customer" var="customerURL">
                <c:param name="email" value="${order.customer.email}"></c:param>
              </c:url>
              <td>
                <a href='<c:out value="${customerURL}" escapeXml="false"></c:out>'>
                  <c:out value="${order.customer.name} ${order.customer.surname}"></c:out>
                </a>
              </td>
            </c:if>

            <td>
              <c:forEach items="${order.items}" var="item">
                <c:url value="/item" var="itemURL">
                  <c:param name="id" value="${item.key}"></c:param>
                </c:url>
                <a href='<c:out value="${itemURL}" escapeXml="false"></c:out>'>
                    <c:out value="${item.key}"></c:out><br>
                </a>
              </c:forEach>
            </td>
            <td>
              <c:forEach items="${order.items}" var="item">
                <c:out value="${item.value}"></c:out><br>
              </c:forEach>
            </td>
            <td>
              <c:forEach items="${order.receipt}" var="price">
                <c:out value="${price.key}"></c:out><br>
              </c:forEach>
            </td>
            <td>
              <c:forEach items="${order.receipt}" var="price">
                <c:out value="${price.value}"></c:out><br>
              </c:forEach>
            </td>

            <c:if test="${sessionScope.customer.role eq 'customer'}">
              <c:url value="/orders/edit" var="editOrderURL">
                <c:param name="id" value="${order.id}"></c:param>
              </c:url>
              <c:url value="/orders/delete" var="deleteOrderURL">
                <c:param name="id" value="${order.id}"></c:param>
              </c:url>
              <td>
                <a href='<c:out value="${editOrderURL}" escapeXml="false"></c:out>'>
                  Редагувати
                </a>
              </td>
              <td>
                <a href='<c:out value="${deleteOrderURL}" escapeXml="false"></c:out>'>
                  Видалити
                </a>
              </td>
            </c:if>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>
