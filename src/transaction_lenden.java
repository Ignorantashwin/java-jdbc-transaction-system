import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class transaction_lenden {

    private static final String URL = "jdbc:mysql://localhost:3306/lenden";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {

        String debitSql =
                "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        String creditSql =
                "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

        try (
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Scanner sc = new Scanner(System.in);
            PreparedStatement debitPs = connection.prepareStatement(debitSql);
            PreparedStatement creditPs = connection.prepareStatement(creditSql)
        ) {

            connection.setAutoCommit(false);

            System.out.print("Enter Sender Account Number: ");
            int sender = sc.nextInt();

            if (!isAvailable(connection, sender)) {
                System.out.println("Sender account does not exist");
                return;
            }

            System.out.print("Enter Amount: ");
            double amount = sc.nextDouble();

            System.out.print("Enter Receiver Account Number: ");
            int receiver = sc.nextInt();

            if (!isAvailable(connection, receiver)) {
                System.out.println("Receiver account does not exist");
                return;
            }

            /* 🔒 CONSISTENT LOCK ORDER (AVOID DEADLOCK) */
            int firstLock = Math.min(sender, receiver);
            int secondLock = Math.max(sender, receiver);

            lockAccount(connection, firstLock);
            lockAccount(connection, secondLock);

            if (!hasSufficientBalance(connection, sender, amount)) {
                connection.rollback();
                System.out.println("Transaction failed: Insufficient balance");
                return;
            }

            debitPs.setDouble(1, amount);
            debitPs.setInt(2, sender);

            creditPs.setDouble(1, amount);
            creditPs.setInt(2, receiver);

            int debitRows = debitPs.executeUpdate();
            int creditRows = creditPs.executeUpdate();

            if (debitRows == 1 && creditRows == 1) {
                connection.commit();
                System.out.println("Transaction successful");
            } else {
                connection.rollback();
                System.out.println("Transaction failed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ✅ CHECK ACCOUNT EXISTS (NO LOCK) */
    static boolean isAvailable(Connection connection, int account) throws SQLException {
        String sql = "SELECT 1 FROM accounts WHERE account_number = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, account);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /* 🔒 LOCK ACCOUNT ROW */
    static void lockAccount(Connection connection, int account) throws SQLException {
        String sql =
                "SELECT account_number FROM accounts WHERE account_number = ? FOR UPDATE";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, account);
            ps.executeQuery();
        }
    }

    /* 🔒 CHECK BALANCE AFTER LOCK */
    static boolean hasSufficientBalance(Connection connection,
                                        int account,
                                        double amount) throws SQLException {
        String sql =
                "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, account);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance") >= amount;
            }
            return false;
        }
    }
}