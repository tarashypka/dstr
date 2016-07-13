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
    <jsp:invoke fragment="title"/>
</head>
<body>

    <!-- header -->
    <c:url value="/home" var="homeURL"/>
    <a href="${homeURL}">Customers&Orders</a>
    <c:url value="/controller" var="controller"/>
    <form action="${controller}" method="get">
        <input type="hidden" name="action" value="showItems">
        <input type="submit" value="Усі товари">
    </form>
    <c:choose>
        <c:when test="${sessionScope.customer eq null}">
            <form action="${controller}" method="get">
                <input type="hidden" name="action" value="login">
                <input type="submit" value="Увійти в систему">
            </form>

            <form action="${controller}" method="get">
                <input type="hidden" name="action" value="register">
                <input type="submit" value="Зареєструватись">
            </form>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${sessionScope.customer.role eq 'customer'}">
                    <c:url value="/controller/customer" var="customerController"/>
                    <form action="${customerController}" method="get">
                        <input type="hidden" name="action" value="showItems">
                        <input type="submit" value="Куплені товари">
                    </form>
                    <form action="${customerController}" method="get">
                        <input type="hidden" name="action" value="showOrders">
                        <input type="submit" value="Мої замовлення">
                    </form>
                    <form action="${customerController}" method="get">
                        <input type="hidden" name="action" value="newOrder">
                        <input type="submit" value="Нове замовлення">
                    </form>
                </c:when>
                <c:otherwise>
                    <c:url value="/controller/admin" var="adminController"/>
                    <form action="${adminController}" method="get">
                        <input type="hidden" name="action" value="showOrders">
                        <input type="submit" value="Усі замовлення">
                    </form>
                    <form action="${adminController}" method="get">
                        <input type="hidden" name="action" value="showCustomers">
                        <input type="submit" value="Усі замовники">
                    </form>
                    <form action="${adminController}" method="get">
                        <input type="hidden" name="action" value="newItem">
                        <input type="submit" value="Додати товар">
                    </form>
                </c:otherwise>
            </c:choose>
            <form action="${controller}" method="post">
                <input type="hidden" name="action" value="logout">
                <input type="submit" value="Вийти із системи">
            </form>
        </c:otherwise>
    </c:choose>

    <jsp:invoke fragment="bar"/>

    <!-- page -->
    <jsp:invoke fragment="error"/>

    <jsp:doBody/>

    <!-- footer -->
    <div id="host">localhost</div>
    <div id="session"><%= request.getSession().getId() %></div>
</body>
</html>
