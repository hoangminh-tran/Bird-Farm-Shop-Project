package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Image;
import com.tttm.birdfarmshop.Models.TypeOfBird;
import com.tttm.birdfarmshop.Models.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeOfBirdRepository extends JpaRepository<TypeOfBird, String> {
    @Query(
            value = "SELECT tob.* FROM type_of_bird tob join bird b on b.typeid = tob.typeid where b.birdid = ?1", nativeQuery = true
    )
    List<TypeOfBird> findTypeOfBirdByBirdId(String birdID);
}
