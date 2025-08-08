package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.dto.ItemResponseDTO;
import com.hana7.hanaro.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/items") // 관리자 전용 API
public class ItemController {

	private final ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@PostMapping
	public ResponseEntity<String> createItem(@Valid @RequestBody ItemCreateRequestDTO request) {
		itemService.createItem(request);
		return new ResponseEntity<>("상품이 성공적으로 등록되었습니다.", HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<ItemResponseDTO>> getItems(@RequestParam(required = false) String keyword) {
		List<ItemResponseDTO> items = itemService.getItems(keyword);
		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ItemResponseDTO> getItem(@PathVariable Long id) {
		ItemResponseDTO item = itemService.getItem(id);
		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateItem(@PathVariable Long id, @Valid @RequestBody ItemUpdateRequestDTO request) {
		itemService.updateItem(id, request);
		return new ResponseEntity<>("상품이 성공적으로 수정되었습니다.", HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteItem(@PathVariable Long id) {
		itemService.deleteItem(id);
		return new ResponseEntity<>("상품이 성공적으로 삭제되었습니다.", HttpStatus.NO_CONTENT);
	}
}
