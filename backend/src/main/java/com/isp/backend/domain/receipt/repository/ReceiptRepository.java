package com.isp.backend.domain.receipt.repository;

import com.isp.backend.domain.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
