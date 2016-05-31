<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ attribute name="title" fragment="true" %>
<%@ attribute name="style" fragment="true" %>
<%@ attribute name="buttons" fragment="true" %>
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
            margin-top: 60px;
            height: 30px;
            border: solid 1px black;
        }
        #buttons form {
            position: absolute;
            top: 3px;
            left: 3px;
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
        #buttons .button2 {
            left: 206px;
        }
        #buttons .button3 {
            left: 409px;
        }
        #buttons .button4 {
            left: 612px;
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
        <jsp:invoke fragment="buttons"/>
    </div>

    <div id="body">
        <jsp:doBody/>
    </div>

    <div id="footer">
        <div id="info">
            <div id="info1">localhost</div>
            <div id="info2"><%= request.getSession().getId() %></div>
        </div>
    </div>
</body>
</html>