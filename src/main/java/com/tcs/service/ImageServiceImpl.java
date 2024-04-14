package com.tcs.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.domain.Image;
import com.tcs.repository.ImageRepository;

@Service
public class ImageServiceImpl {
	
	@Autowired
    private ImageRepository imageRepository;

    public Image saveImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg")) {
            throw new IOException("Invalid file type");
        }
        Image image = new Image();
        image.setName(fileName);
        image.setImgData(file.getBytes());
        return imageRepository.save(image);
    }

    public Image getImage(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }


}
