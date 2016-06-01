<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 01.06.16
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Товар</title>
  </jsp:attribute>

  <jsp:attribute name="style">
    <style>
      #body form {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
      #body form input, select {
        background-color: whitesmoke;
        width: 300px;
        height: 35px;
        font-family: cursive;
        font-size: large;
        text-align: center;
      }
      #body form input.text, select {
        background-color: whitesmoke;
        margin-top: 5px;
        border: solid 2px gray;
      }
      #body form select {
        text-align-last: center;
      }
      #body form input.text:hover, input.text:focus, select:hover {
        color: darkgreen;
        border: solid 2px darkgreen;
      }
      #body form input.submit {
        background-color: olivedrab;
        color: white;
        margin-top: 20px;
        border: solid 2px darkgreen;
      }
      #body form input.submit:hover {
        background-color: darkolivegreen;
        border: solid 2px white;
        cursor: hand;
      }
      #body #error {
        top: 30%;
        left: 50%;
        transform: translate(-50%, -30%);
      }
    </style>
  </jsp:attribute>

  <jsp:attribute name="error">
    <c:if test="${requestScope.errtype ne null}">
      <c:choose>
        <c:when test="${requestScope.errtype eq 'category'}">
          Введіть категорію
        </c:when>
        <c:when test="${requestScope.errtype eq 'priceNull'}">
          Введіть ціну
        </c:when>
        <c:when test="${requestScope.errtype eq 'priceNegative'}">
          Ціну введено невірно
        </c:when>
        <c:when test="${requestScope.errtype eq 'currency'}">
          Виберіть валюту
        </c:when>
        <c:when test="${requestScope.errtype eq 'leftNull'}">
          Введіть кількість одиниць товару
        </c:when>
        <c:when test="${requestScope.errtype eq 'leftNegative'}">
          Поле залишилось введено невірно
        </c:when>
      </c:choose>
    </c:if>
  </jsp:attribute>

  <jsp:body>
    <c:url value="/item/add" var="itemAddURL"></c:url>
    <c:set var="price"
           value="${(requestScope.item.price ne -1.0) ? requestScope.item.price : ''}" />
    <c:set var="left"
           value="${(requestScope.item.left ne -1) ? requestScope.item.left : ''}" />

    <form action='<c:out value="${itemAddURL}"></c:out>' method="post">
      <input class="text" type="text" value="${requestScope.item.category}"
             placeholder="Категорія" name="category"><br>
      <input class="text" type="number" value="${price}"
             placeholder="Ціна" name="price"><br>
      <select name="currency">
        <option value="USD">долар США ($)</option>
        <option value="EUR">євро (€)</option>
        <option value="UAH">гривня (₴)</option>
        <option value="RUB">російський рубль (₽)</option>
      </select>
      <br>
      <input class="text" type="number" value="${left}"
             placeholder="Залишилось" name="left"><br>
      <input class="submit" type="submit" value="Додати товар">
    </form>
  </jsp:body>
</t:genericpage>