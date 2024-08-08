package com.isp.backend.domain.receipt.repository;

import com.isp.backend.domain.receipt.entity.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {
}
