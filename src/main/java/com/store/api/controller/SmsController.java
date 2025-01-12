package com.store.api.controller;

import com.store.api.exceptions.UserNotFoundException;
import com.store.api.model.user.AppUser;
import com.store.api.model.user.UserTelephoneDto;
import com.store.api.repository.UserRepository;
import com.store.api.service.ConfirmationCodeService;
import com.store.api.model.util.ConfirmationCode;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class SmsController {

    private final ConfirmationCodeService confirmationCodeService;
    private final UserRepository userRepository;
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    public SmsController(ConfirmationCodeService confirmationCodeService,UserRepository userRepository){
        this.confirmationCodeService = confirmationCodeService;
        this.userRepository = userRepository;
    }

    @GetMapping("/sendSMS")
    public ResponseEntity<String> sendSMS(String telephone){
        String code = confirmationCodeService.getByTelephone(telephone).getCode();
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
        Message.creator(new PhoneNumber("+48"+telephone),
                new PhoneNumber("+19549069402"), "Your verification code is "+code).create();
        return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }


    private ConfirmationCode getCode(String code) {
       return confirmationCodeService.getByCode(code);
    }

    @PatchMapping("/add-number/{id}")
    public ResponseEntity<String> addTelephoneNumber(@RequestParam Long id,@Valid @RequestBody UserTelephoneDto userTelephoneDto){
        try{
            boolean successful= confirmationCodeService.save(userTelephoneDto.getTelephone());
            if(successful){
                return sendSMS(userTelephoneDto.getTelephone());
            }
            return ResponseEntity.badRequest().body("Error performing operation");
        }catch (UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth-code")
    public ResponseEntity<String> authCode(Long id,String code){
        ConfirmationCode dbCode = getCode(code);
        if(dbCode.getCode().equals(code)){
            String retValue= dbCode.getTelephone();
           AppUser user= userRepository.findById(id).orElse(null);
           if(user != null){
               user.setTelephone(dbCode.getTelephone());
               userRepository.save(user);
           }
            confirmationCodeService.delete(dbCode.getTelephone());
            return ResponseEntity.ok().body(retValue);
        }
        return ResponseEntity.badRequest().body(null);
    }

}
