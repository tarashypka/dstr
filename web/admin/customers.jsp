<%@ page import="com.hazelcast.core.Hazelcast" %>
<%@ page import="java.util.List" %>
<%@ page import="model.customer.Customer" %>

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
  <title>dstr: Замовники</title>
  <style>
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
    #err4 {
      top: 155px;
    }
    .success, .error {
      position: fixed;
      top: 215px;
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
    form[name=butt0] {
      top: 50px;
    }
    form[name=butt1] {
      top: 20px;
    }
    form[name=butt2] {
      top: 20px;
      left: 230px;
    }
    form[name=butt3] {
      top: 20px;
      left: 435px;
    }
    form[name=inp1] {
      top: 75px;
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

  <c:url value="/admin/addCustomer" var="addCustomerURL"></c:url>
  <c:url value="/admin/editCustomer" var="editCustomerURL"></c:url>
  <c:url value="/admin/showItems" var="showItemsURL"></c:url>
  <c:url value="/admin/showCustomers" var="showCustomersURL"></c:url>
  <c:url value="/admin/showOrders" var="showOrdersURL"></c:url>

  <form action='<c:out value="${addCustomerURL}"></c:out>' method="get" name="butt0">
    <input type="submit" value="Створити нового користувача">
  </form>

  <%-- Customer Add/Edit logic --%>
  <c:if test="${requestScope.errtype ne null}">
    <strong class="err">
      <c:choose>
        <c:when test="${requestScope.errtype eq 'name'}">
          <div id="err1">Введіть ім'я</div>
        </c:when>
        <c:when test="${requestScope.errtype eq 'surname'}">
          <div id="err2">Введіть прізвище</div>
        </c:when>
        <c:when test="${requestScope.errtype eq 'email'}">
          <div id="err3">Електронну пошту введено невірно</div>
        </c:when>
        <c:when test="${requestScope.errtype eq 'password'}">
          <div id="err4">Занадто короткий пароль</div>
        </c:when>
      </c:choose>
    </strong>
  </c:if>

  <%-- Edit Request --%>
  <c:if test="${requestScope.customer ne null}">
    <form action='<c:out value="${editCustomerURL}"></c:out>' method="post" name="inp1">
      <input type="text" value="${requestScope.customer.name}"
             placeholder="Ім'я" name="name"><br>
      <input type="text" value="${requestScope.customer.surname}"
             placeholder="Прізвище" name="surname"><br>
      <input type="text" value="${requestScope.customer.email}"
             placeholder="Електронна пошта" name="email"><br>
      <input type="text" value="${requestScope.customer.password}"
             placeholder="Пароль" name="password"><br>
      <input type="submit" value="Редагувати користувача">
    </form>
  </c:if>

  <%-- Add Request --%>
  <c:if test="${requestScope.customer eq null}">
    <form action='<c:out value="${addCustomerURL}"></c:out>' method="post" name="inp1">
      <input type="text" value="${requestScope.addcustomer.name}"
             placeholder="Ім'я" name="name"><br>
      <input type="text" value="${requestScope.addcustomer.surname}"
             placeholder="Прізвище" name="surname"><br>
      <input type="text" value="${requestScope.addcustomer.email}"
             placeholder="Електронна пошта" name="email"><br>
      <input type="text" value="${requestScope.addcustomer.password}"
             placeholder="Пароль" name="password"><br>
      <input type="submit" value="Створити користувача">
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

  <form action='<c:out value="${showItemsURL}"></c:out>' method="get" name="butt1">
    <input type="submit" value="Товари">
  </form>

  <form action='<c:out value="${showCustomersURL}"></c:out>' method="get" name="butt2">
    <input type="submit" value="Користувачі">
  </form>

  <form action='<c:out value="${showOrdersURL}"></c:out>' method="get" name="butt3">
    <input type="submit" value="Замовлення">
  </form>

  <%
    List<Customer> customers =
            Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("CUSTOMERS");
    request.setAttribute("customers", customers);
  %>

  <%-- Customers List Logic --%>
  <c:if test="${not empty requestScope.customers}">
    <table>
        <tr>
          <th>Ім'я</th>
          <th>Прізвище</th>
          <th>Електронна пошта</th>
          <th>Пароль</th>
        </tr>
        <c:forEach items="${requestScope.customers}" var="customer">
          <c:url value="/admin/editCustomer" var="editCustomerURL">
            <c:param name="email" value="${customer.email}"></c:param>
          </c:url>
          <c:url value="/admin/deleteCustomer" var="deleteCustomerURL">
            <c:param name="email" value="${customer.email}"></c:param>
          </c:url>
          <tr>
            <td><c:out value="${customer.name}"></c:out></td>
            <td><c:out value="${customer.surname}"></c:out></td>
            <td><c:out value="${customer.email}"></c:out></td>
            <td><c:out value="${customer.password}"></c:out></td>
            <td>
              <a href='<c:out value="${editCustomerURL}" escapeXml="true"></c:out>'>
                Редагувати
              </a>
            </td>
            <td>
              <a href='<c:out value="${deleteCustomerURL}" escapeXml="true"></c:out>'>
                Видалити
              </a>
            </td>
          </tr>
        </c:forEach>
    </table>
  </c:if>
</body>
</html>