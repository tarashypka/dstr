<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 5:42
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>dstr: Замовник</title>
  <style>
    form {
      top: 20px;
      position: fixed;
    }
    form input {
      width: 200px;
      height: 25px;
    }
    form[name=butt1] {
      left: 25px;
    }
    form[name=butt2] {
      left: 230px;
    }
    form[name=butt3] {
      left: 435px;
    }
  </style>
</head>
<body>

  <c:url value="/customer/addOrder" var="makeOrderURL"></c:url>
  <c:url value="/customer/showItems" var="showItemsURL"></c:url>
  <c:url value="/customer/showOrders" var="showOrdersURL"></c:url>

  <form action='<c:out value="${makeOrderURL}"></c:out>' method="get" name="butt1">
    <input type="submit" value="Зробити замовлення">
  </form>

  <form action='<c:out value="${showItemsURL}"></c:out>' method="get" name="butt2">
    <input type="submit" value="Мої товари">
  </form>

  <form action='<c:out value="${showOrdersURL}"></c:out>' method="get" name="butt3">
    <input type="submit" value="Мої замовлення">
  </form>

</body>
</html>
