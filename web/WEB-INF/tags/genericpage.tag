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
    <a href='<c:out value="${homeURL}"/>'>Customers&Orders</a>

    <!-- navigation bar -->
    <c:url value="/items" var="itemsURL"/>

    <c:choose>
        <c:when test="${sessionScope.customer eq null}">
            <c:url value="/login" var="loginURL"/>
            <c:url value="/register" var="registrationURL"/>

            <form action='<c:out value="${itemsURL}"/>' method="get">
                <input type="submit" value="Усі товари">
            </form>

            <form action='<c:out value="${loginURL}"/>' method="get">
                <input type="submit" value="Увійти в систему">
            </form>

            <form action='<c:out value="${registrationURL}"/>' method="get">
                <input type="submit" value="Зареєструватись">
            </form>
        </c:when>
        <c:otherwise>
            <c:url value="/logout" var="logoutURL"/>

            <c:choose>
                <c:when test="${sessionScope.customer.role eq 'customer'}">
                    <c:url value="/controller/customer" var="controllerURL"/>

                    <form action="${controllerURL}" method="get">
                        <input type="hidden" name="action" value="showItems">
                        <input type="submit" value="Куплені товари">
                    </form>
                    <form action="${controllerURL}" method="get">
                        <input type="hidden" name="action" value="showOrders">
                        <input type="submit" value="Мої замовлення">
                    </form>
                    <form action="${controllerURL}" method="get">
                        <input type="hidden" name="action" value="makeOrder">
                        <input type="submit" value="Нове замовлення">
                    </form>
                </c:when>
                <c:otherwise>
                    <c:url value="/items" var="itemsURL"/>
                    <c:url value="/orders" var="ordersURL"/>
                    <c:url value="/customers" var="customersURL"/>
                    <c:url value="/item/add" var="addItemURL"/>

                    <form action='<c:out value="${itemsURL}"/>' method="get">
                        <input type="submit" value="Усі товари">
                    </form>

                    <form action='<c:out value="${ordersURL}"/>' method="get">
                        <input type="submit" value="Усі замовлення">
                    </form>

                    <form action='<c:out value="${customersURL}"/>' method="get">
                        <input type="submit" value="Усі замовники">
                    </form>

                    <form action='<c:out value="${addItemURL}"/>' method="get">
                        <input type="submit" value="Додати товар">
                    </form>
                </c:otherwise>
            </c:choose>

            <form action='<c:out value="${logoutURL}"/>' method="post">
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
