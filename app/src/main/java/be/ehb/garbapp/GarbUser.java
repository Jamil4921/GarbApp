package be.ehb.garbapp;

public class GarbUser {
    public String name, email, profilePictureUrl;

    public GarbUser() {
    }

    public GarbUser(String name, String email, String profilePictureUrl) {
        this.name = name;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
