package br.com.sakila.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "category", schema = "sakila")
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Byte categoryId;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    private Timestamp lastUpdate;

    // Getters e Setters

    public Byte getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Byte categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
