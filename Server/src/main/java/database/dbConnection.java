package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {
    public static Connection mysqlConnection(){
        Connection connection = null;
        try
        {
            /*
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grpcDB?"+"user=root&password=root");

             */
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gRPC_demo_users?"+"user=root&password=MySql63!");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
