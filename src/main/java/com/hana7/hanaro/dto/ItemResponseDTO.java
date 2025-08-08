package com.hana7.hanaro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Integer stock;
	private List<ItemImageResponseDTO> images;
}
