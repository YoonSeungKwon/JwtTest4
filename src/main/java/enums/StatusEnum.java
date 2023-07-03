package enums;

public enum StatusEnum {

    OK(1, 200),
    BAD_REQUEST(2,400),
    NOT_FOUND(3,404),
    SERVER_ERROR(4,500);

    private int code;
    private int status;

    StatusEnum(int code, int status){
        this.code = code;
        this.status=status;
    }

}
