package com.hana7.hanaro.item.dto;

import com.hana7.hanaro.item.entity.ItemImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageDTO {
	private String orgName;
	private String saveName;
	private String saveDir;

	// DTO를 Entity로 변환하는 메서드
	public ItemImage toEntity() {
		return ItemImage.builder()
			.orgName(this.orgName)
			.saveName(this.saveName)
			.saveDir(this.saveDir)
			.build();
	}
}
