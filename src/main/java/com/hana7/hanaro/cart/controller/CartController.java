package com.hana7.hanaro.cart.controller;

import com.hana7.hanaro.cart.dto.CartItemAddDTO;
import com.hana7.hanaro.cart.dto.CartItemUpdateDTO;
import com.hana7.hanaro.cart.dto.CartListDTO;
import com.hana7.hanaro.cart.service.CartService;
import com.hana7.hanaro.config.UserPrincipal;
import com.hana7.hanaro.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "장바구니")
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@Operation(summary = "장바구니 상품 추가")
	@PostMapping
	public ResponseEntity<ApiResponse<Long>> addCartItem(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody @Valid CartItemAddDTO cartItemAddDto) {
		Long cartItemId = cartService.addCartItem(userPrincipal.getMember().getId(), cartItemAddDto);
		return ResponseEntity.ok(ApiResponse.onSuccess(cartItemId, "장바구니에 상품을 담았습니다."));
	}

	@Operation(summary = "장바구니 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResponse<List<CartListDTO>>> getCartList(
		@AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<CartListDTO> cartList = cartService.getCartList(userPrincipal.getMember().getId());
		return ResponseEntity.ok(ApiResponse.onSuccess(cartList, "장바구니 목록 조회 성공"));
	}

	@Operation(summary = "장바구니 상품 수량 변경")
	@PatchMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse<Void>> updateCartItemCount(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable Long cartItemId,
		@RequestBody @Valid CartItemUpdateDTO updateDto) {
		cartService.updateCartItemCount(cartItemId, updateDto.getQuantity(), userPrincipal.getMember().getId());
		return ResponseEntity.ok(ApiResponse.onSuccess(null, "장바구니 상품 수량을 변경했습니다."));
	}

	@Operation(summary = "장바구니 상품 삭제")
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse<Void>> deleteCartItem(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable Long cartItemId) {
		cartService.deleteCartItem(cartItemId, userPrincipal.getMember().getId());
		return ResponseEntity.ok(ApiResponse.onSuccess(null, "장바구니 상품을 삭제했습니다."));
	}
}
