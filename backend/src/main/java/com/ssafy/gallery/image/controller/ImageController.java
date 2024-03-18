package com.ssafy.gallery.image.controller;

import com.ssafy.gallery.image.model.CreateImage;
import com.ssafy.gallery.image.model.ImageOption;
import com.ssafy.gallery.image.model.SEX;
import com.ssafy.gallery.image.service.ImageService;
import com.ssafy.pjt.grpc.Image;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "이미지", description = "Image API")
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {
    private static final Logger logger = LogManager.getLogger(ImageController.class);
    private final ImageService imageService;

    // Login한 사용자의 UserId값이 있어야 JPA로 DB에 데이터 넘길 수 있음.
    @PostMapping("/create")
    public ResponseEntity<CreateImage> createImage(
            @RequestParam(value = "originalImage") MultipartFile originalImage,
            @RequestParam(value = "background") String background,
            @RequestParam(value = "suit") String suit,
            @RequestParam(value = "hair") String hair,
            @RequestParam(value = "sex") Image.SEX sex

    ) throws Exception {

        try{
//            System.out.println("com");

            CreateImage response = imageService.sendImage(originalImage,new ImageOption(background,suit,hair,sex));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
                    .body(response);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                    .body(null);
        }
    }

    @PostMapping("delete")
    public ResponseEntity deleteImage(String Image){
        return ResponseEntity.ok().build();
    }

    @PostMapping("getImage")
    public ResponseEntity getImage(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("getImages")
    public ResponseEntity<List> getImages(){
        return ResponseEntity.ok().build();
    }

}



