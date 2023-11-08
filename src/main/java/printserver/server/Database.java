package printserver.server;

import javax.security.auth.login.AccountNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String URL = "jdbc:postgresql://ella.db.elephantsql.com:5432/efiespng";
    private final String USER = "efiespng";
    private final String PASSWORD = "xPT519NpOeC6Y14XZp6vYPoO_IeusQVR";

    public void createUser(Login login) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = con.prepareStatement(
                    "INSERT INTO public.\"Logins\"(\n" +
                    "\t\"Username\", \"PasswordHash\", \"Salt\")\n" +
                    "\tVALUES (?, ?, ?) ON CONFLICT DO NOTHING;");
            statement.setString(1, login.getUsername());
            statement.setBytes(2, login.getPasswordHash());
            statement.setString(3, login.getSalt());
            statement.execute();
        }
    }

    public void changePassword(Login login) throws AccountNotFoundException, ClassNotFoundException {
        if(login.isAuthenticated()){
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
                PreparedStatement statement = con.prepareStatement(
                        "UPDATE public.\"Logins\"\n" +
                                "\tSET \"PasswordHash\"=?, \"Salt\"=?\n" +
                                "\tWHERE \"Username\"=?;");
                statement.setBytes(1, login.getPasswordHash());
                statement.setString(2, login.getSalt());
                statement.setString(3, login.getUsername());
                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new AccountNotFoundException();
        }
    }

    public Login getUser(Login login) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = con.prepareStatement(
                    "SELECT \"Username\", \"PasswordHash\", \"Salt\"\n" +
                            "\tFROM public.\"Logins\"\n" +
                            "\tWHERE \"Username\"=?;");
            statement.setString(1, login.getUsername());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return new Login(resultSet.getString("Username"),
                        resultSet.getBytes("PasswordHash"),
                        resultSet.getString("Salt"),
                        false);
            }
        } catch (SQLException e) {
            return login;
        }
    }

    public void createRole(String role) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = con.prepareStatement(
                    "INSERT INTO public.\"Roles\"(\n" +
                            "\t\"Role\")\n" +
                            "\tVALUES (?) ON CONFLICT DO NOTHING;");
            statement.setString(1, role);
            statement.execute();
        }
    }

    public void assignRoleToUse(String username, String role) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = con.prepareStatement(
                    "INSERT INTO public.\"UserRoles\"(\n" +
                            "\t\"Username\", \"Role\")\n" +
                            "\tVALUES (?, ?) ON CONFLICT DO NOTHING;");
            statement.setString(1, username);
            statement.setString(2, role);
            statement.execute();
        }
    }

    public String getRoleForUser(String username) throws ClassNotFoundException, SQLException
    {
        Class.forName("org.postgresql.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = con.prepareStatement(
                    "SELECT \"Role\"\n" +
                            "\tFROM public.\"UserRoles\"\n" +
                            "\tWHERE \"Username\" = ?;");
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getString("Role");
            }
        }
    }
    public List<String> getPrivilegesForRole(String role) throws ClassNotFoundException, SQLException
    {
        Class.forName("org.postgresql.Driver");
        List<String> privileges = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = con.prepareStatement(
                    "SELECT \"Privilege\"\n" +
                            "\tFROM public.\"RolePrivileges\"\n" +
                            "\tWHERE \"Role\" = ?;");
            statement.setString(1, role);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                {
                    privileges.add(resultSet.getString("Privilege"));
                }
            }
        }
        return privileges;
    }
}