package be.ehb.garbapp;

public class User {
    public String firstName, lastName, email, profilePicture;

    public User() {
    }

    public User(String firstName, String lastName, String email, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
    }
}
