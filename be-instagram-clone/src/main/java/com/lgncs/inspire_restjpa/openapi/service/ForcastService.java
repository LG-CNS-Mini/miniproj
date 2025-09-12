package com.lgncs.inspire_restjpa.openapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgncs.inspire_restjpa.openapi.domain.ForcastResponseDTO;
import com.lgncs.inspire_restjpa.openapi.util.ForcastItems;


@Service
public class ForcastService {

    public List<ForcastResponseDTO> parsingJson(String data) {
        System.out.println("debug >>>> service parsingJson"); 
        ObjectMapper mapper = new ObjectMapper() ;
        List<ForcastResponseDTO> list = null ; 
        try{
            ForcastItems items = mapper.readValue(data, ForcastItems.class) ;
            list = items.getItems() ;
            System.out.println(">>>> service test result"); 
            list.stream()
                .forEach(System.out::println); 
                
        }catch(Exception e) {
            e.printStackTrace();
        }
        return list ;
    }
    
}
