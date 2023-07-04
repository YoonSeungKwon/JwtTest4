package yoon.test.jwtTest4.enums;

public enum StatusEnum {

    OK("200"),
    BAD_REQUEST("400"),
    NOT_FOUND("404"),
    SERVER_ERROR("500");

    private String code;

    StatusEnum(String code){
        this.code = code;
    }
}
