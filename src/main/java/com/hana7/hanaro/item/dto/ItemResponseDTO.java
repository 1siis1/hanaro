package com.hana7.hanaro.item.dto;

import com.hana7.hanaro.item.entity.Item;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ItemResponseDTO {
	private Long id;
	private String itemName;
	private int price;
	private int stockNumber;
	private List<ItemImageResponseDTO> itemImages;

	public ItemResponseDTO(Item item) {
		this.id = item.getId();
		this.itemName = item.getName();
		this.price = item.getPrice();
		this.stockNumber = item.getStock();
		this.itemImages = item.getItemImages().stream()
			.map(ItemImageResponseDTO::new)
			.collect(Collectors.toList());
	}
}
