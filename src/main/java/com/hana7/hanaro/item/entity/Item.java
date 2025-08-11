package com.hana7.hanaro.item.entity;

import java.util.ArrayList;
import java.util.List;

import com.hana7.hanaro.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Item")
@Getter
@Setter
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;

	@Column(length = 20, nullable = false)
	private String itemName;

	private int price;

	private int stock;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemImage> itemImages = new ArrayList<>();

	public void addImage(ItemImage itemImage) {
		itemImages.add(itemImage);
		itemImage.setItem(this);
	}

	public void removeImage(ItemImage itemImage) {
		itemImages.remove(itemImage);
		itemImage.setItem(null);
	}
}
