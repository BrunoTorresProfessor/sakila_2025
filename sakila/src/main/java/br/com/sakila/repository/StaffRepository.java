package br.com.sakila.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sakila.model.StaffModel;

@Repository
public interface StaffRepository extends JpaRepository<StaffModel, Integer> {

}
