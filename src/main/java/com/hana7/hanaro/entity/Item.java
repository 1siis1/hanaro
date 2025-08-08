package com.hana7.hanaro.entity;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DynamicInsert
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = {"images"})
@EqualsAndHashCode(callSuper = true)
public class Item extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(length = 500)
	private String description;

	@Column(nullable = false)
	private BigDecimal price;

	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer stock;

	@OneToMany(mappedBy = "item",
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	@Column(nullable=false)
	private List<ItemImage> images;
}
