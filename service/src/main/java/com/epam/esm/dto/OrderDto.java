package com.epam.esm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderDto {
    private long orderId;
    private LocalDateTime createDate;
    private BigDecimal totalCost;

    public OrderDto() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getPurchaseDate() {
        return createDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.createDate = purchaseDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderId=" + orderId +
                ", purchaseDate=" + createDate +
                ", cost=" + totalCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return orderId == orderDto.orderId && Objects.equals(createDate, orderDto.createDate) && Objects.equals(totalCost, orderDto.totalCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, createDate, totalCost);
    }
}
