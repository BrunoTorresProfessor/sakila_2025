package br.com.sakila.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "city", schema = "sakila")
public class CityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Short cityId;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "country_id", nullable = false)
    private Short countryId;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    private Timestamp lastUpdate;

    // Getters e Setters

    public Short getCityId() {
        return cityId;
    }

    public void setCityId(Short cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Short getCountryId() {
        return countryId;
    }

    public void setCountryId(Short countryId) {
        this.countryId = countryId;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}