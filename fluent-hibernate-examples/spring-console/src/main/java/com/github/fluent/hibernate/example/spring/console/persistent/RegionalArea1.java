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
public class RegionalArea1 {

    @Id
    @Column(unique = true, nullable = false, updatable = false, length = 36)
    private String uuid = UUID.randomUUID().toString();

    private String area1Name;

    private String area1Code;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<RegionalArea2> regionalArea2;

    public String getUuid() {
        return uuid;
    }

    public String getArea1Name() {
        return area1Name;
    }

    public String getArea1Code() {
        return area1Code;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Set<RegionalArea2> getRegionalArea2() {
        return regionalArea2;
    }

    public void setArea1Name(String area1Name) {
        this.area1Name = area1Name;
    }

    public void setArea1Code(String area1Code) {
        this.area1Code = area1Code;
    }

    public void setRegionalArea2(Set<RegionalArea2> regionalArea2) {
        this.regionalArea2 = regionalArea2;
    }

}
