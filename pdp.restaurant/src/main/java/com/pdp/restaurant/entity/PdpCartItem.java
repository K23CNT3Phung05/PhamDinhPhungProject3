package com.pdp.restaurant.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // Thêm Builder để tạo đối tượng nhanh hơn
public class PdpCartItem {

    private Long dishId;
    private String dishName;
    private String imageUrl;

    // Thêm đối tượng Dish để Controller lấy được dữ liệu lưu vào DB
    private PdpDish dish;

    private BigDecimal price;
    private Integer quantity;

    public BigDecimal getSubTotal() {
        if (price == null || quantity == null || quantity <= 0) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}