import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    // ==================== НАСТРОЙКИ ПОДКЛЮЧЕНИЯ ====================
    private static final String HOST = "192.168.1.141";
    private static final String PORT = "3306";
    private static final String DB_NAME = "magazin2";
    private static final String USER = "toor21";
    private static final String PASSWORD = "toor21";
    // ==============================================================

    private static final String SERVER_URL =
            "jdbc:mysql://" + HOST + ":" + PORT +
                    "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

    private static final String DB_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME +
                    "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

    // Одно соединение, живёт всё время работы программы
    private static Connection connection = null;

    // Получить (или переиспользовать) соединение
    public static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Подключение к MySQL установлено.");
        }
        return connection;
    }

    public static void createDatabaseIfNotExists() {
        String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME +
                " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("База данных '" + DB_NAME + "' готова.");
        } catch (SQLException e) {
            System.out.println("Не удалось создать базу: " + e.getMessage());
        }
    }

    public static void initDatabase() {
        createDatabaseIfNotExists();

        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "login VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(100) NOT NULL" +
                ");";

        String clientsTable = "CREATE TABLE IF NOT EXISTS clients (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "phone VARCHAR(30)," +
                "email VARCHAR(100)" +
                ");";

        String servicesTable = "CREATE TABLE IF NOT EXISTS services (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "price DECIMAL(10,2) NOT NULL" +
                ");";

        try (Statement stmt = connect().createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(clientsTable);
            stmt.execute(servicesTable);
            System.out.println("Таблицы готовы.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблиц: " + e.getMessage());
        }
    }

    // ---------- Пользователи ----------

    public static boolean userExists(String login) {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkPassword(String login, String password) {
        String sql = "SELECT password FROM users WHERE login = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(String login, String password) {
        String sql = "INSERT INTO users(login, password) VALUES(?, ?)";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Ошибка регистрации: " + e.getMessage());
            return false;
        }
    }

    // ---------- Клиенты ----------

    public static void addClient(String name, String phone, String email) {
        String sql = "INSERT INTO clients(name, phone, email) VALUES(?, ?, ?)";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getClients() {
        List<String[]> result = new ArrayList<>();
        String sql = "SELECT id, name, phone, email FROM clients";
        try (Statement stmt = connect().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---------- Услуги ----------

    public static void addService(String name, double price) {
        String sql = "INSERT INTO services(name, price) VALUES(?, ?)";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getServices() {
        List<String[]> result = new ArrayList<>();
        String sql = "SELECT id, name, price FROM services";
        try (Statement stmt = connect().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        String.valueOf(rs.getDouble("price"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Закрыть соединение при выходе из программы
    public static void close() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException ignored) {}
        }
    }
}