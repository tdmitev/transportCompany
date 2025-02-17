package com.example.transportCompany.repository;

import com.example.transportCompany.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Integer> {

    List<Transport> findByEndLocation(String endLocation);
    List<Transport> findByPaidTrue();
    List<Transport> findByPaidFalse();
    List<Transport> findByStartDateBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT COUNT(t) FROM Transport t")
    Long countAllTransports();
    @Query("SELECT COUNT(t) FROM Transport t WHERE t.driver.id = :driverId")
    Long countTransportsByDriverId(@Param("driverId") Integer driverId);
    @Query("SELECT SUM(t.price) FROM Transport t")
    BigDecimal sumAllTransportPrices();
    @Query("SELECT t.driver.name, COUNT(t) FROM Transport t GROUP BY t.driver.name")
    List<Object[]> countTransportsByAllDrivers();
    @Query("SELECT t.driver.name, SUM(t.price) FROM Transport t GROUP BY t.driver.name")
    List<Object[]> sumPricesByDriver();
    List<Transport> findByDriverId(Integer driverId);
}