package com.mycompany.rir.rest.impl;

import com.mycompany.rir.dtos.ISPDto;
import com.mycompany.rir.dtos.ISPNames;
import com.mycompany.rir.entities.ISP;
import com.mycompany.rir.exceptions.RegistrationException;
import com.mycompany.rir.rest.ISPRegistrationServiceRestEndpoint;
import com.mycompany.rir.services.ISPService;
import net.ripe.commons.ip.Ipv4Range;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class ISPRegistrationServiceRestEndpointImpl implements ISPRegistrationServiceRestEndpoint {

    @Autowired
    private ISPService iSPService;

    public ISPNames getAll() {
        List<String> names = new ArrayList<String>();

        List<ISP> isps = iSPService.getISPs();
        for(ISP isp : isps){
            names.add(isp.getName());
        }

        return new ISPNames(names);
    }


    public ISPDto get(String companyName) {
        String escapedCompanyName = StringEscapeUtils.escapeHtml4(companyName.trim().replaceAll("\\s+", " "));

        ISP isp = iSPService.getISP(escapedCompanyName);
        if(isp==null){
            return null;
        }

        return buildDto(isp);
    }

    private ISPDto buildDto(ISP isp) {
        ISPDto ispDto = new ISPDto();
        ispDto.setName(isp.getName());
        ispDto.setWebsite(isp.getWebsite());
        ispDto.setPrefix(isp.getPrefix());
        Ipv4Range ipv4Range = Ipv4Range.parse(isp.getPrefix());
        ispDto.setTotalAddresses(ipv4Range.size());
        return ispDto;
    }

    public Response register(ISPDto ispDto) {
        try{
            String name = StringEscapeUtils.escapeHtml4(ispDto.getName().trim().replaceAll("\\s+", " "));
            String website =  StringEscapeUtils.escapeHtml4(ispDto.getWebsite().trim().replaceAll("\\s+", " "));
            iSPService.registerISP(name, website);
            return Response.status(Response.Status.OK).build();
        } catch(IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Name and website cannot be empty").build();
        }  catch(PersistenceException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("ISP already exist").build();
        } catch(RegistrationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Cannot register ISP. Please, try again later").build();
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Unknown exception").build();
        }
    }

}
