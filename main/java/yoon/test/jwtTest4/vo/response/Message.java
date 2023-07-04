package yoon.test.jwtTest4.vo.response;

import lombok.Data;
import yoon.test.jwtTest4.enums.StatusEnum;

@Data
public class Message {

    private StatusEnum status;

    private String message;

    private Object data;

    public Message(){
        this.status = StatusEnum.BAD_REQUEST;
        this.message = null;
        this.data = null;
    }


}
