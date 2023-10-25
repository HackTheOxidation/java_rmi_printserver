package printserver.server;

public class Login {
    private final String Username;
    private final byte[] PasswordHash;
    private final String Salt;
    private final boolean Authenticated;
    public Login(String username, byte[] passwordHash, String salt, boolean authenticated) {
        Username = username;
        PasswordHash = passwordHash;
        Salt = salt;
        Authenticated = authenticated;
    }

    public Login(String username, byte[] passwordHash, String salt) {
        Username = username;
        PasswordHash = passwordHash;
        Salt = salt;
        Authenticated = false;
    }

    public Login(String username) {
        Username = username;
        PasswordHash = null;
        Salt = null;
        Authenticated = false;
    }

    public String getUsername() {
        return Username;
    }

    public byte[] getPasswordHash() {
        return PasswordHash;
    }

    public String getSalt() {
        return Salt;
    }

    public boolean isAuthenticated() {
        return Authenticated;
    }
}
