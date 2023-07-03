package vo.response;

import enums.StatusEnum;
import lombok.Data;

@Data
public class Message {

    private StatusEnum status;

    private String message;

    private Object data;
    public Message(){
        this.data = null;
        this.message = null;
        this.status = StatusEnum.BAD_REQUEST;
    }

}
