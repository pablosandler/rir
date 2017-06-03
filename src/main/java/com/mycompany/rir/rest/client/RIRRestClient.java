package com.mycompany.rir.rest.client;


import com.mycompany.rir.dtos.RIRServiceResponse;

import java.io.IOException;

public interface RIRRestClient {

    RIRServiceResponse getIPV4Allocation() throws IOException;

}
