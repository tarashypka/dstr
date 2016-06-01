<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ attribute name="title" fragment="true" %>
<%@ attribute name="style" fragment="true" %>
<%@ attribute name="buttons" fragment="true" %>
<%@ attribute name="error" fragment="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <jsp:invoke fragment="title"/>
    <style>
        body {
            position: relative;
        }
        #header, #buttons, #footer {
            position: absolute;
            left: 10px;
            right: 10px;
        }
        #header {
            background-color: crimson;
            top: 10px;
            height: 40px;
            border: solid 1px black;
        }
        #header div {
            position: relative;
            text-align: center;
        }
        #header div a {
            color: white;
            text-decoration: none;
            font-size: 30px;
        }
        #header div a:hover {
            color: greenyellow;
        }
        #buttons {
            background-color: coral;
            top: 60px;
            height: 30px;
            border: solid 1px black;
        }
        #buttons form {
            position: absolute;
            top: 3px;
        }
        #buttons form input {
            background-color: crimson;
            height: 24px;
            border: solid 1px darkgreen;
            width: 200px;
            font-family: cursive;
            font-size: 14px;
            color: white;
        }
        #buttons form input:hover {
            background-color: brown;
            border: solid 1px white;
            cursor: hand;
        }
        #buttons .button1 {
            left: 3px;
        }
        #buttons .button2 {
            left: 206px;
        }
        #buttons .button3 {
            left: 409px;
        }
        #buttons .button4 {
            left: 612px;
        }
        #buttons .button8 {
            right: 206px;
        }
        #buttons .button9, .button10 {
            right: 3px;
        }
        #footer {
            background-color: coral;
            bottom: 10px;
            height: 20px;
            border: solid 1px black;
        }
        #footer #info {
            position: relative;
            bottom: 2px;
            font-family: cursive;
            font-size: 15px;
        }
        #footer #info1 {
            position: absolute;
            left: 5px;
        }
        #footer #info2 {
            position: absolute;
            right: 5px;
        }
        #body #error {
            position: fixed;
            font-family: cursive;
            font-size: 20px;
            color: red;
        }
    </style>
    <jsp:invoke fragment="style"/>
<body>
    <div id="header">
        <c:url value="/home" var="homeURL"></c:url>
        <div>
            <a href='<c:out value="${homeURL}"></c:out>'>Customers&Orders</a>
        </div>
    </div>

    <div id="buttons">
        <c:url value="/items" var="itemsURL"></c:url>

        <c:choose>
            <c:when test="${sessionScope.customer eq null}">
                <c:url value="/login" var="loginURL"></c:url>
                <c:url value="/register" var="registrationURL"></c:url>

                <form class="button1"
                      action='<c:out value="${itemsURL}"></c:out>' method="get">
                    <input type="submit" value="Усі товари">
                </form>
                <form class="button8"
                      action='<c:out value="${loginURL}"></c:out>' method="get">
                    <input type="submit" value="Увійти в систему">
                </form>
                <form class="button9"
                      action='<c:out value="${registrationURL}"></c:out>' method="get">
                    <input type="submit" value="Зарегіструватись">
                </form>
            </c:when>
            <c:otherwise>
                <c:url value="/orders" var="ordersURL"></c:url>
                <c:url value="/logout" var="logoutURL"></c:url>

                <c:choose>
                    <c:when test="${sessionScope.customer.role eq 'customer'}">
                        <c:url value="/order/add" var="addOrderURL"></c:url>

                        <form class="button1"
                              action='<c:out value="${itemsURL}"></c:out>' method="get">
                            <input type="submit" value="Мої товари">
                        </form>
                        <form class="button2"
                              action='<c:out value="${ordersURL}"></c:out>' method="get">
                            <input type="submit" value="Мої замовлення">
                        </form>
                        <form class="button3"
                              action='<c:out value="${addOrderURL}"></c:out>' method="get">
                            <input type="submit" value="Нове замовлення">
                        </form>
                    </c:when>
                    <c:when test="${sessionScope.customer.role eq 'admin'}">
                        <c:url value="/customers" var="customersURL"></c:url>
                        <c:url value="/item/add" var="addItemURL"></c:url>

                        <form class="button1"
                              action='<c:out value="${itemsURL}"></c:out>' method="get">
                            <input type="submit" value="Усі товари">
                        </form>
                        <form class="button2"
                              action='<c:out value="${ordersURL}"></c:out>' method="get">
                            <input type="submit" value="Усі замовлення">
                        </form>
                        <form class="button3"
                              action='<c:out value="${customersURL}"></c:out>' method="get">
                            <input type="submit" value="Усі замовники">
                        </form>
                        <form class="button4"
                              action='<c:out value="${addItemURL}"></c:out>' method="get">
                            <input type="submit" value="Додати товар">
                        </form>
                    </c:when>
                </c:choose>
                <form class="button10"
                      action='<c:out value="${logoutURL}"></c:out>' method="post">
                    <input type="submit" value="Вийти із системи">
                </form>
            </c:otherwise>
        </c:choose>

        <jsp:invoke fragment="buttons"/>
    </div>

    <div id="body">
        <jsp:doBody/>
        <div id="error">
            <jsp:invoke fragment="error"/>
        </div>
    </div>

    <div id="footer">
        <div id="info">
            <div id="info1">localhost</div>
            <div id="info2"><%= request.getSession().getId() %></div>
        </div>
    </div>
</body>
</html>