package com.mycompany.rir.daos.impl;

import com.mycompany.rir.daos.ISPDao;
import com.mycompany.rir.daos.AbstractDao;
import com.mycompany.rir.entities.ISP;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
@Transactional(noRollbackFor={NoResultException.class})
public class ISPDaoImpl extends AbstractDao<ISP> implements ISPDao {

    public ISPDaoImpl() {
        super(ISP.class);
    }


    public ISP getISPByName(String name) throws NoResultException {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<ISP> ispRoot = cq.from(ISP.class);

        cq.select(ispRoot);
        cq.where(getEntityManager().getCriteriaBuilder().equal(ispRoot.get("name"), name));

        ISP isp = (ISP) getEntityManager().createQuery(cq).getSingleResult();

        return isp;
    }
}
