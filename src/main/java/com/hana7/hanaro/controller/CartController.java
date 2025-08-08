package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.CartItemRequestDTO;
import com.hana7.hanaro.dto.CartItemResponseDTO;
import com.hana7.hanaro.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping
	public ResponseEntity<String> addCartItem(@Valid @RequestBody CartItemRequestDTO request) {
		cartService.addCartItem(request);
		return new ResponseEntity<>("상품이 장바구니에 추가되었습니다.", HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<CartItemResponseDTO>> getCartItems() {
		List<CartItemResponseDTO> cartItems = cartService.getCartItems();
		return new ResponseEntity<>(cartItems, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateCartItem(@PathVariable Long id, @Valid @RequestBody CartItemRequestDTO request) {
		cartService.updateCartItem(id, request);
		return new ResponseEntity<>("장바구니 상품 수량이 수정되었습니다.", HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> removeCartItem(@PathVariable Long id) {
		cartService.removeCartItem(id);
		return new ResponseEntity<>("상품이 장바구니에서 삭제되었습니다.", HttpStatus.NO_CONTENT);
	}
}
