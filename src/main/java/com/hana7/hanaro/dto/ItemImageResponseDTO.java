package com.hana7.hanaro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageResponseDTO {
	private Long id;
	private String orgname;
	private String savename;
	private String savedir;
}
