package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;
import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class EmbeddedMethodsStrategyTest {

    @BeforeClass
    public static void initSessionFactory() {
        HibernateSessionFactory.Builder.configureWithoutHibernateCfgXml()
                .useNamingStrategy(StrategyOptions.builder().autodetectMaxLength().build())
                .annotatedClasses(User.class).createSessionFactory();
    }

    @AfterClass
    public static void closeSessionFactory() {
        HibernateSessionFactory.closeSessionFactory();
    }

    @Test
    public void testStrategy() {

    }

    @Entity
    public static class User {

        private String pid;

        private Name name;

        @Id
        @GeneratedValue
        public String getPid() {
            return pid;
        }

        @Embedded
        public Name getName() {
            return name;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setName(Name name) {
            this.name = name;
        }

    }

    @Embeddable
    public static class Name {

        private String firstName;

        private String lastName;

        @Column
        public String getFirstName() {
            return firstName;
        }

        @Column
        public String getLastName() {
            return lastName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    }

}
