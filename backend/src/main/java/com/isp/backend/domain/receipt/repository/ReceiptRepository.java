package com.isp.backend.domain.receipt.repository;

import com.isp.backend.domain.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("SELECT COALESCE(MAX(r.orderNum), 0) FROM Receipt r WHERE r.schedule.id = :scheduleId AND r.purchaseDate = :purchaseDate")
    int findMaxOrderNumByScheduleIdAndPurchaseDate(@Param("scheduleId") Long scheduleId, @Param("purchaseDate") String purchaseDate);

    // 스케줄에 해당하는 영수증을 purchaseDate 오름차순, orderNum 오름차순으로 정렬하여 반환
    List<Receipt> findByScheduleIdOrderByPurchaseDateAscOrderNumAsc(Long scheduleId);

    List<Receipt> findByScheduleId(Long scheduleId);

    // 스케줄 ID에 해당하는 영수증의 totalPrice 합계를 구하는 쿼리
    @Query("SELECT SUM(r.totalPrice) FROM Receipt r WHERE r.schedule.id = :scheduleId")
    Double sumTotalPriceByScheduleId(@Param("scheduleId") Long scheduleId);

    // 스케줄 ID에 해당하는 영수증의 개수를 구하는 쿼리
    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.schedule.id = :scheduleId")
    int countByScheduleId(@Param("scheduleId") Long scheduleId);

}
