package com.hana7.hanaro.repository;

import com.hana7.hanaro.entity.Cart;
import com.hana7.hanaro.entity.CartItem;
import com.hana7.hanaro.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartAndItem(Cart cart, Item item);
}
