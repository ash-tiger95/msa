package com.inflearn.userservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                break;
            case 404:
                if(methodKey.contains("getOrders")){ // getOrders한해서만 작동
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), "User's orders is Empty");
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
