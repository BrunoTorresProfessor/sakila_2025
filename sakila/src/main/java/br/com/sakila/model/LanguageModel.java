package br.com.sakila.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "language" , schema = "sakila")
public class LanguageModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "language_id")
	private Long languageId;	
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "last_update")
	private Timestamp lastUpdate;

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
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
