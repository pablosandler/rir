package com.mycompany.rir.services.impl;

import com.mycompany.rir.dtos.RIRServiceResponse;
import com.mycompany.rir.rest.client.RIRRestClient;
import com.mycompany.rir.services.IPAllocationService;
import com.mycompany.rir.exceptions.IPAllocationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class IPAllocationServiceTest {

    @InjectMocks
    @Autowired
    private IPAllocationService ipAllocationService;

    @Mock
    private RIRRestClient rirRestClient;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPrefixSuccessfully(){
        RIRServiceResponse rirServiceResponse = new RIRServiceResponse();
        rirServiceResponse.setIpv4Allocation("128.0.0.0/8");

        try {
            when(rirRestClient.getIPV4Allocation()).thenReturn(rirServiceResponse);
        } catch (IOException e) {
            fail();
        }

        try {
            String prefix = ipAllocationService.getPrefix();
            assertEquals("128.0.0.0/8",prefix);
        } catch (IPAllocationException e) {
            fail();
        }
    }


    @Test
    public void testFailsWithException(){
        try {
            when(rirRestClient.getIPV4Allocation()).thenThrow(new IOException("error"));
        } catch (IOException e) {
            fail();
        }

        try {
            ipAllocationService.getPrefix();
            fail();
        } catch (IPAllocationException e) {
            assertNotNull(e);
            assertEquals("Cannot allocate IPs", e.getMessage());
        }
    }



}