package com.mycompany.rir.daos;

import com.mycompany.rir.entities.ISP;

public interface ISPDao extends EditableBaseDao<ISP> {

    ISP getISPByName(String name);

}
