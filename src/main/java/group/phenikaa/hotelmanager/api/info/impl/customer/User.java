package group.phenikaa.hotelmanager.api.info.impl.customer;

public record User(String username, String password) {

    @Override
    public String toString() {
        return "Username: " + username + ", Password: " + password;
    }
}