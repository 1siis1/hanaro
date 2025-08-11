package com.hana7.hanaro.item.controller;

import com.hana7.hanaro.item.dto.ItemDTO;
import com.hana7.hanaro.item.dto.ItemDetailDTO;
import com.hana7.hanaro.item.dto.ItemImageResponseDTO;
import com.hana7.hanaro.item.service.ItemService;
import com.hana7.hanaro.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

	private final ItemService itemService;

	@Tag(name = "상품")
	@Operation(summary = "상품 추가", description = "상품을 추가합니다")
	@PostMapping("/admin/add")
	public ResponseEntity<ApiResponse<ItemDTO>> addItem(@RequestBody ItemDTO requestDTO) {
		ItemDTO registeredItem = itemService.addItem(requestDTO);
		return ResponseEntity.ok(ApiResponse.onSuccess(registeredItem, "상품 추가 완료"));
	}

	@Tag(name = "상품")
	@Operation(summary = "상품 리스트", description = "상품 목록을 조회합니다")
	@GetMapping
	public ResponseEntity<ApiResponse<List<ItemDTO>>> getAllItems() {
		List<ItemDTO> items = itemService.getAllItems();
		return ResponseEntity.ok(ApiResponse.onSuccess(items, "상품 리스트"));
	}

	@Tag(name = "상품")
	@Operation(summary = "상품 조회", description = "상품을 조회합니다")
	@GetMapping("/{itemId}")
	public ResponseEntity<ApiResponse<ItemDetailDTO>> getItemById(@PathVariable Long itemId) {
		ItemDetailDTO item = itemService.getItemDetail(itemId);
		return ResponseEntity.ok(ApiResponse.onSuccess(item, "상품 조회 완료"));
	}

	@Tag(name = "상품")
	@Operation(summary = "상품 수정", description = "상품을 수정합니다")
	@PutMapping("/admin/edit/{itemId}")
	public ResponseEntity<ApiResponse<ItemDTO>> updateItem(@PathVariable Long itemId, @RequestBody ItemDTO requestDTO) {
		ItemDTO updatedItem = itemService.updateItem(itemId, requestDTO);
		return ResponseEntity.ok(ApiResponse.onSuccess(updatedItem, "상품 수정 완료"));
	}

	@Tag(name = "상품")
	@Operation(summary = "상품 삭제", description = "상품을 삭제합니다")
	@DeleteMapping("/admin/delete/{itemId}")
	public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long itemId) {
		itemService.deleteItem(itemId);
		return ResponseEntity.ok(ApiResponse.onSuccess(null, "상품 삭제 완료"));
	}

	@Tag(name = "상품 이미지")
	@Operation(summary = "상품 이미지 추가", description = "상품 이미지를 추가합니다")
	@PostMapping(value = "/admin/images/{itemId}/add", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<ItemImageResponseDTO>> addImageToItem(@PathVariable Long itemId, @RequestPart("file") MultipartFile file) {
		ItemImageResponseDTO itemImage = itemService.addImageToItem(itemId, file);
		return ResponseEntity.ok(ApiResponse.onSuccess(itemImage, "상품 이미지 등록 완료"));
	}

	@Tag(name = "상품 이미지")
	@Operation(summary = "상품 이미지 삭제", description = "상품 이미지를 삭제합니다")
	@DeleteMapping("/admin/images/{imageId}/delete")
	public ResponseEntity<ApiResponse<Void>> deleteImageFromItem(@PathVariable Long imageId) {
		itemService.deleteImageFromItem(imageId);
		return ResponseEntity.ok(ApiResponse.onSuccess(null, "상품 이미지 삭제 완료"));
	}
}
