package com.pdp.restaurant.repository;

import com.pdp.restaurant.entity.PdpOrder;
import com.pdp.restaurant.entity.PdpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PdpOrderRepository extends JpaRepository<PdpOrder, Long> {


    @Query(value = "SELECT COALESCE(SUM(total_amount), 0) FROM pdp_order " +
            "WHERE (order_status = 'PAID' OR order_status = 'COMPLETED' " +
            "OR order_status = 'Hoàn tất' OR order_status = 'Đã thanh toán') " +
            "AND MONTH(created_at) = MONTH(CURRENT_DATE) " +
            "AND YEAR(created_at) = YEAR(CURRENT_DATE)", nativeQuery = true)
    Double sumMonthlyRevenue();

    @Query(value = "SELECT COALESCE(SUM(total_amount), 0) FROM pdp_order " +
            "WHERE (order_status = 'PAID' OR order_status = 'COMPLETED' " +
            "OR order_status = 'Hoàn tất' OR order_status = 'Đã thanh toán') " +
            "AND DATE(created_at) = CURRENT_DATE", nativeQuery = true)
    Double sumTodayRevenue();

    @Query(value = "SELECT COUNT(*) FROM pdp_order " +
            "WHERE order_status IN ('PAID', 'COMPLETED', 'Hoàn tất', 'Đã thanh toán')", nativeQuery = true)
    long countCompletedOrders();

    @Query(value = "SELECT COALESCE(SUM(o.total_amount), 0) FROM ( " +
            "SELECT CURRENT_DATE - INTERVAL (6-i) DAY AS date FROM (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6) AS days " +
            ") d LEFT JOIN pdp_order o ON DATE(o.created_at) = d.date " +
            "AND o.order_status IN ('PAID', 'COMPLETED', 'Hoàn tất', 'Đã thanh toán') " +
            "GROUP BY d.date ORDER BY d.date", nativeQuery = true)
    List<Double> getRevenueLast7Days();

    List<PdpOrder> findByUser(PdpUser user);
}