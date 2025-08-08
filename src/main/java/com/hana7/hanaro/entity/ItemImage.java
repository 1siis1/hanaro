package com.hana7.hanaro.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orgname;
	private String savename;
	private String savedir;

	@ManyToOne
	@JoinColumn(
		name = "item",
		foreignKey = @ForeignKey(
			name = "fk_ItemImage_item",
			foreignKeyDefinition = """
                foreign key (item)
                   references Item(id)
                    on DELETE cascade on UPDATE cascade
                """
		)
	)
	private Item item;
}
