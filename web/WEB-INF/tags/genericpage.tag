<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" fragment="true" %>
<%@ attribute name="style" fragment="true" %>
<%@ attribute name="bar" fragment="true" %>
<%@ attribute name="error" fragment="true" %>

<html>
<head>
    <jsp:invoke fragment="title"/>
</head>
<body>

    <!-- header -->
    <c:url value="/home" var="homeURL"></c:url>
    <a href='<c:out value="${homeURL}"></c:out>'>Customers&Orders</a>

    <!-- navigation bar -->
    <c:url value="/items" var="itemsURL"></c:url>

    <c:choose>
        <c:when test="${sessionScope.customer eq null}">
            <c:url value="/login" var="loginURL"></c:url>
            <c:url value="/register" var="registrationURL"></c:url>

            <form action='<c:out value="${itemsURL}"></c:out>' method="get">
                <input type="submit" value="Усі товари">
            </form>

            <form action='<c:out value="${loginURL}"></c:out>' method="get">
                <input type="submit" value="Увійти в систему">
            </form>

            <form action='<c:out value="${registrationURL}"></c:out>' method="get">
                <input type="submit" value="Зареєструватись">
            </form>
        </c:when>
        <c:otherwise>
            <c:url value="/orders" var="ordersURL"></c:url>
            <c:url value="/logout" var="logoutURL"></c:url>

            <c:choose>
                <c:when test="${sessionScope.customer.role eq 'customer'}">
                    <c:url value="/order/add" var="addOrderURL"></c:url>

                    <form action='<c:out value="${itemsURL}"></c:out>' method="get">
                        <input type="submit" value="Мої товари">
                    </form>

                    <form action='<c:out value="${ordersURL}"></c:out>' method="get">
                        <input type="submit" value="Мої замовлення">
                    </form>

                    <form action='<c:out value="${addOrderURL}"></c:out>' method="get">
                        <input type="submit" value="Нове замовлення">
                    </form>
                </c:when>
                <c:when test="${sessionScope.customer.role eq 'admin'}">
                    <c:url value="/customers" var="customersURL"></c:url>
                    <c:url value="/item/add" var="addItemURL"></c:url>

                    <form action='<c:out value="${itemsURL}"></c:out>' method="get">
                        <input type="submit" value="Усі товари">
                    </form>

                    <form action='<c:out value="${ordersURL}"></c:out>' method="get">
                        <input type="submit" value="Усі замовлення">
                    </form>

                    <form action='<c:out value="${customersURL}"></c:out>' method="get">
                        <input type="submit" value="Усі замовники">
                    </form>

                    <form action='<c:out value="${addItemURL}"></c:out>' method="get">
                        <input type="submit" value="Додати товар">
                    </form>
                </c:when>
            </c:choose>

            <form action='<c:out value="${logoutURL}"></c:out>' method="post">
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