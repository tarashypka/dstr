<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 02.06.16
  Time: 3:08
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Нове замовлення</title>
  </jsp:attribute>

  <jsp:attribute name="error">
    <c:if test="${requestScope.error ne null}">
      <c:out value="${requestScope.error}"/>
    </c:if>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${sessionScope.orderItems ne null}">
      <table id="order">
        <tr>
          <th>ID</th>
          <th>Кількість</th>
          <th>Вартість</th>
          <th>Валюта</th>
        </tr>
        <c:forEach items="${sessionScope.orderItems}" var="item">
          <tr>
            <td><c:out value="${item.key.id}"/></td>
            <td><c:out value="${item.value}"/></td>
            <td><c:out value="${item.key.price * item.value}"/></td>
            <td><c:out value="${item.key.currency}"/></td>
          </tr>
        </c:forEach>
        <tr>
          <c:url value="/order/add" var="orderAddURL"/>
            <form action='<c:out value="${orderAddURL}"></c:out>' method="post">
              <input type="hidden" name="action" value="order">
              <td><input type="submit" value="Зробити замовлення"></td>
            </form>
          <td>
            <c:forEach items="${sessionScope.receipt}" var="price">
              <c:out value="${price.value}"/>
              <c:out value="${price.key}"/><br>
            </c:forEach>
          </td>
        </tr>
      </table>
    </c:if>

    <c:if test="${sessionScope.items ne null}">
      <table id="items">
        <tr>
          <th>ID</th>
          <th>Категорія</th>
          <th>Ціна</th>
          <th>Валюта</th>
          <th>Залишилось</th>
        </tr>
        <c:forEach items="${sessionScope.items}" var="item">
          <c:url value="/item" var="itemURL">
            <c:param name="id" value="${item.key.id}"></c:param>
          </c:url>
          <tr>
            <td>
              <a href='<c:out value="${itemURL}"></c:out>'>
                <c:out value="${item.key.id}"></c:out>
              </a>
            </td>
            <td><c:out value="${item.key.category}"></c:out></td>
            <td><c:out value="${item.key.price}"></c:out></td>
            <td><c:out value="${item.key.currency}"></c:out></td>
            <td><c:out value="${item.key.left}"></c:out></td>
              <c:url value="/order/add" var="orderAddURL"/>
              <form action='<c:out value="${orderAddURL}"></c:out>' method="post">
                <input type="hidden" name="action" value="item">
                <input type="hidden" name="id" value='<c:out value="${item.key.id}"/>'>
                <td><input type="number" name="quantity"
                           value="${item.value}" placeholder="Кількість"></td>
                <td><input type="submit" value="Додати товар"></td>
              </form>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>