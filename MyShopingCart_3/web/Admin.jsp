<%-- 
    Document   : Admin
    Created on : Jan 29, 2014, 12:05:17 AM
    Author     : wndessy
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<c:out value="${sessionScope.name}"></c:out>
 <c:if test="${sessionScope.name != Admin}" > 
     <c:redirect url="index.php"></c:redirect>
 </c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <li><a href="index.jsp">Place  an Order</a></li>
                <li><a href=login.jsp?name="profile">login</a></li>
                <li><a href=manageItem.jsp?name="profile">item</a></li>
                <li><a href=ManageComponents.jsp?name="profile">component</a></li>
                <li><a href=ManageComponents.jsp?name="profile">Users</a></li>
                <li><a href=ManageComponents.jsp?name="profile">Purchases</a></li>

        <h1>Hello World!</h1>
    </body>
</html>
