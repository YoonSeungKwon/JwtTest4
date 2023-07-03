package controller;

import enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import service.AccountService;
import vo.request.AccountDto;
import vo.response.AccountResponse;
import vo.response.Message;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/")
    public String index(){
        return "Main Page";
    }

    @PostMapping("/account/join")
    public ResponseEntity<Message> accountJoin(AccountDto dto){
        Message message = new Message();
        AccountResponse accountResponse = accountService.join(dto);

        if(accountResponse==null){
            return ResponseEntity.status(400).build();
        }
        message.setStatus(StatusEnum.OK);
        message.setMessage("Register Success");
        message.setData(accountResponse);
        return ResponseEntity.ok(message);
    }
}
