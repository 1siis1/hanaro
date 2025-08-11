package com.hana7.hanaro.item.controller;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemImageDTO;
import com.hana7.hanaro.item.dto.ItemResponseDTO;
import com.hana7.hanaro.item.service.FileService;
import com.hana7.hanaro.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	private final FileService fileService; // FileService 주입

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Long> createItem(@Valid @RequestPart("item") ItemCreateRequestDTO request,
		@RequestPart("images") List<MultipartFile> itemImageFiles) throws IOException {

		// 1. 파일들을 먼저 서버에 저장하고, 결과(DTO) 리스트를 받음
		List<ItemImageDTO> imageDTOs = itemImageFiles.stream()
			.map(file -> {
				try {
					return fileService.uploadFile(file);
				} catch (IOException e) {
					// 실제 프로덕션에서는 에러 처리를 더 정교하게 해야 합니다.
					throw new RuntimeException("파일 업로드에 실패했습니다.", e);
				}
			})
			.collect(Collectors.toList());

		// 2. 상품 정보와 이미지 정보(DTO) 리스트를 서비스에 전달
		Long itemId = itemService.createItem(request, imageDTOs);
		return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
	}

	// ... 나머지 GET, PUT, DELETE 메서드 ...
}
