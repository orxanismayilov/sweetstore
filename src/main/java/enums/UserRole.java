package enums;

public enum UserRole {
    USER(1),
    ADMIN(2);

    private int code;

    UserRole(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
