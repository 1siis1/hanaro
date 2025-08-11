package com.hana7.hanaro.item.dto;

import com.hana7.hanaro.item.entity.ItemImage;
import lombok.Getter;

@Getter
public class ItemImageResponseDTO {
	private Long id;
	private String imageUrl; // 최종 URL

	public ItemImageResponseDTO(ItemImage itemImage) {
		this.id = itemImage.getId();
		this.imageUrl = itemImage.getSaveDir() + "/" + itemImage.getSaveName();
	}
}
