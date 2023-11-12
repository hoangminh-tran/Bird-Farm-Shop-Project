package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Admin;
import com.tttm.birdfarmshop.Models.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Integer> {
}
