package com.hana7.hanaro.cart.service;

import com.hana7.hanaro.cart.dto.CartItemAddDTO;
import com.hana7.hanaro.cart.dto.CartListDTO;
import com.hana7.hanaro.cart.entity.Cart;
import com.hana7.hanaro.cart.entity.CartItem;
import com.hana7.hanaro.cart.repository.CartItemRepository;
import com.hana7.hanaro.cart.repository.CartRepository;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.repository.ItemRepository;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	public Long addCartItem(Long memberId, CartItemAddDTO cartItemAddDto) {
		Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
		Cart cart = cartRepository.findByMemberId(member.getId()).orElseThrow(EntityNotFoundException::new);
		Item item = itemRepository.findById(cartItemAddDto.getItemId()).orElseThrow(EntityNotFoundException::new);

		// ** 핵심 로직: 장바구니에 이미 상품이 있는지 확인 **
		CartItem savedCartItem = cartItemRepository.findByCartIdAndItem_ItemId(cart.getId(), item.getItemId()).orElse(null);

		if (savedCartItem != null) {
			// Case 1: 이미 상품이 있으면 수량만 증가
			savedCartItem.addCount(cartItemAddDto.getQuantity());
			return savedCartItem.getId();
		} else {
			// Case 2: 없으면 새로 추가
			CartItem cartItem = CartItem.builder()
				.cart(cart)
				.item(item)
				.quantity(cartItemAddDto.getQuantity())
				.build();
			cartItemRepository.save(cartItem);
			return cartItem.getId();
		}
	}

	@Transactional(readOnly = true)
	public List<CartListDTO> getCartList(Long memberId) {
		Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(EntityNotFoundException::new);

		return cart.getCartItems().stream()
			.map(cartItem -> {
				return new CartListDTO(cartItem);
			})
			.collect(Collectors.toList());
	}

	public void updateCartItemCount(Long cartItemId, int quantity, Long memberId) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

		if (!cartItem.getCart().getMember().getId().equals(memberId)) {
			throw new SecurityException("수정 권한이 없습니다.");
		}
		cartItem.setQuantity(quantity);
	}

	public void deleteCartItem(Long cartItemId, Long memberId) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

		if (!cartItem.getCart().getMember().getId().equals(memberId)) {
			throw new SecurityException("삭제 권한이 없습니다.");
		}
		cartItemRepository.delete(cartItem);
	}
}
