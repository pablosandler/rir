package com.mycompany.rir.services.impl;

import com.mycompany.rir.dtos.RIRServiceResponse;
import com.mycompany.rir.rest.client.RIRRestClient;
import com.mycompany.rir.services.IPAllocationService;
import com.mycompany.rir.exceptions.IPAllocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IPAllocationServiceImpl implements IPAllocationService {

    @Autowired
    private RIRRestClient rirRestClient;

    public String getPrefix() throws IPAllocationException {
        try {
            RIRServiceResponse response = rirRestClient.getIPV4Allocation();
            return response.getIpv4Allocation();
        } catch (Exception e) {
            throw new IPAllocationException("Cannot allocate IPs");
        }
    }



}
