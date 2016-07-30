<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- MainController URLs -->
<c:url var="homeGET" value="/"/>
<c:url var="showItemsGET" value="/controller">
  <c:param name="action" value="showItems"/>
</c:url>
<c:url var="loginGET" value="/controller">
  <c:param name="action" value="login"/>
</c:url>
<c:url var="registerGET" value="/controller">
  <c:param name="action" value="register"/>
</c:url>

<!-- AdminController URLs -->
<c:url var="showOrdersGET" value="/controller/admin">
  <c:param name="action" value="showOrders"/>
</c:url>
<c:url var="showCustomersGET" value="/controller/admin">
  <c:param name="action" value="showCustomers"/>
</c:url>
<c:url var="newItemGET" value="/controller/admin">
  <c:param name="action" value="newItem"/>
</c:url>

<!-- CustomerController URLs -->
<c:url var="myAccountGET" value="/controller/customer">
  <c:param name="action" value="showCustomer"/>
</c:url>
<c:url var="myItemsGET" value="/controller/customer">
  <c:param name="action" value="showItems"/>
</c:url>
<c:url var="myOrdersGET" value="/controller/customer">
  <c:param name="action" value="showOrders"/>
</c:url>
<c:url var="newOrderGET" value="/controller/customer">
  <c:param name="action" value="newOrder"/>
</c:url>
<c:url var="logoutPOST" value="/controller">
  <c:param name="action" value="logout"/>
</c:url>



