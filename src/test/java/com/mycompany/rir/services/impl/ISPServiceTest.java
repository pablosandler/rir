package com.mycompany.rir.services.impl;

import com.mycompany.rir.daos.ISPDao;
import com.mycompany.rir.entities.ISP;
import com.mycompany.rir.exceptions.IPAllocationException;
import com.mycompany.rir.exceptions.RegistrationException;
import com.mycompany.rir.services.IPAllocationService;
import com.mycompany.rir.services.ISPService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class ISPServiceTest{

    public static final String COMPANY_NAME = "comanyName";
    public static final String WEBSITE = "website";
    public static final String NAME_CANNOT_BE_EMPTY = "Name cannot be empty";
    public static final String WEBSITE_CANNOT_BE_EMPTY = "Website cannot be empty";
    public static final String PREFIX = "128.0.0.0/8";
    public static final String CANNOT_ALLOCATE_IPS = "Cannot allocate IPs";

    private ISPService ispService;

    @Autowired
    private ISPDao ispDao;

    @Mock
    private IPAllocationService ipAllocationService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        ispService = new ISPServiceImpl();

        setField(ispService,"ipAllocationService",ipAllocationService);
        setField(ispService,"ispDao",ispDao);

        try {
            when(ipAllocationService.getPrefix()).thenReturn(PREFIX);
        } catch (IPAllocationException e) {
            fail();
        }
    }


    @Test
    public void testSaveISP(){
        try {
            ISP isp = ispService.registerISP(COMPANY_NAME, WEBSITE);
            assertNotNull(isp.getId());
            assertEquals(PREFIX, isp.getPrefix());
            assertEquals(COMPANY_NAME, isp.getName());
            assertEquals(WEBSITE, isp.getWebsite());
        } catch (RegistrationException e) {
            fail();
        }
    }

    @Test
    public void testSaveISPFailsBecauseCannotRetrievePrefix(){
        try {
            when(ipAllocationService.getPrefix()).thenThrow(new IPAllocationException(CANNOT_ALLOCATE_IPS));
        } catch (IPAllocationException e) {
            fail();
        }

        try {
            ispService.registerISP(COMPANY_NAME, WEBSITE);
        } catch (RegistrationException e) {
            assertNotNull(e);
            assertEquals(CANNOT_ALLOCATE_IPS, e.getMessage());
        }
    }


    @Test
    public void testSaveISPWithFirstValueNull(){
        try{
            ispService.registerISP(null, WEBSITE);
            fail();
        }catch(IllegalArgumentException e){
            assertNotNull(e);
            assertEquals(e.getMessage(), NAME_CANNOT_BE_EMPTY);
        } catch (RegistrationException e) {
            fail();
        }
    }


    @Test
    public void testSaveISPWithSecondValueNull(){
        try{
            ispService.registerISP(COMPANY_NAME, null);
            fail();
        }catch(IllegalArgumentException e){
            assertNotNull(e);
            assertEquals(e.getMessage(), WEBSITE_CANNOT_BE_EMPTY);
        }catch(RegistrationException e){
            fail();
        }
    }


    @Test
    public void testSaveISPWithFirstValueEmpty(){
        try{
            ispService.registerISP("", WEBSITE);
            fail();
        }catch(IllegalArgumentException e){
            assertNotNull(e);
            assertEquals(e.getMessage(), NAME_CANNOT_BE_EMPTY);
        }catch(RegistrationException e){
            fail();
        }
    }


    @Test
    public void testSaveISPWithSecondValueEmpty(){
        try{
            ispService.registerISP(COMPANY_NAME, "");
            fail();
        }catch(IllegalArgumentException e){
            assertNotNull(e);
            assertEquals(e.getMessage(), WEBSITE_CANNOT_BE_EMPTY);
        }catch(RegistrationException e){
            fail();
        }
    }


    @Test
    public void testSaveTwoISPsWithSameName(){
        ISP isp = null;
        try {
            isp = ispService.registerISP(COMPANY_NAME, WEBSITE);
        } catch (RegistrationException e) {
            fail();
        }
        assertNotNull(isp.getId());

        try {
            ispService.registerISP(COMPANY_NAME, WEBSITE + 2);
            fail();
        } catch(PersistenceException e){
            assertNotNull(e);
        }catch(RegistrationException e){
            e.printStackTrace();
        }

    }


    @Test
    public void testGetISPs() throws Exception {
        ISP isp = ispService.registerISP(COMPANY_NAME, WEBSITE);
        ISP isp2 = ispService.registerISP(COMPANY_NAME + 2, WEBSITE);

        List<ISP> isps = ispService.getISPs();
        assertTrue(isps.size()==2);
        assertTrue(isps.contains(isp));
        assertTrue(isps.contains(isp2));
    }


    @Test
    public void testGetISPByName(){
        ISP isp = null;
        try {
            isp = ispService.registerISP(COMPANY_NAME, WEBSITE);
        } catch (RegistrationException e) {
            fail();
        }
        assertNotNull(isp.getId());

        ISP ispRetrieved = ispService.getISP(COMPANY_NAME);
        assertEquals(isp, ispRetrieved);
    }


    @Test
    public void testGetISPByNameWithNonexistentValue(){
        ISP ispRetrieved = ispService.getISP(COMPANY_NAME);
        assertNull(ispRetrieved);
    }


    @Test
    public void testGetISPByNameWithNullValue(){
        try{
            ispService.getISP(null);
            fail();
        } catch(IllegalArgumentException e){
            assertNotNull(e);
            assertEquals(e.getMessage(), NAME_CANNOT_BE_EMPTY);
        }
    }


}