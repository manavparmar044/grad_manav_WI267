import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
 
// Layout Maintenance Application
// ---------------------------------------
// Types -  Villa, Apartment, Independent House, Open Site
// Total Sites - 35 Sites
// First 10 sites are of 40x60 ft size
// Next 10 sites are of 30x50 ft size
// Last 15 sites are of 30x40 ft size
// Open sites are charged 6Rs/sqft
// Occupied sites are charged 9Rs./sqft
 
// Admin    -
//  Can add/edit/remove the owner details and site details
//  Can collect the maintenance and update
//  Can see the pending details of all sites or the specific site
//  Can approve/reject the site owners update about their own sites
// Site Owner -
//  Can only see/update the details of his/her own site (but should be approved by Admin)
 
public class LayoutMaintainanceApp {
 
    static Scanner sc = new Scanner(System.in);
 
    // ================= DB CONNECTION =================
    static Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/LayoutMaintenance",
                "postgres",
                "MANAV@WISSEN");
    }
 
    // ================= LOGIN =================
    static String login(int userId) {
 
        String role = null;
        String sql = "SELECT role FROM users WHERE user_id = ?";
 
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                role = rs.getString("role");
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }
 
    // ================= OWNER: ADD PAYMENT =================
    static void ownerAddPayment() {
 
        System.out.print("Enter Maintenance ID: ");
        int mid = sc.nextInt();
 
        System.out.print("Enter Amount: ");
        int amt = sc.nextInt();
 
        String sql = """
                    INSERT INTO payments (maintenance_id, paid_amount, payment_status)
                    VALUES (?, ?, 'PARTIALLY_PAID')
                """;
 
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, mid);
            ps.setInt(2, amt);
            ps.executeUpdate();
 
            System.out.println("Payment added (Pending Admin Approval)");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // ================= ADMIN: APPROVE PAYMENT =================
    static void approvePayment() {
 
        System.out.print("Enter Payment ID to approve: ");
        int pid = sc.nextInt();
 
        String sql = """
                    UPDATE payments
                    SET approved_by_admin = TRUE
                    WHERE payment_id = ?
                """;
 
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setInt(1, pid);
            ps.executeUpdate();
 
            System.out.println("Payment Approved");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // ================= ADMIN: VIEW PENDING MAINTENANCE =================
    static void viewPendingMaintenance() {
 
        String sql = """
                    SELECT s.site_number, m.month, m.year,
                           m.total_amount, m.payment_status
                    FROM maintenance m
                    JOIN sites s ON m.site_id = s.site_id
                    WHERE m.payment_status != 'PAID'
                """;
 
        try (Connection con = getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
 
            System.out.println("\n--- Pending Maintenance ---");
            while (rs.next()) {
                System.out.println(
                        "Site: " + rs.getInt("site_number") +
                                ", Amount: " + rs.getInt("total_amount") +
                                ", Status: " + rs.getString("payment_status"));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // ================= ADMIN MENU =================
    static void adminMenu() {
 
        while (true) {
            System.out.println("""
                        \n===== ADMIN MENU =====
                        1. View Pending Maintenance
                        2. Approve Payment
                        3. Logout
                    """);
 
            int ch = sc.nextInt();
 
            switch (ch) {
                case 1 -> viewPendingMaintenance();
                case 2 -> approvePayment();
                case 3 -> {
                    System.out.println("Admin logged out");
                    return;
                }
            }
        }
    }
 
    // ================= OWNER MENU =================
    static void ownerMenu() {
 
        while (true) {
            System.out.println("""
                        \n===== OWNER MENU =====
                        1. Make Payment
                        2. Logout
                    """);
 
            int ch = sc.nextInt();
 
            switch (ch) {
                case 1 -> ownerAddPayment();
                case 2 -> {
                    System.out.println("Owner logged out");
                    return;
                }
            }
        }
    }
 
    // ================= MAIN =================
    public static void main(String[] args) {
 
        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter choice: ");
 
            int choice = sc.nextInt();
 
            if (choice == 2) {
                System.out.println("Program terminated. Goodbye!");
                break;
            }
 
            if (choice != 1) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }
 
            // LOGIN FLOW
            System.out.print("Enter User ID to login: ");
            int userId = sc.nextInt();
 
            String role = login(userId);
 
            if (role == null) {
                System.out.println("Invalid User");
                continue;
            }
 
            System.out.println("Logged in as: " + role);
 
            if (role.equals("ADMIN")) {
                adminMenu();
            } else {
                ownerMenu();
            }
 
            System.out.println("Logged out successfully.");
        }
 
        sc.close();
    }
 
}