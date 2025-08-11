package com.hana7.hanaro.cart.dto;

import com.hana7.hanaro.cart.entity.CartItem;

import lombok.Getter;

@Getter
public class CartListDTO {
	private Long cartItemId;
	private String itemName;
	private int price;
	private int quantity;

	public CartListDTO(CartItem cartItem) {
		this.cartItemId = cartItem.getId();
		this.itemName = cartItem.getItem().getItemName();
		this.price = cartItem.getItem().getPrice();
		this.quantity = cartItem.getQuantity();
	}

}
