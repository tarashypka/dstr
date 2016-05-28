<%@ page import="com.hazelcast.core.Hazelcast" %>
<%@ page import="java.util.List" %>
<%@ page import="model.item.Item" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 17.05.16
  Time: 21:31
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"
         language="java" pageEncoding="UTF-8" session="true" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>dstr: Товари</title>
  <style>
    #worker, #ip, #session {
      position: fixed;
      left: 5px;
    }
    #worker {
      bottom: 45px;
    }
    #ip {
      bottom: 25px;
    }
    #session {
      bottom: 5px;
    }
    .err div {
      color: red;
      position: fixed;
      left: 230px;
    }
    #err1 {
      top: 65px;
    }
    #err2 {
      top: 95px;
    }
    #err3 {
      top: 125px;
    }
    .success, .error {
      position: fixed;
      top: 190px;
      left: 25px;
    }
    .success {
      color: green;
    }
    .error {
      color: red;
    }
    form {
      position: fixed;
      left: 25px;
    }
    form input {
      width: 200px;
      height: 25px;
    }
    form[name=butt1] {
      top: 25px;
    }
    form[name=butt2] {
      top: 25px;
      left: 230px;
    }
    form[name=butt3] {
      top: 25px;
      left: 435px;
    }
    form[name=inp1] {
      top: 50px;
      margin-top: 5px;
    }
    form[name=inp1] input {
      margin-top: 5px;
    }
    table {
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
    table,th,td {
      border: 1px solid black;
    }
  </style>
</head>
<body>
  <div id="worker">worker: no workers</div>
  <div id="ip">ip: localhost</div>
  <div id="session">session: <%= request.getSession().getId() %></div>

  <c:url value="/admin/addItem" var="addItemURL"></c:url>
  <c:url value="/admin/editItem" var="editItemURL"></c:url>
  <c:url value="/admin/showItems" var="showItemsURL"></c:url>

  <form action='<c:out value="${addItemURL}"></c:out>' method="get" name="butt1">
    <input type="submit" value="Додати новий товар">
  </form>

  <%-- User Add/Edit logic --%>
  <c:if test="${requestScope.errtype ne null}">
    <strong class="err">
      <c:choose>
        <c:when test="${requestScope.errtype eq 'category'}">
          <div id="err1">Введіть категорію</div>
        </c:when>
        <c:when test="${requestScope.errtype eq 'price'}">
          <div id="err2">Некоректна ціна</div>
        </c:when>
        <c:when test="${requestScope.errtype eq 'currency'}">
          <div id="err3">Виберіть тип валюти</div>
        </c:when>
      </c:choose>
    </strong>
  </c:if>

  <%-- Edit Request --%>
  <c:if test="${requestScope.item ne null}">
    <form action='<c:out value="${editItemURL}"></c:out>' method="post" name="inp1">
      <input type="text" value="${requestScope.item.id}"
             placeholder="ID" name="id" readonly><br>
      <input type="text" value="${requestScope.item.category}"
             placeholder="Категорія" name="category"><br>
      <input type="text" value="${requestScope.item.price}"
             placeholder="Ціна" name="price"><br>
      <input type="text" value="${requestScope.item.currency}"
             placeholder="Валюта" name="currency"><br>
      <input type="submit" value="Редагувати товар">
    </form>
  </c:if>

  <%-- Add Request --%>
  <c:if test="${requestScope.item eq null}">
    <form action='<c:out value="${addItemURL}"></c:out>' method="post" name="inp1">
      <input type="text" value="${requestScope.additem.category}"
             placeholder="Категорія" name="category"><br>
      <input type="text" value="${requestScope.additem.price}"
             placeholder="Ціна" name="price"><br>
      <input type="text" value="${requestScope.additem.currency}"
             placeholder="Валюта" name="currency"><br>
      <input type="submit" value="Додати товар">
    </form>
  </c:if>

  <c:if test="${requestScope.success ne null}">
    <strong class="success">
      <c:out value="${requestScope.success}"></c:out>
    </strong>
  </c:if>
  <c:if test="${requestScope.error ne null}">
    <strong class="error">
      <c:out value="${requestScope.error}"></c:out>
    </strong>
  </c:if>

  <form action='<c:out value="${showItemsURL}"></c:out>' method="get" name="butt2">
    <input type="submit" value="Вивести всі товари">
  </form>

  <form action="/admin/customers" method="get" name="butt3">
    <input type="submit" value="Показати користувачів">
  </form>

  <%
    List<Item> items =
            Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("ITEMS");
    request.setAttribute("items", items);
  %>

  <%-- Users List Logic --%>
  <c:if test="${not empty requestScope.items}">
    <table>
      <tbody>
        <tr>
          <th>ID</th>
          <th>Категорія</th>
          <th>Ціна</th>
          <th>Валюта</th>
        </tr>
        <c:forEach items="${requestScope.items}" var="item">
          <c:url value="/admin/editItem" var="editItemURL">
            <c:param name="id" value="${item.id}"></c:param>
          </c:url>
          <c:url value="/admin/deleteItem" var="deleteItemURL">
            <c:param name="id" value="${item.id}"></c:param>
          </c:url>
          <tr>
            <td><c:out value="${item.id}"></c:out></td>
            <td><c:out value="${item.category}"></c:out></td>
            <td><c:out value="${item.price}"></c:out></td>
            <td><c:out value="${item.currency}"></c:out></td>
            <td>
              <a href='<c:out value="${editItemURL}" escapeXml="true"></c:out>'>
                Редагувати
              </a>
            </td>
            <td>
              <a href='<c:out value="${deleteItemURL}" escapeXml="true"></c:out>'>
                Видалити
              </a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:if>
</body>
</html>