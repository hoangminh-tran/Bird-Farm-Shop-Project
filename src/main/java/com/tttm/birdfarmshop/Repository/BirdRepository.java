package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Bird;
import com.tttm.birdfarmshop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BirdRepository extends JpaRepository<Bird, String> {
    @Query(
            value = " select * from `bird` where healthcareid = ?1", nativeQuery = true
    )
    List<Bird> getAllBirdBelongToHealthcare(int healthcareID);
}
