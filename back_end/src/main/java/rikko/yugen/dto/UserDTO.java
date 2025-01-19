package rikko.yugen.dto;

import rikko.yugen.model.User;

public class UserDTO {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final String image;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.image = user.getImage();
    }

    //Getters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
