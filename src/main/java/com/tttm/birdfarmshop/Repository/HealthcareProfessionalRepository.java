package com.tttm.birdfarmshop.Repository;


import com.tttm.birdfarmshop.Models.HealthcareProfessional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthcareProfessionalRepository extends JpaRepository<HealthcareProfessional, Integer> {
}
