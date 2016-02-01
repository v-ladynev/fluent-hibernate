package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//@Entity
@Table
public class RegionalCountry {

    @Id
    @Column(unique = true, nullable = false, updatable = false, length = 36)
    private final String uuid = UUID.randomUUID().toString();

    private String countryName;

    private String countryCode;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<RegionalArea1> regionalArea1;

    public String getUuid() {
        return uuid;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Set<RegionalArea1> getRegionalArea1() {
        return regionalArea1;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setRegionalArea1(Set<RegionalArea1> regionalArea1) {
        this.regionalArea1 = regionalArea1;
    }

}
