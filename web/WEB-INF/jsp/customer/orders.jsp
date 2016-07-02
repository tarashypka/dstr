<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 4:26
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
    <c:if test="${requestScope.orders ne null}">
      <table>
        <tr>
          <th>№ замовлення</th>
          <th>Дата</th>
          <th>Товар</th>
          <th>Кількість</th>
          <th>Вартість</th>
          <th>Валюта</th>
        </tr>
        <c:forEach items="${requestScope.orders}" var="order">
          <tr>
            <td><c:out value="${order.orderNumber}"></c:out></td>
            <td><c:out value="${order.date}"></c:out></td>
            <td>
              <c:forEach items="${order.items}" var="item">
                <c:url value="/item" var="itemURL">
                  <c:param name="id" value="${item.key.id}"></c:param>
                </c:url>
                <a href='<c:out value="${itemURL}" escapeXml="false"></c:out>'>
                  <c:out value="${item.key.id}"></c:out><br>
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
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>