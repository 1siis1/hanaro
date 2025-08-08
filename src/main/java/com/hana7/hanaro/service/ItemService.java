package com.hana7.hanaro.service;

import com.hana7.hanaro.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.dto.ItemResponseDTO;
import com.hana7.hanaro.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.entity.Item;
import com.hana7.hanaro.entity.MemberRole;
import com.hana7.hanaro.repository.ItemRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

	private final ItemRepository itemRepository;

	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public void createItem(ItemCreateRequestDTO request) {
		// 관리자 권한 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean isAdmin = authorities.stream()
			.anyMatch(a -> a.getAuthority().equals(MemberRole.ROLE_ADMIN.name()));

		if (!isAdmin) {
			throw new AccessDeniedException("상품 등록 권한이 없습니다.");
		}

		// DTO를 엔티티로 변환하여 저장하는 로직
		Item item = Item.builder()
			.name(request.getName())
			.description(request.getDescription())
			.price(request.getPrice())
			.stock(request.getStock())
			.build();
		itemRepository.save(item);
	}

	@Transactional(readOnly = true)
	public List<ItemResponseDTO> getItems(String keyword) {
		List<Item> items;
		if (keyword != null && !keyword.trim().isEmpty()) {
			items = itemRepository.findByNameContaining(keyword);
		} else {
			items = itemRepository.findAll();
		}
		return items.stream()
			.map(item -> ItemResponseDTO.builder()
				.id(item.getId())
				.name(item.getName())
				.description(item.getDescription())
				.price(item.getPrice())
				.stock(item.getStock())
				.build())
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ItemResponseDTO getItem(Long id) {
		Item item = itemRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		return ItemResponseDTO.builder()
			.id(item.getId())
			.name(item.getName())
			.description(item.getDescription())
			.price(item.getPrice())
			.stock(item.getStock())
			.build();
	}

	public void updateItem(Long id, ItemUpdateRequestDTO request) {
		Item item = itemRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		item.setName(request.getName());
		item.setDescription(request.getDescription());
		item.setPrice(request.getPrice());
		item.setStock(request.getStock());
	}

	public void deleteItem(Long id) {
		itemRepository.deleteById(id);
	}
}
