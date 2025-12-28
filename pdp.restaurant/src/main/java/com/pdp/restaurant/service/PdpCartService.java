package com.pdp.restaurant.service;

import com.pdp.restaurant.entity.PdpCartItem;
import com.pdp.restaurant.entity.PdpDish;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdpCartService {

    private static final String CART_SESSION_KEY = "cart";

    // =========================
    // LẤY GIỎ HÀNG TỪ SESSION
    // =========================
    @SuppressWarnings("unchecked")
    public Map<Long, PdpCartItem> getCart(HttpSession session) {
        Map<Long, PdpCartItem> cart =
                (Map<Long, PdpCartItem>) session.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    // =========================
    // LẤY ITEM CHO VIEW
    // =========================
    public Collection<PdpCartItem> getCartItems(HttpSession session) {
        return getCart(session).values();
    }

    // =========================
    // ĐẾM SỐ LƯỢNG (BADGE)
    // =========================
    public int getItemCount(HttpSession session) {
        return getCartItems(session).stream()
                .mapToInt(PdpCartItem::getQuantity)
                .sum();
    }

    // =========================
    // TÍNH TỔNG TIỀN
    // =========================
    public BigDecimal getTotalPrice(HttpSession session) {
        return getCartItems(session).stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // =========================
    // THÊM VÀO GIỎ
    // =========================
    public void addToCart(PdpDish dish, HttpSession session) {
        Map<Long, PdpCartItem> cart = getCart(session);

        cart.compute(dish.getId(), (id, item) -> {
            if (item == null) {
                PdpCartItem newItem = new PdpCartItem();
                newItem.setDishId(dish.getId());
                newItem.setDishName(dish.getName());
                newItem.setImageUrl(dish.getImageUrl());
                newItem.setPrice(dish.getPrice());
                newItem.setQuantity(1);
                return newItem;
            }
            item.setQuantity(item.getQuantity() + 1);
            return item;
        });
    }

    // =========================
    // CẬP NHẬT SỐ LƯỢNG
    // =========================
    public void updateQuantity(Long dishId, int quantity, HttpSession session) {
        Map<Long, PdpCartItem> cart = getCart(session);

        if (quantity <= 0) {
            cart.remove(dishId);
        } else if (cart.containsKey(dishId)) {
            cart.get(dishId).setQuantity(quantity);
        }
    }

    // =========================
    // XOÁ 1 MÓN
    // =========================
    public void removeItem(Long dishId, HttpSession session) {
        getCart(session).remove(dishId);
    }

    // =========================
    // XOÁ TOÀN BỘ GIỎ
    // =========================
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}
