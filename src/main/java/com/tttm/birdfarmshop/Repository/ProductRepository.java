package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Product;
import com.tttm.birdfarmshop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(
            value = "select * from product where productid like '%F%'", nativeQuery = true
    )
    List<Product> findAllFood();

    @Query(
            value = "select * from product where productid like '%N%'", nativeQuery = true
    )
    List<Product> findAllNest();

    @Query(
            value = "select * from product where productid like '%B%'", nativeQuery = true
    )
    List<Product> findAllBird();

    @Query(
            value = "SELECT * FROM product WHERE type_of_product like ?1 order by price ASC",
            nativeQuery = true
    )
    List<Product> sortProductByPriceAndTypeOfProductAscending(String typeOfProduct);

    @Query(
            value = "SELECT * FROM product WHERE type_of_product like ?1 order by price DESC",
            nativeQuery = true
    )
    List<Product> sortProductByPriceAndTypeOfProductDescending(String typeOfProduct);

    @Query(
            value = "SELECT * FROM product WHERE type_of_product like ?1 order by product_name ASC",
            nativeQuery = true
    )
    List<Product> sortProductByProductNameAndTypeOfProductAscending(String typeOfProduct);

    @Query(
            value = "SELECT * FROM product WHERE type_of_product like ?1 order by product_name DESC",
            nativeQuery = true
    )
    List<Product> sortProductByProductNameAndTypeOfProductDescending(String typeOfProduct);

}
