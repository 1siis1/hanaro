package com.hana7.hanaro.item.service;

import com.hana7.hanaro.item.dto.ItemImageDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileService {

	// 파일을 업로드하고, 그 결과 정보를 ItemImageDTO로 반환
	public ItemImageDTO uploadFile(MultipartFile multipartFile) throws IOException {
		if (multipartFile == null || multipartFile.isEmpty()) {
			return null;
		}

		String uploadPath = ResourceUtils.getFile("classpath:static/upload/").getAbsolutePath();

		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		String saveDir = Paths.get("upload", today).toString(); // DB에 저장할 상대 경로 (upload/yyyy/MM/dd)

		File folder = new File(uploadPath, saveDir);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String originalFilename = multipartFile.getOriginalFilename();
		String extension = StringUtils.getFilenameExtension(originalFilename);
		String saveName = UUID.randomUUID().toString() + "." + extension;

		String savedPath = Paths.get(uploadPath, saveDir, saveName).toString();
		multipartFile.transferTo(new File(savedPath));

		return ItemImageDTO.builder()
			.orgName(originalFilename)
			.saveName(saveName)
			.saveDir("/" + saveDir.replace(File.separator, "/")) // 웹 경로 형식으로 변환
			.build();
	}
}
