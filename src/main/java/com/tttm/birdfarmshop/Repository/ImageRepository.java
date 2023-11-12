package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Image;
import com.tttm.birdfarmshop.Models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Query(
            value = "select * from image where productID = ?1", nativeQuery = true
    )
    List<Image> findImageByProductID(String productID);

    @Modifying
    @Query(value = "delete from `image` where productID = ?1", nativeQuery = true)
    void deleteImageByproductID(String productID);
}
