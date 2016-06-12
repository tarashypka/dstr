<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 01.06.16
  Time: 22:10
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Редагувати товар</title>
  </jsp:attribute>

  <jsp:attribute name="error">
    <c:if test="${requestScope.error ne null}">
      <c:out value="${requestScope.error}"/>
    </c:if>
  </jsp:attribute>

  <jsp:body>
    <c:url value="/item/edit" var="itemEditURL"/>
    <c:set var="price"
           value="${(requestScope.item.price ge 0.0) ?
           requestScope.item.price : ''}"/>
    <c:set var="stocked"
           value="${(requestScope.item.status.stocked ge 0) ?
           requestScope.item.status.stocked : ''}"/>
    <c:set var="reserved"
           value="${(requestScope.item.status.reserved ge 0) ?
           requestScope.item.status.reserved : ''}"/>
    <c:set var="sold"
           value="${(requestScope.item.status.sold ge 0) ?
           requestScope.item.status.sold : ''}"/>

    <form action='<c:out value="${itemEditURL}"></c:out>' method="post">
      <input type="hidden" value="${requestScope.item.id}" name="id">
      <input type="text" value="${requestScope.item.category}"
             placeholder="Категорія" name="category"><br>
      <input type="number" step="0.01" value="${price}"
             placeholder="Ціна" name="price"><br>
      <select name="currency">
        <option value="USD">долар США ($)</option>
        <option value="EUR">євро (€)</option>
        <option value="UAH">гривня (₴)</option>
        <option value="RUB">російський рубль (₽)</option>
      </select><br>
      <input type="number" value="${stocked}"
             placeholder="Залишилось" name="status.stocked"><br>
      <input type="number" value="${reserved}"
             placeholder="Зарезервовано" name="status.reserved"><br>
      <input type="number" value="${sold}"
             placeholder="Продано" name="status.sold"><br>
      <input type="submit" value="Редагувати товар">
    </form>
  </jsp:body>
</t:genericpage>