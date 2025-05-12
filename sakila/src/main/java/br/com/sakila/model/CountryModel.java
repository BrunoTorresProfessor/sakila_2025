package br.com.sakila.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "country", schema = "sakila")
public class CountryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Short countryId;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    private Timestamp lastUpdate;

    // Getters e Setters

    public Short getCountryId() {
        return countryId;
    }

    public void setCountryId(Short countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
