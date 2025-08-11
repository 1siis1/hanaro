package com.hana7.hanaro.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateRequestDTO {

	/**
	 * 상품명 (Item.name)
	 */
	@NotBlank(message = "상품명은 필수 입력 값입니다.")
	private String name;

	/**
	 * 가격 (Item.price)
	 */
	@NotNull(message = "가격은 필수 입력 값입니다.")
	@Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
	private Integer price;

	/**
	 * 재고 (Item.stock)
	 */
	@NotNull(message = "재고 수량은 필수 입력 값입니다.")
	@Min(value = 0, message = "재고 수량은 0개 이상이어야 합니다.")
	private Integer stock;
}
