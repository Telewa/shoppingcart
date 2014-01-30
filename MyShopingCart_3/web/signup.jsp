<%-- 
    Document   : create_account.jsp
    Created on : Nov 14, 2013, 4:24:25 AM
    Author     : wndessy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel='stylesheet' href='mydefault.css' type='text/css'/> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign up page</title>
    </head>
    <body>

        <div id="bodyContainer">
            <form action="signUp" method="POST">
                        Name:  <input type="text" name="Myname"/><br>
                        Email Address:  <input type="text" name="email"/><br>
                        Telephone:  <input type="number" name="phone"/><br>
                        Password:   <input type="password" name="password"/><br>
                        Confirm Password<input type="password"/>
                        <button type="submit" name="Signup"> Sign Up </button>            
            </form>
        </div>
        </body>
</html>
