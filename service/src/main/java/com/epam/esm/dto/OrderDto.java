package com.epam.esm.dto;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.UserEntity;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderDto  extends RepresentationModel<OrderDto> {
    private long orderId;
    private LocalDateTime createDate;
    private BigDecimal totalCost;
    private List<GiftCertificateEntity> giftCertificates;
    private UserEntity user;

    public OrderDto() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public List<GiftCertificateEntity> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(List<GiftCertificateEntity> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return orderId == orderDto.orderId && Objects.equals(createDate, orderDto.createDate) && Objects.equals(totalCost, orderDto.totalCost) && Objects.equals(giftCertificates, orderDto.giftCertificates) && Objects.equals(user, orderDto.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, createDate, totalCost, giftCertificates, user);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderId=" + orderId +
                ", createDate=" + createDate +
                ", totalCost=" + totalCost +
                ", giftCertificates=" + giftCertificates +
                ", user=" + user +
                '}';
    }
}
