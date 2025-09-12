package com.lgncs.inspire_restjpa.openapi.util;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgncs.inspire_restjpa.openapi.domain.ForcastResponseDTO;

import lombok.Data;

@Data
public class ForcastItems {

    @JsonProperty("item")
    private List<ForcastResponseDTO> items ; 

    @JsonCreator
    public ForcastItems(@JsonProperty("response") JsonNode node) {
        try{
            ObjectMapper mapper = new ObjectMapper() ;
            JsonNode item = node.findValue("item") ;
            items = Arrays.asList( 
                mapper.treeToValue(item, ForcastResponseDTO[].class)) ;
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
