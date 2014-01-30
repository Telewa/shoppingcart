<%-- 
    Document   : manageItem.jsp
    Created on : Jan 4, 2014, 10:48:30 PM
    Author     : wndessy
--%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div id="">
            <h1>Hello World!</h1>
            <form action="manageItem"  method="GET">
                
                Item name  <input type="text" name="ItemName"></br>
                Components <select multiple  name="components" >
                    <%-- a loop for piking itema from the database and adding to the dropdown--%>
                    <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/shoppingcart" user="comp408" password="comp408"/>
                    <sql:query dataSource="${name}" var="itemName">
                        SELECT name FROM component;
                    </sql:query>
                    <c:forEach  var="row" items="${itemName.rows}">
                        <option value="${row.name}" > <c:out value="${row.name}"> </c:out></option> 
                    </c:forEach>   
                </select>  <br> 
                price  <input type="text" name="price">

                <button type="submit" class="myclass"> Add</button> 
            </form>
        </div>
        <div id="container">
            <table>
                <tr> <td>id</td> <td>item name</td> <td>Price</td> <td> components</td></tr>
                <sql:query dataSource="${name}" var="itemName">
                    SELECT * FROM product;
                </sql:query>
                <c:forEach  var="row" items="${itemName.rows}">
                    <tr> <td>${row.product_id}</td>  <td>${row.name}</td> <td>${row.components}</td> <td>${row.price}</td> </tr>      
                </c:forEach>
            </table>
        </div>
    </body>
</html>
