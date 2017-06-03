package com.mycompany.rir.services.impl;

import com.mycompany.rir.daos.ISPDao;
import com.mycompany.rir.exceptions.RegistrationException;
import com.mycompany.rir.services.IPAllocationService;
import com.mycompany.rir.services.ISPService;
import com.mycompany.rir.entities.ISP;
import com.mycompany.rir.exceptions.IPAllocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
@Transactional(noRollbackFor={NoResultException.class})
public class ISPServiceImpl implements ISPService {

    @Autowired
    private ISPDao ispDao;

    @Autowired
    private IPAllocationService ipAllocationService;

    public List<ISP> getISPs() {
        return ispDao.getAll();
    }

    public ISP registerISP(String name, String website) throws IllegalArgumentException, PersistenceException, RegistrationException {
        Assert.hasText(name, "Name cannot be empty");
        Assert.hasText(website, "Website cannot be empty");

        try {
            ISP isp = new ISP(name, website,ipAllocationService.getPrefix());
            return ispDao.save(isp);
        } catch (IPAllocationException e) {
            throw new RegistrationException(e.getMessage());
        }

    }

    public ISP getISP(String name) throws NoResultException{
        Assert.hasText(name, "Name cannot be empty");

        try{
            ISP isp = ispDao.getISPByName(name);
            return isp;
        } catch (NoResultException e){
            return null;
        }
    }

}
