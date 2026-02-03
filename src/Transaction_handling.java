import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Transaction_handling { 
    private static final String url = System.getenv("DB_URL");
private static final String username = System.getenv("DB_USERNAME");
private static final String password = System.getenv("DB_PASSWORD");
    
    public static void main(String[] args) throws SQLException {
         String debitquery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
         String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
         String Txn_Query = "INSERT INTO Transactions(sender_account, receiver_account, amount, status)" + "VALUES(?, ?, ?, ?) ";
         
     try(
         Connection connection = DriverManager.getConnection(url, username, password);
         Scanner sc = new Scanner(System.in);
        ){
              
            PreparedStatement debiPreparedStatement = connection.prepareStatement(debitquery);
         PreparedStatement crediPreparedStatement = connection.prepareStatement(creditQuery);
         PreparedStatement txnps = connection.prepareStatement(Txn_Query);
            connection.setAutoCommit(false);
            System.out.println();
            System.out.print("Enter your account number : ");
            int sender = sc.nextInt();
            if (!isAvailable(connection, sender)) {
                System.out.println("Account does not exist !");
                return;
            }
            System.out.print("Enter Amount : ");
            double amount = sc.nextDouble();
            System.out.print("Enter receiver account number : ");
            int receiverAccount = sc.nextInt();

            if (!isAvailable(connection, receiverAccount)) {
                System.out.println("Receiver account does not exist");
                return;
            }

            int firstLock = Math.min(sender, receiverAccount);
            int secondLock = Math.max(sender, receiverAccount);
            lockAccount(connection, firstLock);
            lockAccount(connection, secondLock);

             if (!isSufficient(connection, sender, amount) ) {
                connection.rollback();
                insertfailed(connection, txnps, sender, receiverAccount, amount);
                connection.commit();
            System.out.println("Transaction failed ! Insufficient Balance ");
             
            return;
          }
          
          debiPreparedStatement.setDouble(1, amount);
            debiPreparedStatement.setInt(2, sender);

            crediPreparedStatement.setDouble(1, amount);
            crediPreparedStatement.setInt(2, receiverAccount);

         int debitrows = debiPreparedStatement.executeUpdate();
         int creditRows = crediPreparedStatement.executeUpdate();
         

         if (debitrows == 1 && creditRows == 1) {
           inserttransactions(txnps, sender, receiverAccount, amount, "SUCCESS");
            connection.commit();
            System.out.println("Transaction Successful ");

         } else {
            connection.rollback();
            insertfailed(connection, txnps, sender, receiverAccount, amount);
            connection.commit();
            System.out.println("Transaction Failed ");
         }
     } catch(SQLException e){
       System.out.println(e.getMessage());
     }

    }

    static boolean isSufficient(Connection connection, int account_number, double amount) throws SQLException{
        String check = "SELECT balance FROM accounts WHERE account_number = ?";
       try( PreparedStatement pre = connection.prepareStatement(check);){
        pre.setInt(1, account_number);
        ResultSet resultSet = pre.executeQuery();
        if (resultSet.next()) {
           return resultSet.getDouble("balance") >= amount;
        }
        return false;
       }
    }

    static boolean isAvailable(Connection connection, int account ) throws SQLException {
        String check12 = "SELECT 1 FROM accounts WHERE account_number = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(check12)){
            preparedStatement.setInt(1, account);
            ResultSet resultSet = preparedStatement.executeQuery(); 
           return resultSet.next();
    }
}
static void lockAccount(Connection connection, int account) throws SQLException{
    String s = "SELECT account_number FROM accounts WHERE account_number = ? FOR UPDATE";
   try( PreparedStatement pst = connection.prepareStatement(s); ){
    pst.setInt(1, account);
    pst.executeQuery();
   }

}
static void inserttransactions(PreparedStatement tps, int sender, int receiver, double amount, String status ) throws SQLException{
 tps.setInt(1, sender);
 tps.setInt(2, receiver);
 tps.setDouble(3, amount);
 tps.setString(4, status);
 tps.executeUpdate();
}

static void insertfailed(Connection connection, PreparedStatement txnPS, int sender, int receiver, double amount) throws SQLException{
    boolean previous_auto_commit = connection.getAutoCommit();
    connection.setAutoCommit(true);
    txnPS.setInt(1, sender);
    txnPS.setInt(2, receiver);
    txnPS.setDouble(3, amount);
    txnPS.setString(4, "FAILED");
    txnPS.executeUpdate();
    connection.setAutoCommit(previous_auto_commit);
}
}
