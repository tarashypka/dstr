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
  <jsp:attribute name="title">New item</jsp:attribute>
  <jsp:attribute name="js">add_field.js</jsp:attribute>

  <jsp:body>
    <c:url var="customerController" value="/controller/admin"/>
    <c:set var="status" value="${item.status}"/>
    <c:set var="price" value="${(item.price gt 0.0) ? item.price : null}"/>
    <c:set var="stocked" value="${(status.stocked gt 0) ? status.stocked : null}"/>
    <c:set var="reserved" value="${(status.reserved gt 0) ? status.reserved : null}"/>
    <c:set var="sold" value="${(status.sold gt 0) ? status.sold : null}"/>
    <form action="${customerController}" method="post">
      <input type="hidden" name="action" value="newItem">
      <input type="text" name="category" value="${item.category}" placeholder="Category?"><br>
      <input type="number" name="price" value="${price}" step="0.01" placeholder="Price?"><br>
      <select name="currency">
        <option value="USD">USD ($)</option>
        <option value="EUR">EURO (€)</option>
        <option value="UAH">HRYVNIA (₴)</option>
        <option value="RUB">RUBLE (₽)</option>
      </select><br>
      <input type="number" name="stocked" value="${stocked}" placeholder="Left?"><br>
      <input type="number" name="reserved" value="${reserved}" placeholder="Reserved?"><br>
      <input type="number" name="sold" value="${sold}" placeholder="Sold?"><br>
      <div id="container"></div>
      <input type="button" onclick="addField()" value="Add field">
      <input type="submit" value="Add item">
    </form>
  </jsp:body>
</t:genericpage>