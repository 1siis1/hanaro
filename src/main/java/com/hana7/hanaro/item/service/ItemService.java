package com.hana7.hanaro.item.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hana7.hanaro.exception.GeneralException;
import com.hana7.hanaro.item.dto.ItemImageResponseDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;
import com.hana7.hanaro.item.repository.ItemImageRepository;
import com.hana7.hanaro.item.repository.ItemRepository;
import com.hana7.hanaro.item.dto.ItemDTO;
import com.hana7.hanaro.response.code.status.ErrorStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;

	private static final String BASE_UPLOAD_PATH = "src/main/resources/static/";
	private static final String ORIGIN_DIR = "origin";
	private static final String UPLOAD_DIR = "upload";

	private static final long MAX_FILE_SIZE_BYTES = 512 * 1024;
	private static final long MAX_TOTAL_FILE_SIZE_BYTES = 3 * 1024 * 1024;

	private ItemDTO toDTO(Item item) {
		return ItemDTO.builder()
			.itemId(item.getItemId())
			.itemName(item.getItemName())
			.price(item.getPrice())
			.stock(item.getStock())
			.build();
	}

	private ItemImageResponseDTO toImageDTO(ItemImage itemImage) {
		return ItemImageResponseDTO.builder()
			.imageId(itemImage.getImageId())
			.imageUrl(itemImage.getImageUrl())
			.itemId(itemImage.getItem().getItemId())
			.build();
	}

	@Transactional
	public ItemDTO addItem(ItemDTO requestDTO) {
		Item item = Item.builder()
			.itemName(requestDTO.getItemName())
			.price(requestDTO.getPrice())
			.stock(requestDTO.getStock())
			.build();
		return toDTO(itemRepository.save(item));
	}

	@Transactional()
	public List<ItemDTO> getAllItems() {
		return itemRepository.findAll().stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}

	@Transactional
	public ItemDTO getItemById(Long itemId) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
		return toDTO(item);
	}

	@Transactional
	public ItemDTO updateItem(Long itemId, ItemDTO requestDTO) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
		item.setItemName(requestDTO.getItemName());
		item.setPrice(requestDTO.getPrice());
		item.setStock(requestDTO.getStock());

		return toDTO(itemRepository.save(item));
	}

	@Transactional
	public void deleteItem(Long itemId) {
		if (!itemRepository.existsById(itemId)) {
			throw new GeneralException(ErrorStatus.ITEM_NOT_FOUND);
		}
		itemRepository.deleteById(itemId);
	}

	@Transactional
	public ItemImageResponseDTO addImageToItem(Long itemId, MultipartFile file) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

		if (file.getSize() > MAX_FILE_SIZE_BYTES) {
			throw new GeneralException(ErrorStatus.FILE_SIZE_EXCEEDED);
		}

		LocalDate today = LocalDate.now();
		String datePath = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		Path uploadPath = Paths.get(BASE_UPLOAD_PATH, UPLOAD_DIR, datePath);
		Path originPath = Paths.get(BASE_UPLOAD_PATH, ORIGIN_DIR, datePath);

		try {
			Files.createDirectories(uploadPath);
			Files.createDirectories(originPath);
		} catch (IOException e) {
			throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
		}

		String originalFilename = file.getOriginalFilename();
		String fileExtension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

		Path targetUploadFile = uploadPath.resolve(uniqueFilename);
		Path targetOriginFile = originPath.resolve(uniqueFilename);

		try {
			file.transferTo(targetUploadFile);
			Files.copy(targetUploadFile, targetOriginFile);

			String relativeImageUrl = UPLOAD_DIR + "/" + datePath + "/" + uniqueFilename;

			ItemImage itemImage = ItemImage.builder()
				.imageUrl(relativeImageUrl)
				.build();
			item.addImage(itemImage);

			return toImageDTO(itemImageRepository.save(itemImage));

		} catch (IOException e) {
			throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
		}
	}

	@Transactional
	public void deleteImageFromItem(Long imageId) {
		ItemImage itemImage = itemImageRepository.findById(imageId)
			.orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

		Path filePath = Paths.get(BASE_UPLOAD_PATH, itemImage.getImageUrl());
		Path originFilePath = Paths.get(BASE_UPLOAD_PATH, ORIGIN_DIR,
			itemImage.getImageUrl().substring(itemImage.getImageUrl()
				.indexOf(UPLOAD_DIR) + UPLOAD_DIR.length() + 1));

		try {
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		itemImage.getItem().removeImage(itemImage);
		itemImageRepository.delete(itemImage);
	}
}
