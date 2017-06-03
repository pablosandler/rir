package com.mycompany.rir.services;

import com.mycompany.rir.entities.ISP;
import com.mycompany.rir.exceptions.RegistrationException;

import javax.persistence.PersistenceException;
import java.util.List;

public interface ISPService {

    List<ISP> getISPs();

    ISP registerISP(String name, String website)  throws IllegalArgumentException, PersistenceException, RegistrationException;

    ISP getISP(String name);

}
