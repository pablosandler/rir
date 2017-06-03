package com.mycompany.rir.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RIRServiceResponse {

    @JsonProperty("ipv4_allocation")
    String ipv4Allocation;

    public String getIpv4Allocation() {
        return ipv4Allocation;
    }

    public void setIpv4Allocation(String ipv4Allocation) {
        this.ipv4Allocation = ipv4Allocation;
    }
}
