package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
@Table
public class RegionalArea2 {

    @Id
    @Column(unique = true, nullable = false, updatable = false, length = 36)
    private String uuid = UUID.randomUUID().toString();

    private String area2Name;

    private String area2Code;

    public String getUuid() {
        return uuid;
    }

    public String getArea2Name() {
        return area2Name;
    }

    public String getArea2Code() {
        return area2Code;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setArea2Name(String area2Name) {
        this.area2Name = area2Name;
    }

    public void setArea2Code(String area2Code) {
        this.area2Code = area2Code;
    }

}
