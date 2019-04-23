package sample.enums;

public enum UserRole {
    ADMIN(1),
    USER(2);

    private int code;

    UserRole(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
