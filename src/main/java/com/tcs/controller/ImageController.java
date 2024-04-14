package com.tcs.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.domain.Image;
import com.tcs.service.ImageServiceImpl;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageServiceImpl imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            imageService.saveImage(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the image: " + e.getMessage());
        }
    }
/*
 //fetching all details

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImage(@PathVariable Long id) {
        Image image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image);
    }
      */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        // Determine the content type from the image file name
        String contentType = image.getName().endsWith(".png") ? "image/png" : "image/jpeg";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(contentType));
        headers.setContentLength(image.getImgData().length);
        
        return ResponseEntity.ok().headers(headers).body(image.getImgData());
    }

/*
    @GetMapping("/")
    public ResponseEntity<List<byte[]>> getAllImages() {
        // Fetch all images using the service layer
        List<Image> allImages = imageService.getAllImages();
        
        // Transform list of Image objects into a list of byte arrays
        List<byte[]> imageData = allImages.stream()
                                          .map(Image::getImgData)
                                          .collect(Collectors.toList());
        
        // Return the list of image data
        return ResponseEntity.ok(imageData);
    }
    
    */
    
    
    @GetMapping("/")
    public ResponseEntity<List<String>> getAllImages() {
        // Fetch all images using the service layer
        List<Image> allImages = imageService.getAllImages();

        // Transform list of Image objects into a list of Base64 encoded strings
        List<String> imageData = allImages.stream()
                                          .map(image -> Base64.getEncoder().encodeToString(image.getImgData()))
                                          .collect(Collectors.toList());

        // Return the list of Base64 encoded image data as a response entity with an OK status
        return ResponseEntity.ok(imageData);
}
}
