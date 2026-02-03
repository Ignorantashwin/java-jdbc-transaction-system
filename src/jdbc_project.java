import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbc_project {
private static final String url = "jdbc:mysql://localhost:3306/school";
private static final String username = "root";
private static final String password = "password";

public static void main(String[] args) throws SQLException{
    try {
        Connection connection = DriverManager.getConnection( url, username, password);
      Statement statement = connection.createStatement();

      // (for data insert using both statement )

    //    String hj = String.format("INSERT INTO student ( id, name, email, age, dob) VALUES(%d, '%s', '%s', %d, '%s' )",9, "Mia Khalifa", "Miakhalifa12@gmail.com", 28, "1995-03-14");
    //    int rowsaa = statement.executeUpdate(hj);


    //   String hf = "INSERT INTO student(name, email, age, dob) VALUES(?, ?, ?, ?) ";
    //   PreparedStatement ps = connection.prepareStatement(hf);
    //   ps.setString(1, "Nidhi");
    //   ps.setString(2, "Nidhi2@gmail.com");
    //   ps.setInt(3, 17);
    //   ps.setString(4, "2008-07-20");
      
    //   ps.setString(1, "Arjun" );
    //   ps.setString(2, "Arjun1@gmail.com");
    //   ps.setInt(3, 33);
    //   ps.setString(4, "1990-09-19");

    //   int rowa = ps.executeUpdate();

       // (using statement )

    //   String Query = String.format("INSERT INTO student(name, email, age, dob) VALUES('%s', '%s', %o, '%s')","Kunal", "Kunal2@gmail.com", 22, "2002-02-02");
    //   int rows = statement.executeUpdate(Query);
    //   if (rows>0) {
    //     System.out.println("Data Updated Successfully");
    //   } else System.out.println("Data insertion failed");

       
      //      ( for data update using PreparedStatement )
       
    //  String que = "UPDATE student SET age = ? WHERE id = ?";
    //  PreparedStatement preparedStatement= connection.prepareStatement(que);
    //  preparedStatement.setInt(1, 10);
    //  preparedStatement.setInt(2, 5);
    //  int rows = preparedStatement.executeUpdate();

     //       ( for data delete using preparedStatement)
      
    //    String op = "DELETE FROM student WHERE id = ?";
    //    PreparedStatement preparedStatement = connection.prepareStatement(op);
    //    preparedStatement.setInt(1, 6);
    //    int rowsaffected = preparedStatement.executeUpdate();
        
     
       
        //  for get or retreive the data

           // ( by prepared statement )

    //     String Query = "SELECT * FROM student";
    //     PreparedStatement op = connection.prepareStatement(Query);
    //   ResultSet resultSet = op.executeQuery();
    //   while (resultSet.next()) {
    //     System.out.println();
    //     System.out.println("Id = " + resultSet.getInt("id"));
    //     System.out.println("Name = " + resultSet.getString("name"));
    //     System.out.println("Email = " + resultSet.getString("email"));
    //     System.out.println("Age = " + resultSet.getInt("age"));
    //   }

         
    //    // String query = "select * FROM student";
    //    ResultSet resultSet = statement.executeQuery(query);
    //     while (resultSet.next()) {
    //    int id = resultSet.getInt("id");
    //    String name = resultSet.getString("name");
    //    String email = resultSet.getString("email");
    //    int age  = resultSet.getInt("age");
    //    String dob = resultSet.getString("dob");
    //    System.out.println();
    //    System.out.println("Id = " + id);
    //    System.out.println("Name = " + name);
    //    System.out.println("Email = " + email);
    //    System.out.println("Age = " + age );
    //    System.out.println("Dob = " +dob);

     //  }

    } catch (SQLException e) {
       System.out.println(e.getMessage());
    }
}
}