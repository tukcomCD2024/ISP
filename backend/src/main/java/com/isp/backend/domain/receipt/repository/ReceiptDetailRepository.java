package com.isp.backend.domain.receipt.repository;

import com.isp.backend.domain.receipt.entity.Receipt;
import com.isp.backend.domain.receipt.entity.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {
    void deleteAllByReceipt(Receipt receipt);

    List<ReceiptDetail> findByReceiptId(Long receiptId);

}
