<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" fragment="true" %>
<%@ attribute name="style" fragment="true" %>
<%@ attribute name="bar" fragment="true" %>
<%@ attribute name="error" fragment="true" %>

<html>
<head>
    <link rel="shortcut icon"
          href="<c:out value='${pageContext.request.contextPath}'/>/resources/favicon.ico"/>
    <title><jsp:invoke fragment="title"/></title>
</head>
<body>

    <!-- header -->
    <c:url var="homeURL" value="/"/>
    <a href="${homeURL}">Customers&Orders</a>
    <c:url var="controller" value="/controller"/>
    <form action="${controller}" method="get">
        <input type="hidden" name="action" value="showItems">
        <input type="submit" value="All items">
    </form>
    <c:set var="customer" value="${sessionScope.customer}"/>
    <c:choose>
        <c:when test="${customer eq null}">
            <form action="${controller}" method="get">
                <input type="hidden" name="action" value="login">
                <input type="submit" value="Login">
            </form>
            <form action="${controller}" method="get">
                <input type="hidden" name="action" value="register">
                <input type="submit" value="Register">
            </form>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${customer.role eq 'admin'}">
                    <c:url var="adminController" value="/controller/admin"/>
                    <form action="${adminController}" method="get">
                        <input type="hidden" name="action" value="showOrders">
                        <input type="submit" value="All orders">
                    </form>
                    <form action="${adminController}" method="get">
                        <input type="hidden" name="action" value="showCustomers">
                        <input type="submit" value="All customers">
                    </form>
                    <form action="${adminController}" method="get">
                        <input type="hidden" name="action" value="newItem">
                        <input type="submit" value="Add item">
                    </form>
                </c:when>
                <c:otherwise>
                    <c:url var="customerController" value="/controller/customer"/>
                    <form action="${customerController}" method="get">
                        <input type="hidden" name="action" value="showCustomer">
                        <input type="submit" value="My account">
                    </form>
                    <form action="${customerController}" method="get">
                        <input type="hidden" name="action" value="showItems">
                        <input type="submit" value="My items">
                    </form>
                    <form action="${customerController}" method="get">
                        <input type="hidden" name="action" value="showOrders">
                        <input type="submit" value="My orders">
                    </form>
                    <form action="${customerController}" method="get">
                        <input type="hidden" name="action" value="newOrder">
                        <input type="submit" value="New order">
                    </form>
                </c:otherwise>
            </c:choose>
            <form action="${controller}" method="post">
                <input type="hidden" name="action" value="logout">
                <input type="submit" value="Logout">
            </form>
        </c:otherwise>
    </c:choose>

    <jsp:invoke fragment="bar"/>

    <!-- page -->
    <c:if test="${requestScope.error ne null}">${requestScope.error}</c:if>
    <jsp:doBody/>

    <!-- footer -->
    <div id="host">localhost</div>
    <div id="session"><%= request.getSession().getId() %></div>
</body>
</html>
