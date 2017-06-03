package com.mycompany.rir.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ISPNames implements Serializable {

    @XmlElement
    private List<String> names;

    public ISPNames(){
        names = new ArrayList<String>();
    }

    public ISPNames(List<String> names){
        this.names = new ArrayList<String>(names);
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = new ArrayList<String>(names);
    }

}
