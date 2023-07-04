package yoon.test.jwtTest4.enums;

public enum Role {

    GUEST("ROLE_ANONYMOUS"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String role;

    Role(String role){
        this.role = role;
    }

}
