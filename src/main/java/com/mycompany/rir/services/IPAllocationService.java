package com.mycompany.rir.services;

import com.mycompany.rir.exceptions.IPAllocationException;

public interface IPAllocationService {

    public String getPrefix() throws IPAllocationException;

}
