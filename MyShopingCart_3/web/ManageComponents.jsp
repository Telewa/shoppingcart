<%-- 
    Document   : ManageComponents
    Created on : Jan 28, 2014, 3:20:15 PM
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
        <title>MY SHopping Cart</title>
    </head>
    <body>
        <div id="">
            <h1>Hello World!</h1>
            <form action="ManageComponent"  method="GET">

                Component name  <input type="text" name="Name"/>
                Price per Component  <input type="text" name="price"/>
                Description      <input type="text" name="description">
                <button type="submit" class="myclass"> Add </button> 
            </form>
    </div>
        <div id="container">
            <table>
                <tr> <td>component_id</td>  <td>Name</td> <td>number_of_units</td> <td>price_per_item</td> <td>description</td></tr>                    <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/shoppingcart" user="comp408" password="comp408"/>
                <sql:query dataSource="${name}" var="itemName">
                    SELECT * FROM component;
                </sql:query>
                <c:forEach  var="row" items="${itemName.rows}">
                    <tr> <td>${row.compnt_id}</td>  <td>${row.name}</td> <td>${row.number_of_units}</td> <td>${row.price_per_item}</td> <td>${row.description}</td></tr>  

                </c:forEach>
               
            </table>
        </div>
                <form action="Addstock" method="GET"
                <div> Item to update<select name="itemToAdd">
                    <%-- a loop for piking itema from the database and adding to the dropdown--%>
                    <sql:setDataSource var="name" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/shoppingcart" user="comp408" password="comp408"/>
                    <sql:query dataSource="${name}" var="itemName">
                        SELECT name FROM component;
                    </sql:query>
                    <c:forEach  var="row" items="${itemName.rows}">
                        <option value="${row.name}" > <c:out value="${row.name}"> </c:out></option> 
                    </c:forEach>   
                </select>  <br> 
                Quantity to add  <input type="text" name="quantity"/>
                <button type="submit" >Update</button>
                </form>
    </body>
</html>
