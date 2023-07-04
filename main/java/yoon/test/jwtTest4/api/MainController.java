package yoon.test.jwtTest4.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import yoon.test.jwtTest4.enums.StatusEnum;
import yoon.test.jwtTest4.vo.response.Message;

import java.security.Principal;

@RestController
public class MainController {

    @GetMapping("/")
    public String main(){
        return "Main Page";
    }

    @GetMapping("/auth")
    public ResponseEntity<Message> authInfo(Principal principal){
        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("User Info");
        message.setData(principal);
        return ResponseEntity.ok(message);
    }
}
