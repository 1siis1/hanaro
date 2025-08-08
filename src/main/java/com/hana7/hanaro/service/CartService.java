package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.CartItemRequestDTO;
import com.hana7.hanaro.dto.CartItemResponseDTO;
import com.hana7.hanaro.dto.ItemResponseDTO;
import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.CartItem;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.entity.Member;
import com.hana7.hanaro.repository.CartItemRepository;
import com.hana7.hanaro.repository.CartRepository;
import com.hana7.hanaro.repository.ItemRepository;
import com.hana7.hanaro.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@Service
@Transactional
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.memberRepository = memberRepository;
		this.itemRepository = itemRepository;
	}

	public void addCartItem(@Valid CartItemRequestDTO request) {
		// 현재 로그인한 회원 정보 가져오기
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

		// 회원의 장바구니 찾기, 없으면 새로 생성
		Cart cart = cartRepository.findByMember(member)
			.orElseGet(() -> cartRepository.save(new Cart(null, member, null)));

		Item item = itemRepository.findById(request.getItemId())
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		// 이미 담긴 상품인지 확인
		cartItemRepository.findByCartAndItem(cart, item)
			.ifPresentOrElse(
				cartItem -> cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity()),
				() -> {
					CartItem newCartItem = CartItem.builder()
						.cart(cart)
						.item(item)
						.quantity(request.getQuantity())
						.build();
					cartItemRepository.save(newCartItem);
				}
			);
	}

	@Transactional(readOnly = true)
	public List<CartItemResponseDTO> getCartItems() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

		Cart cart = cartRepository.findByMember(member)
			.orElse(null);

		if (cart == null) {
			return Collections.emptyList();
		}

		return cart.getCartItems().stream()
			.map(cartItem -> CartItemResponseDTO.builder()
				.id(cartItem.getId())
				.item(ItemResponseDTO.builder()
					.id(cartItem.getItem().getId())
					.name(cartItem.getItem().getName())
					.price(cartItem.getItem().getPrice())
					.build())
				.quantity(cartItem.getQuantity())
				.build())
			.collect(Collectors.toList());
	}

	public void updateCartItem(Long id, @Valid CartItemRequestDTO request) {
		CartItem cartItem = cartItemRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다."));

		cartItem.setQuantity(request.getQuantity());
	}

	public void removeCartItem(Long id) {
		cartItemRepository.deleteById(id);
	}
}
