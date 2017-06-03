package com.mycompany.rir.rest.impl;

import com.mycompany.rir.dtos.ISPDto;
import com.mycompany.rir.dtos.ISPNames;
import com.mycompany.rir.entities.ISP;
import com.mycompany.rir.rest.ISPRegistrationServiceRestEndpoint;
import com.mycompany.rir.services.ISPService;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class ISPRegistrationServiceRestEndpointTest {

    private final static String ENDPOINT_ADDRESS = "http://localhost:8081";
    public static final String NAME = "name";
    public static final String WEBSITE = "website";
    public static final String PREFIX = "128.0.0.0/24";
    private final String PATH = "ispRegistrationService/";

    @Mock
    private ISPService iSPService;

    @InjectMocks
    @Autowired
    private ISPRegistrationServiceRestEndpoint iSPRegistrationServiceRestEndpoint;

    private static boolean runInit = true;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        if(!runInit){
            return;
        }

        runInit=false;
        startServer();
    }


    private void startServer(){
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setServiceBean(iSPRegistrationServiceRestEndpoint);

        List<Object> providerList = new ArrayList<Object>();
        providerList.add(new org.apache.cxf.jaxrs.provider.json.JSONProvider());
        sf.setProviders(providerList);

        sf.setAddress(ENDPOINT_ADDRESS);
        sf.create();
    }


    @Test
    public void testGetNamesList() throws Exception {
        List<ISP> isps = new ArrayList<ISP>();
        isps.add(new ISP(NAME, WEBSITE, PREFIX));
        isps.add(new ISP(NAME+2, WEBSITE+2,PREFIX+2));

        when(iSPService.getISPs()).thenReturn(isps);

        WebClient client = WebClient.create(ENDPOINT_ADDRESS);
        client.accept(MediaType.APPLICATION_JSON);
        client.path(PATH + "getAllISPs/");

        ISPNames names = client.get(ISPNames.class);
        assertNotNull(names);
        assertTrue(names.getNames().size() == 2);
        assertTrue(names.getNames().contains(NAME));
        assertTrue(names.getNames().contains(NAME+2));
    }



    @Test
    public void testGet() throws Exception {
        ISP isp  = new ISP(NAME, WEBSITE, PREFIX);

        when(iSPService.getISP(NAME)).thenReturn(isp);

        WebClient client = WebClient.create(ENDPOINT_ADDRESS);
        client.accept(MediaType.APPLICATION_JSON);

        client.path(PATH + "get/" + NAME);

        ISPDto ispRetrieved = client.get(ISPDto.class);
        assertEquals(NAME, ispRetrieved.getName());
        assertEquals(WEBSITE, ispRetrieved.getWebsite());
        assertEquals(PREFIX, ispRetrieved.getPrefix());
        assertEquals(256, ispRetrieved.getTotalAddresses());
    }


    @Test
    public void testGetNonexistentISP() throws Exception {
        when(iSPService.getISP(NAME)).thenReturn(null);

        WebClient client = WebClient.create(ENDPOINT_ADDRESS);
        client.accept(MediaType.APPLICATION_JSON);

        client.path(PATH + "get/" + NAME);

        ISPDto ispRetrieved = client.get(ISPDto.class);
        assertNull(ispRetrieved);
    }


    @Test
    public void testGetEscapesCharacters() throws Exception {
        when(iSPService.getISP(NAME)).thenReturn(null);

        WebClient client = WebClient.create(ENDPOINT_ADDRESS);
        client.accept(MediaType.APPLICATION_JSON);

        client.path(PATH + "get/" + "<mytag1>=1");

        ISPDto ispRetrieved = client.get(ISPDto.class);
        assertNull(ispRetrieved);
    }


    @Test
    public void testRegisterNewISPSuccessfully() throws Exception {
        ISP isp = new ISP(NAME, WEBSITE, PREFIX);
        when(iSPService.registerISP(NAME, WEBSITE)).thenReturn(isp);

        WebClient client = WebClient.create(ENDPOINT_ADDRESS);
        client.accept(MediaType.APPLICATION_JSON);
        client.type(MediaType.APPLICATION_JSON);

        client.path(PATH + "register/");

        ISPDto ispDto = new ISPDto();
        ispDto.setName(isp.getName());
        ispDto.setWebsite(isp.getWebsite());

        Response r = client.post(ispDto, Response.class);
        Assert.assertTrue(r.getStatus()==200);
    }


    @Test
    public void testRegisterNewISPFails() throws Exception {
        ISP isp = new ISP(NAME, WEBSITE, PREFIX);
        when(iSPService.registerISP(NAME, WEBSITE)).thenThrow(new PersistenceException("error"));

        WebClient client = WebClient.create(ENDPOINT_ADDRESS);
        client.accept(MediaType.APPLICATION_JSON);
        client.type(MediaType.APPLICATION_JSON);

        client.path(PATH + "register/");

        ISPDto ispDto = new ISPDto();
        ispDto.setName(isp.getName());
        ispDto.setWebsite(isp.getWebsite());

        Response r = client.post(ispDto, Response.class);
        Assert.assertTrue(r.getStatus()==400);
    }


}