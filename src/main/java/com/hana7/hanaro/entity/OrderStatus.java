package com.hana7.hanaro.entity;

public enum OrderStatus {
	PAID("결제완료"),
	READY("배송준비"),
	SHIPPING("배송중"),
	DELIVERED("배송완료");

	private final String label;

	OrderStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
