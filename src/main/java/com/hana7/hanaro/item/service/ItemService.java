package com.hana7.hanaro.item.service;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemImageDTO;
import com.hana7.hanaro.item.dto.ItemResponseDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;
import com.hana7.hanaro.item.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public Long createItem(ItemCreateRequestDTO request, List<ItemImageDTO> imageDTOs) {
		Item item = Item.builder()
			.name(request.getName())
			.price(request.getPrice())
			.stock(request.getStock())
			.build();

		// DTO 리스트를 Entity 리스트로 변환하고, 각 이미지에 상품 정보를 연결
		if (imageDTOs != null && !imageDTOs.isEmpty()) {
			List<ItemImage> itemImages = imageDTOs.stream()
				.map(ItemImageDTO::toEntity)
				.peek(image -> image.setItem(item)) // 각 이미지에 상품(item)을 연결
				.collect(Collectors.toList());
			item.setItemImages(itemImages);
		}

		itemRepository.save(item);
		return item.getId();
	}

	// ... getItem, getItemList, updateItem, deleteItem 메서드는 이전과 거의 동일 ...
	// (update 로직은 추후 이미지 교체 로직 추가 시 변경 필요)
}
