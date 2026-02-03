import java.sql.Statement;
import java.lang.Thread.State;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.cj.protocol.Resultset;

public class Batch_processing_jdbc {
    private static final String url = "jdbc:mysql://localhost:3306/school";
    private static final String username = "root";
    private static final String password = "password";
    public static void main(String[] args ) throws SQLException {
        try{
        Connection connection = DriverManager.getConnection(url, username, password);
          Scanner scanner = new Scanner(System.in);
          // ( Batch Processing using statement )
        
          //Statement statement = connection.createStatement();
        //   while (true) {
        //     System.out.print("Enter your name : " );
        //     String name = sc.next();
        //     System.out.print("Enter your email : ");
        //     String email = sc.next();
        //     System.out.print("Enter your age : ");
        //     int age = sc.nextInt();
        //     System.out.print("Enter your dob : ");
        //     String dob = sc.next();
        //     System.out.print("Enter more data (Y/N)");
        //     String choice = sc.next();
        //     String s = String.format("INSERT INTO student (name, email, age, dob) VALUES('%s','%s', %d,'%s')", name, email, age, dob);
        //     statement.addBatch(s);
        //     if (choice.toUpperCase().equals("N") ) {
        //          break;
        //     } 
        //  }
         //statement.executeBatch();
         //  System.out.println("Batch Inserted Successfully");

          // ( Batch Processing using PreparedStatement )

           String jadu = "INSERT INTO student (name, email, age, dob) VALUES(?,?,?,?)";
          PreparedStatement preparedStatement = connection.prepareStatement(jadu);
         while (true) {
            System.out.print("Enter your name : " );
            String name = scanner.next();
            System.out.print("Enter your email : ");
            String email = scanner.next();
            System.out.print("Enter your age : ");
            int age = scanner.nextInt();
            System.out.print("Enter your dob : ");
            String dob = scanner.next();
            System.out.print("Enter more data (Y/N)");
            String choice = scanner.next();
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, dob);;
            preparedStatement.addBatch();
            if (choice.toUpperCase().equals("N")) {
                break;
            }
            
         }
         preparedStatement.executeBatch();
            System.out.println("Batch added Successfully");
        
        // ////////////////////////////////////////////////////////  ( to read with PreparedStatement ) /////////////////////////////////////////////

         String st = "SELECT * FROM student";
         PreparedStatement preparedStatement1 = connection.prepareStatement(st);
         ResultSet resultset = preparedStatement1.executeQuery();
         while (resultset.next()) {
            System.out.println();
            System.out.println("Id = " + resultset.getInt("id"));
            System.out.println("Name = " + resultset.getString("name"));
            System.out.println("Email = " + resultset.getString("email"));
            System.out.println("Age = " + resultset.getInt("age"));
            System.out.println("Dob = " + resultset.getString("dob"));
        }
            
         
        
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
