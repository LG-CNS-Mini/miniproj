package com.lgncs.inspire_restjpa.openapi.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgncs.inspire_restjpa.openapi.domain.ForcastRequestDTO;
import com.lgncs.inspire_restjpa.openapi.domain.ForcastResponseDTO;
import com.lgncs.inspire_restjpa.openapi.service.ForcastService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v2/inspire/forcast")
public class ForcastCtrl {
    
    @Value("${openApi.serviceKey}")
    private String serviceKey ; 
    @Value("${openApi.callBackUrl}")
    private String callBackUrl ; 
    @Value("${openApi.dataType}")
    private String dataType ; 

    @Autowired
    private ForcastService service ;

    @GetMapping("/getData")
    public ResponseEntity<List<ForcastResponseDTO>> getData(ForcastRequestDTO params) {
        System.out.println("debug >>>> end point : /forcast/getData");
        System.out.println("debug >>>> service key : "+serviceKey);
        System.out.println("debug >>>> callBackUrl : "+callBackUrl);
        System.out.println("debug >>>> data type : "+dataType);
        System.out.println("debug >>>> params : "+params);

        // 콜백 URL에 요청 파라미터를 보내는 것 
        String requestURL = callBackUrl+
                            "?serviceKey="+serviceKey+
                            "&beach_num="+params.getBeach_num()+
                            "&base_date="+params.getBase_date()+
                            "&base_time="+params.getBase_time()+
                            "&dataType="+dataType ;
        System.out.println("debug >>> url check : "+requestURL ); 
        //////////////
        HttpURLConnection http   = null ; 
        InputStream       stream = null ; 
        String            result = null ;

        List<ForcastResponseDTO>  list  = null ;
        try {
            URL url = new URL(requestURL);
            http = (HttpURLConnection)url.openConnection();
            System.out.println("http connection = " + http ); 
            int code = http.getResponseCode() ; 
            System.out.println("http response code  = " + code );  
            if( code == 200 ) {
                stream = http.getInputStream() ; 
                result = readString(stream) ;
                System.out.println("result = " + result); 

                // 서비스 구현 
                list = service.parsingJson(result); 

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        ///////////////
        return ResponseEntity.ok().body(list) ;  
    }

    public String readString(InputStream stream) throws IOException {
        BufferedReader br = 
            new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String input = null  ;
        StringBuilder result = new StringBuilder();
        while( (input = br.readLine() ) != null ) {
            result.append(input+"\n\r");
        }
        br.close();   
        return result.toString() ; 
    }
    
}
