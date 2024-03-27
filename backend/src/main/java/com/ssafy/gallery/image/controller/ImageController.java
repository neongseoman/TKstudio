
package com.ssafy.gallery.image.controller;

import com.ssafy.gallery.image.model.*;
import com.ssafy.gallery.image.service.ImageService;
import com.ssafy.pjt.grpc.Image;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Tag(name = "이미지", description = "Image API")
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {
    private final ImageService imageService;

    // Login한 사용자의 UserId값이 있어야 JPA로 DB에 데이터 넘길 수 있음.
    @PostMapping("/create")
    public ResponseEntity<Resource> createImage(
            HttpServletRequest request,
            @RequestParam(value = "originalImage") MultipartFile originalImage,
            @RequestParam(value = "optionId") int optionId
    ) throws Exception {

        try {
            int id = (int) request.getAttribute("userId");

            CreateImageDto response = imageService.createImage(originalImage, optionId, id);

            return ResponseEntity.ok()
                    .header("imageInfoId", String.valueOf(response.getImageInfoId()))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                    .body(response.getResource());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                    .body(null);
        }
    }

    @GetMapping("delete/{imageInfoId}")
    public ResponseEntity deleteImage(HttpServletRequest request, @PathVariable int imageInfoId) {
        int id = (int) request.getAttribute("userId");

        imageService.deleteImage(imageInfoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("getImageInfos")
    public ResponseEntity<Map<Integer, ImageInfoDTO>> getImageInfos(HttpServletRequest request) {
        int id = (int) request.getAttribute("userId");

        List<ImageInfoDTO> imageInfoList = imageService.getImageInfos(id);
        Map<Integer, ImageInfoDTO> responseDTO = imageInfoList.stream()
                .collect(Collectors.toMap(
                        ImageInfoDTO::getImageInfoId,
                        Function.identity()));

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getImage/originalImage/{imageInfoId}")
    public ResponseEntity<Resource> getOriginalImage(HttpServletRequest request, @PathVariable String imageInfoId) throws Exception {
        int id = (int) request.getAttribute("userId");

        log.info("getOriginalImage id : " + imageInfoId);
        Resource image = imageService.getOriginalImage(imageInfoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(image);
    }

    @GetMapping("getImage/processedImage/{imageInfoId}")
    public ResponseEntity<Resource> getProcessedImage(HttpServletRequest request, @PathVariable String imageInfoId) throws Exception {
        int id = (int) request.getAttribute("userId");

        Resource image = imageService.getProcessedImage(imageInfoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(image);

    }

    @GetMapping("getImage/thumbnailImage/{imageInfoId}")
    public ResponseEntity<Resource> getThumbnailImage(HttpServletRequest request, @PathVariable String imageInfoId) throws Exception {
        int id = (int) request.getAttribute("userId");

        log.info("getOriginalImage id : " + imageInfoId);
        Resource image = imageService.getThumbnailImage(imageInfoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(image);
    }
}