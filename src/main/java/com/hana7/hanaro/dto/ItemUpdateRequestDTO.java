package com.hana7.hanaro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequestDTO {
	@NotBlank(message = "상품명은 필수 입력 항목입니다.")
	@Size(max = 100, message = "상품명은 100자 이내로 입력해주세요.")
	private String name;

	@Size(max = 500, message = "설명은 500자 이내로 입력해주세요.")
	private String description;

	@NotNull(message = "가격은 필수 입력 항목입니다.")
	@Min(value = 0, message = "가격은 0 이상이어야 합니다.")
	private BigDecimal price;

	@NotNull(message = "재고 수량은 필수 입력 항목입니다.")
	@Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
	private Integer stock;

}
