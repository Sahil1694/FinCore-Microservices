package com.skillcanvas.message.functions;


import com.skillcanvas.message.dto.AccountsMsgDto;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;


@Configuration
public class MessageFunctions {

    private static final Logger log = Logger.getLogger(MessageFunctions.class.getName());

    @Bean
    public Function<AccountsMsgDto , AccountsMsgDto> email(){
        return  accountsMsgDto -> {
               log.info("Sending email to : " + accountsMsgDto.email());
                return accountsMsgDto;
        };
    }
    @Bean
    public Function<AccountsMsgDto,Long> sms() {
        return accountsMsgDto -> {
            log.info("Sending sms with the details : " +  accountsMsgDto.toString());
            return accountsMsgDto.accountNumber();
        };
    }


}