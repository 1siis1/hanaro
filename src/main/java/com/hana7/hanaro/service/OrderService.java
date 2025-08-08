package com.hana7.hanaro.service;

import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.CartItem;
import com.hana7.hanaro.entity.Member;
import com.hana7.hanaro.entity.Order;
import com.hana7.hanaro.entity.OrderItem;
import com.hana7.hanaro.entity.OrderStatus;
import com.hana7.hanaro.repository.CartRepository;
import com.hana7.hanaro.repository.OrderRepository;
import com.hana7.hanaro.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final MemberRepository memberRepository;

	public OrderService(OrderRepository orderRepository, CartRepository cartRepository, MemberRepository memberRepository) {
		this.orderRepository = orderRepository;
		this.cartRepository = cartRepository;
		this.memberRepository = memberRepository;
	}

	public void createOrderFromCart() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new IllegalArgumentException("장바구니가 비어있습니다."));

		// 주문 상품 리스트 생성
		List<OrderItem> orderItems = cart.getCartItems().stream()
			.map(cartItem -> OrderItem.builder()
				.item(cartItem.getItem())
				.orderPrice(cartItem.getItem().getPrice())
				.quantity(cartItem.getQuantity())
				.build())
			.collect(Collectors.toList());

		// 총액 계산
		BigDecimal totalAmount = orderItems.stream()
			.map(item -> item.getOrderPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		// 주문 엔티티 생성
		Order order = Order.builder()
			.member(member)
			.status(OrderStatus.PAID)
			.totalAmount(totalAmount)
			.orderItems(orderItems)
			.build();

		orderRepository.save(order);

		// 주문 생성 후 장바구니 비우기
		cart.getCartItems().clear();
		cartRepository.save(cart);
	}
}
