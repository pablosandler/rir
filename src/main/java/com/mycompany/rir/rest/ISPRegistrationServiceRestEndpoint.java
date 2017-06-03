package com.mycompany.rir.rest;

import com.mycompany.rir.dtos.ISPDto;
import com.mycompany.rir.dtos.ISPNames;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ispRegistrationService")
public interface ISPRegistrationServiceRestEndpoint {

    @GET
    @Path("/getAllISPs")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    ISPNames getAll();

    @GET
    @Path("/get/{companyName}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    ISPDto get(@PathParam("companyName")String companyName);


    @POST
    @Path("/register")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    Response register(final ISPDto ispDto);

}
