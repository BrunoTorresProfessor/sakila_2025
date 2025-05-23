package br.com.sakila.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sakila.model.LanguageModel;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageModel, Long> {

}
