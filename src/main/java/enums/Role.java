package enums;

public enum Role {

    ROLE_ANONYMOUS(1, "GUEST"),
    ROLE_USER(2, "USER"),
    ROLE_ADMIN(3, "ADMIN");

    private int code;
    private String role;

    Role(int code, String role){
        this.code = code;
        this.role = role;
    }

}
