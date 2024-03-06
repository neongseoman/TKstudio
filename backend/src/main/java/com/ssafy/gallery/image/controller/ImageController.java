package com.ssafy.gallery.image.controller;


import com.ssafy.gallery.example.controller.ExampleApiController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지", description = "Image API")
@Log4j2
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    private static final Logger logger = LogManager.getLogger(ImageController.class);

    @PostMapping("/create")
    public String createImage(
            @RequestParam("image") MultipartFile image
            ){
            logger.info(image.getContentType());
            logger.info(image.getOriginalFilename());
            logger.info(image.getSize());

            return "image income";
    }
}
