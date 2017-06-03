package com.mycompany.rir.rest.impl;

import com.mycompany.rir.dtos.RIRServiceResponse;
import com.mycompany.rir.rest.client.RIRRestClient;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class RIRClientTest {

    @Autowired
    private RIRRestClient rirRestClient;

    @Ignore
    @Test
    public void testConnection(){
        try {
            RIRServiceResponse rirServiceResponse = rirRestClient.getIPV4Allocation();
            assertNotNull(rirServiceResponse);
            assertNotNull(rirServiceResponse.getIpv4Allocation());
        } catch (IOException e) {
            fail();
        }
    }

}
