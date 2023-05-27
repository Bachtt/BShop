package com.bachtt.bshop.models;

import javax.persistence.*;

@Entity
@Table(name = "category", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "nameCategory"
        })
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameCategory;
    @ManyToOne
    User user;

    public Category() {
    }

    public Category(Long id, String nameCategory, String avatarCategory, User user) {
        this.id = id;
        this.nameCategory = nameCategory;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
