package com.hana7.hanaro.item.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item,Long> {
	@EntityGraph(attributePaths = "itemImages")
	Optional<Item> findByItemId(Long itemId);
}
