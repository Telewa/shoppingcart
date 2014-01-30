<%-- 
    Document   : login.jsp
    Created on : Dec 30, 2013, 9:27:42 PM
    Author     : wndessy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <div id="parentlogin">
            <div>
                <form action="controler" method="POST">
                <label >Email Address</label><input type="text" name="email"/>
                <label>Password</label><input type="text" name="password"/>
                <button type="submit" name="submit">Submit</button>
                </form>
            </div>
            
        </div>
        <div id="link">
            <a href=signup.jsp?name="profile">Don't Have An Account?</a></li>
        </div>      
      
      
    </body>
</html>
