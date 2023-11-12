package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Nest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NestRepository extends JpaRepository<Nest, String> {
}
