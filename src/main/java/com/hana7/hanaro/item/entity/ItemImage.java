package com.hana7.hanaro.item.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_image_id")
	private Long id;

	private String orgName; // 원본 파일명 (예: my_cat.jpg)
	private String saveName; // 서버에 저장될 고유 파일명 (예: uuid.jpg)
	private String saveDir;  // 파일이 저장된 경로 (예: /upload/2025/08/10)

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
}
