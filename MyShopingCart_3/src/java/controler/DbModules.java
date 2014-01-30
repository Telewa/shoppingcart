/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author wndessy
 */
public class DbModules {

    public DbModules() {
    }

    static String user = "comp408";
    static String password = "comp408";
    static String db_url = "jdbc:mysql://localhost:3306/shoppingcart";
    int status = 0;
    static Connection con = null;

    /**
     * This code is for getting connection to the db
     *
     * @return connection string
     */
    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());//remember this
            con = DriverManager.getConnection(db_url, user, password);
            System.out.println("connected");
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
        return con;
    }

    private String isComponent(String name, int qty) {
        String isComponent = null;
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();

            String sql = "select * from component Where name=\"" + name + "\"";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                //
                int number_of_units = rs.getInt("number_of_units");
                if (number_of_units >= qty) {
                    isComponent = "present";
                } else if (number_of_units < qty) {
                    isComponent = "few";
                } else if (number_of_units < 1) {
                    isComponent = "absent";
                }
            } else {
                isComponent = "no_component";
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isComponent;
    }

    private String getComponents(String name) {
        String components = "";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            String sql1 = "select * from product where name =\"" + name + "\"";
            ResultSet rs1 = stmt.executeQuery(sql1);

            if (rs1.next()) {
                String id = rs1.getString("product_id");

                String sql = "select DISTINCT name  from product_component join component Where product_id=\"" + id + "\"";
                ResultSet rs = stmt.executeQuery(sql);

                int i = 0;
                if (rs.next()) {
                    components = "";
                    while (rs.next()) {
                        components += rs.getString("name") + ":";
                    }
                }
                components = (components.substring(0, components.length() - 1)).trim();
                stmt.close();
            }
            rs1.close();

        } catch (SQLException e) {
            System.out.println("Error getting components");
            e.printStackTrace();
        }
        return components;
    }

     public String UpdatePurchase(String data[], Item customerID) {
        String status1 = null;
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();
            String sql = null;

            for (int i = 0; i < data.length; i++) {
                String values[] = data[i].split(",");

                for(int j=0;j<values.length;j++)
                {
                    String [] individual = values[j].split(":");
                    int Quantity = Integer.parseInt(individual[1].trim());
                    String name = individual[0];
                //check if the item ia a product or component
                    //if component, just run as usual
                    if ((isComponent(name, Quantity)).equalsIgnoreCase("present")) {
                        sql = "update component set number_of_units=number_of_units-" + Quantity + " Where name=\"" + name + "\"";

                        //run the query  
                        stmt.executeUpdate(sql);
                        status1 = "Success ";

                    } else if ((isComponent(name, Quantity)).equalsIgnoreCase("absent") || (isComponent(name, Quantity)).equalsIgnoreCase("few")) {
                        status1 = "\"" + name + "\" is not available at the moment :)";
                    } else {

                    //else if is Product,
                        //get its components and for each, run the above query
                        String components = getComponents(name);
                        if (components != null) {
                            String[] split = components.split(":");

                            for (int k = 0; k < split.length; k++) {
                                if ((isComponent(split[k], Quantity)).equalsIgnoreCase("present")) {
                                    sql = "update component set number_of_units=number_of_units-" + 1 * Quantity + " Where name=\"" + split[k] + "\"";

                                    //run the query  
                                    stmt.executeUpdate(sql);
                                    status1 = "Success ";
                                } else if ((isComponent(split[k], Quantity)).equalsIgnoreCase("absent") || (isComponent(split[k], Quantity)).equalsIgnoreCase("few")) {
                                    status1 = name + " is not available because its component \"" + split[k] + "\" is not available at the moment :)";
                                }
                            }
                        } else {
                            status1 = "null components!";
                        }
                    }
                }
            }
            String str1 = Arrays.toString(data);
            //replace starting "[" and ending "]" and ","
            str1 = str1.substring(1, str1.length() - 1).replaceAll(",", "");

            System.out.println("String 1: " + str1);
            if ("success".equalsIgnoreCase(status1.trim())) {
                sql = "insert into `order` (items,customer_id) values('" + str1 + "','" + customerID + "')";
                stmt.executeUpdate(sql);
                status1 = "Success ";
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return status1;
    }

    /**
     * This function inserts an item to the db tablea
     *
     * @param itemName The name of the item to be saved to the db
     * @return int success =1, failure = 0;
     * @throws java.sql.SQLException
     */
    public int insertItemToDb(String itemName) throws SQLException {
        String usql = " insert into product (name) VALUES ('" + itemName + "')";
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getItemFromDb(String itemName) throws SQLException {
        String usql = " select *  from product where name = '" + itemName + "'";
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(usql);
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> GetItemsInDb() throws SQLException {

        String usql = " select count *  from product ";

        Connection conn = this.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(usql);
        ArrayList<String> items = new ArrayList<String>();
        while (rs.next()) {
            items.add(rs.getString("name"));
        }

        ArrayList<String> myItems = items;
        {
        }
        return myItems;
    }

    public int updateItemOnDb(String itemName) throws SQLException {
        String usql = " update customers  set name = '" + itemName + "'";
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeQuery(usql);
            //    System.out.println(this.getItemFromDb(itemName));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * This code is for getting connection to the db
     *
     * @return connection string
     * @throws java.sql.SQLException
     */
    public int InsertPurchaseDetails(String itemName, String customerName) throws SQLException {
        String usql = " select product_id from product where name = '" + itemName + "'";
        String usql2 = " select customer_id from product where name = '" + customerName + "'";
        try {
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(usql);
            ResultSet rs2 = stmt.executeQuery(usql2);

            int itemId = rs.getInt("product_id");
            int customerId = rs2.getInt("customer_id");

            String usql1 = " Insert into order (order_date,product_id,customer_id)values ('" + date() + "','" + itemId + "','" + customerId + "')";
            stmt.executeUpdate(usql1);
            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /* for login*/
    public static Item validateLogin(Item user) {
        System.out.println("email" + user.getEmail() + " pass" + user.getPassword() + "");

        String usql = "SELECT  * FROM customer WHERE email_adress = \"" + user.getEmail() + "\" and  password = \"" + user.getPassword() + "\"";
        //String  usql = "SELECT * FROM customer";
        try {
            Connection conn;
            conn = DbModules.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(usql);
            boolean test = rs.next();
            if (!test) {
                System.out.println("ffailure");
            } else if (test) {
                System.out.println("ssuucceessffuull login");
                user.setEmail(rs.getString("email_adress"));
                user.setPassword(rs.getString("password"));
                //call the findname method from within the class and setting  the return value to name 
                user.setName(DbModules.findName(rs.getString("email_adress")));//
                user.setIsValid(true);
            }
        } catch (SQLException e) {
            System.out.println("an exception occured" + e + "");
        }
        return user;
    }

    //for getting the user name
    public static String findName(String EmailAddress) {
        String usql = "SELECT name FROM customer WHERE email_adress=\"" + EmailAddress + "\"";
        Connection conn = DbModules.getConnection();
        String name = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(usql);
            if(rs.next()){
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static int findCustomerId(String EmailAddress) {
        String usql = "SELECT cust_id FROM customer WHERE email_adress=\"" + EmailAddress + "\"";
        Connection conn = DbModules.getConnection();
        int id = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(usql);
            if(rs.next()){
                id = rs.getInt("cust_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    //for signing up a customer
    public void customerSIgnup(String name, String email, String phone, String password) {
        String usql = "INSERT INTO  customer (name,email_adress,phone,password)"
                + " VALUES ('" + name + "','" + email + "','" + phone + "','" + password + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
            System.out.println(name + "  " + email + " " + phone + "  " + password);
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
    }

    //for adding a component
    public void component_add(String val1, String val2, String val3) {
        String usql = "INSERT INTO into component (component_id,name,price_per_item,description)"
                + " VALUES ('','" + val1 + "','" + val2 + "','" + val3 + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
            System.out.println("statemented");
            stmt.executeQuery(usql);

        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
    }

    //for adding a product
    public void product(String name, String[] components, int price) {
        System.out.println("from db modules" + name + "  " + components);
        String myComponents = "";

         for (int i = 0; i < components.length; i++) {
            myComponents += components[i].trim() + ":";
        }
        myComponents = (myComponents.substring(0, myComponents.length() - 1)).trim();

        System.out.println(myComponents);
        //  String myComponents=Arrays.toString(components);

        String usql = "INSERT INTO product (name,components,price) VALUES ('" + name + "','" + myComponents + "','" + price + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            System.out.println(name + "  " + components);
            stmt.executeUpdate(usql);

            //get the item id for the item just entered
            String sql = " Select product_id from product where name=\"" + name + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int ItemId = rs.getInt("product_id");

                System.out.println("upto here");
                //String Mycomponent=null;

                for (int i = 0; i < components.length; i++) {
                    //get the component id fom each component name
                    String sql2 = "Select compnt_id from component where name=\"" + components[i] + "\"";
                    ResultSet rs1 = stmt.executeQuery(sql2);
                    if (rs1.next()) {
                        int ComponentId = rs1.getInt("compnt_id");
                        String sql3 = "Insert into product_component(product_id,componet_id) values('" + ItemId + "','" + ComponentId + "')";
                        stmt.executeUpdate(sql3);
                    }
                }       //insert the component and product ids into the product component table

            }

        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            sql.printStackTrace();
        }
    }
// for adding components
    
    public void AddComponent(String name,String Description, int price) {

        System.out.println("from db modules" + name + "  " + Description);
        String myComponents = "";

       
        String usql = "INSERT INTO component (name,price_per_item,description) VALUES ('" + name + "','" + price + "','" + Description + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
        
              } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            sql.printStackTrace();
        }
    }
// for adding Stock
    
    public void AddStock(String name,int quantity)  {

             
        String usql = "UPDATE component  Set number_of_units +="+quantity+" WHERE name==\""+name+"\"";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(usql);
        
              } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            sql.printStackTrace();
        }
    }

    
    //for adding a purchase
    public void order(String val1, String val2, String val3) {
        String usql = "INSERT INTO  order (order_id,order_date,item_id,customer_id) "
                + "    VALUES ('','" + val1 + "','" + val2 + "','" + val3 + "')";
        try {
            Connection conn = DbModules.getConnection();
            Statement stmt = conn.createStatement();
            stmt = conn.createStatement();
            System.out.println("statemented");
            stmt.executeQuery(usql);

        } catch (SQLException sql) {
            System.out.println(sql.getMessage());

        }
    }

}
