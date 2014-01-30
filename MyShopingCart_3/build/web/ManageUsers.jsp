<%-- 
    Document   : ManageUsers
    Created on : Jan 28, 2014, 3:38:04 PM
    Author     : wndessy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Shopping Cart</title>
    </head>
    <body>
<div id="container">
            <table>
                    <tr> <td>Id</td> <td>Name</td> <td>Email address</td> <td>Phone number</td></tr>                    <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/shoppingcart" user="comp408" password="comp408"/>
                <sql:query dataSource="${name}" var="itemName">
                    SELECT * FROM Customer;
                </sql:query>
                <c:forEach  var="row" items="${itemName.rows}">
                    <tr> <td>${row.cust_id}</td>  <td>${row.name}</td> <td>${row.email_adress}</td> <td>${row.phone}</td> </tr>  
                    
                </c:forEach>
            </body>
</html>
