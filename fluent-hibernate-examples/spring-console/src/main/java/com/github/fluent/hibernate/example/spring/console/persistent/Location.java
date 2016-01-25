package com.github.fluent.hibernate.example.spring.console.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author V.Ladynev
 */
@Embeddable
public class Location {

    private String city;

    private String country;

    @Column
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
