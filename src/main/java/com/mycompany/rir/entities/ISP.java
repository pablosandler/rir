package com.mycompany.rir.entities;

import com.google.common.base.Objects;
import org.springframework.util.Assert;

import javax.persistence.*;


@Entity
@Table(name = "isp")
public class ISP {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable=false)
    private final Long id;

    @Column(nullable=false,unique = true)
    private final String name;

    @Column(nullable=false)
    private final String website;

    @Column(nullable=false)
    private final String prefix;

    public ISP() {
        id=null;
        name=null;
        website=null;
        prefix=null;
    }

    public ISP(String name, String website, String prefix) {
        Assert.hasLength(name,"Name cannot be empty");
        Assert.hasLength(website,"Website cannot be empty");
        Assert.hasLength(prefix,"Prefix cannot be empty");

        this.id = null;
        this.name = name;
        this.website = website;
        this.prefix = prefix;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISP)) return false;
        ISP isp = (ISP) o;
        return Objects.equal(getId(), isp.getId()) &&
                Objects.equal(getName(), isp.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getName());
    }
}
