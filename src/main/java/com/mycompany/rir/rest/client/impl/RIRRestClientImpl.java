package com.mycompany.rir.rest.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.rir.dtos.RIRServiceResponse;
import com.mycompany.rir.rest.client.RIRRestClient;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Component
public class RIRRestClientImpl implements RIRRestClient {

    @Value("${rir.endpoint.address}")
    private String ENDPOINT_ADDRESS;

    @Value("${rir.endpoint.path}")
    private String PATH;


    public RIRServiceResponse getIPV4Allocation() throws IOException {
        String jsonResponse = getWebClient().get(String.class);
        ObjectMapper mapper = new ObjectMapper();
        RIRServiceResponse rIRServiceResponse = mapper.readValue(jsonResponse, RIRServiceResponse.class);

        return rIRServiceResponse;
    }

    private WebClient getWebClient(){
        WebClient webClient = WebClient.create(ENDPOINT_ADDRESS);
        webClient.accept(MediaType.APPLICATION_JSON);
        webClient.path(PATH);
        return webClient;
    }

}
