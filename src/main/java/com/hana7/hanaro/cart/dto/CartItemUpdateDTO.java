package com.hana7.hanaro.cart.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateDTO {
	@Min(value = 1, message = "상품 수량은 1개 이상으로 선택해주세요.")
	private int quantity;
}
