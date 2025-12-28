package com.pdp.restaurant.service;

import com.pdp.restaurant.entity.*;
import com.pdp.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdpWalletService {

    private final PdpWalletRepository walletRepo;
    private final PdpWalletTransactionRepository transactionRepo;
    private final PdpUserRepository userRepo;

    // ================= LẤY VÍ =================
    public PdpWallet getWallet(String email) {
        PdpUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user: " + email));

        return walletRepo.findByUser(user)
                .orElseGet(() -> {
                    PdpWallet wallet = new PdpWallet();
                    wallet.setUser(user);
                    wallet.setBalance(BigDecimal.ZERO);
                    return walletRepo.save(wallet);
                });
    }

    // ================= NẠP TIỀN =================
    @Transactional
    public void deposit(String email, BigDecimal amount, PdpPaymentMethod method) {
        PdpWallet wallet = getWallet(email);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepo.save(wallet);

        PdpWalletTransaction transaction = PdpWalletTransaction.builder()
                .user(wallet.getUser())
                .amount(amount)
                .transactionType(PdpTransactionType.DEPOSIT)
                .paymentMethod(method)
                .description("Nạp tiền vào ví")
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepo.save(transaction);
    }

    // ================= RÚT TIỀN =================
    @Transactional
    public void withdraw(String email, BigDecimal amount) {
        PdpWallet wallet = getWallet(email);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Số dư không đủ");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepo.save(wallet);

        PdpWalletTransaction transaction = PdpWalletTransaction.builder()
                .user(wallet.getUser())
                .amount(amount)
                .transactionType(PdpTransactionType.WITHDRAW)
                .paymentMethod(PdpPaymentMethod.CASH)
                .description("Rút tiền từ ví")
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepo.save(transaction);
    }

    // ================= THANH TOÁN ĐƠN =================
    @Transactional
    public void payOrder(String email, BigDecimal amount, Long orderId, PdpPaymentMethod method) {
        PdpWallet wallet = getWallet(email);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Số dư ví không đủ để thanh toán");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepo.save(wallet);

        PdpWalletTransaction transaction = PdpWalletTransaction.builder()
                .user(wallet.getUser())
                .amount(amount)
                .transactionType(PdpTransactionType.PAY_ORDER)
                .paymentMethod(method)
                .description("Thanh toán đơn hàng #" + orderId)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepo.save(transaction);
    }

    // ================= LỊCH SỬ =================
    public List<PdpWalletTransaction> getTransactions(String email) {
        PdpUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user: " + email));

        return transactionRepo.findByUserOrderByCreatedAtDesc(user);
    }
}
