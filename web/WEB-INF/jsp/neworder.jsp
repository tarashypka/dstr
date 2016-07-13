<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 17.05.16
  Time: 21:31
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
    <c:url var="customerController" value="/controller/customer"/>
    <c:set var="order" value="${sessionScope.order}"/>
    <c:if test="${order ne null and order.items ne null}">
      <table id="order">
        <tr>
          <th>ID</th>
          <th>Кількість</th>
          <th>Вартість</th>
          <th>Валюта</th>
        </tr>
        <c:forEach items="${order.items}" var="item">
          <tr>
            <td><c:out value="${item.key.id}"/></td>
            <td><c:out value="${item.value}"/></td>
            <td><c:out value="${item.key.price * item.value}"/></td>
            <td><c:out value="${item.key.currency}"/></td>
          </tr>
        </c:forEach>
        <tr>
          <form action="${customerController}" method="post">
            <input type="hidden" name="action" value="makeOrder">
            <td><input type="submit" value="Зробити замовлення"></td>
          </form>
          <td>
            <c:forEach items="${order.receipt}" var="price">
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
          <c:url var="controller" value="/controller">
            <c:param name="action" value="showItem"/>
            <c:param name="id" value="${item.id}"/>
          </c:url>
          <tr>
            <td>
              <a href="${controller}"><c:out value="${item.id}"/></a>
            </td>
            <td><c:out value="${item.category}">Category?</c:out></td>
            <td><c:out value="${item.price}">Price?</c:out></td>
            <td><c:out value="${item.currency}">Currency?</c:out></td>
            <td><c:out value="${item.status.stocked}">How much in stock?</c:out></td>
            <form action="${customerController}" method="post">
              <input type="hidden" name="action" value="addOrderItem">
              <input type="hidden" name="id" value="${item.id}"/>
              <td><input type="number" name="quantity" placeholder="Кількість"></td>
              <td><input type="submit" value="Додати товар"></td>
            </form>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>