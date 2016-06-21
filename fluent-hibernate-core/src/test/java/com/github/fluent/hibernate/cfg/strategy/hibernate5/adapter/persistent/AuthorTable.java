package com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter.persistent;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "unique_field" }),
        indexes = @Index(columnList = "author_version"))
public class AuthorTable {

    @Id
    private Long authorPid;

    @Embedded
    private AuthorInfo authorInfo;

    @ManyToMany
    private List<Book> booksOne;

    @ManyToMany
    private List<Book> booksTwo;

    @OneToMany
    @JoinColumn
    private List<Book> booksThree;

    @OneToMany
    @OrderColumn
    private List<Book> booksOrdered;

    @ElementCollection
    @MapKeyColumn
    private Map<String, String> booksMap;

    @ElementCollection
    private List<String> bookTitles;

    @Enumerated
    private AuthorType authorType;

    @Version
    private Integer authorVersion;

    @ElementCollection
    @Embedded
    private List<AuthorInfo> elementCollectionAuthorInfo;

    @Column
    private String uniqueField;

}
