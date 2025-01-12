package com.store.api.service;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.util.ConfirmationCode;
import com.store.api.repository.ConfirmationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ConfirmationCodeService {
    private static final int MIN_VALUE =1000;
    private static final int MAX_VALUE =9999;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    private ConfirmationCodeService(ConfirmationCodeRepository confirmationCodeRepository){
        this.confirmationCodeRepository = confirmationCodeRepository;
    }

    public static String generateCode(){
       return String.valueOf((long)(Math.floor(Math.random()*(MAX_VALUE-MIN_VALUE+1)+MIN_VALUE)));
    }


  public boolean save(String telephone){
     Optional< ConfirmationCode> savedCode =confirmationCodeRepository.findByTelephone(telephone);
      if(savedCode.isPresent()){
          savedCode.get().setCode(generateCode());
          confirmationCodeRepository.save(savedCode.get());
          return true;
      }
          ConfirmationCode confirmationCode = ConfirmationCode.builder()
                  .telephone(telephone)
                  .code(generateCode())
                  .expireDate(Instant.now().plus(30, ChronoUnit.MINUTES))
                  .build();
          confirmationCodeRepository.save(confirmationCode);
          return true;
  }

  public ConfirmationCode getByCode(String code){
     return   confirmationCodeRepository.findByCode(code)
             .orElseThrow(()->new InvalidOperationException("Failed"));
  }

    public ConfirmationCode getByTelephone(String telephone){
        return   confirmationCodeRepository.findByTelephone(telephone)
                .orElseThrow(()->new InvalidOperationException("Failed"));
    }

    public void delete(String telephone){
       ConfirmationCode confirmationCode= confirmationCodeRepository.findByTelephone(telephone)
               .orElseThrow(()->new InvalidOperationException(""));
       confirmationCodeRepository.delete(confirmationCode);
    }

}


