package be.ehb.garbapp;

public class GarbUser {
    public String name, email, profilePictureUrl;
    public int role;

    public GarbUser() {
    }

    public GarbUser(String name, String email, String profilePictureUrl, int role) {
        this.name = name;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
