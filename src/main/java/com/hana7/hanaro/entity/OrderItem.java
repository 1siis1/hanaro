package com.hana7.hanaro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"order", "item"}) // 순환 참조 방지를 위해 연관 관계 필드 제외
public class OrderItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(nullable = false)
	private BigDecimal orderPrice; // 주문 당시 상품 가격

	@Column(nullable = false)
	private int quantity;
}
