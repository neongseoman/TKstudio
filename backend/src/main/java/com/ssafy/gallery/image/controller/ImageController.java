//package com.ssafy.gallery.image.controller;
//
//import com.ssafy.gallery.image.model.*;
//import com.ssafy.gallery.image.service.ImageService;
//import com.ssafy.gallery.user.service.UserService;
//import com.ssafy.pjt.grpc.Image;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Tag(name = "이미지", description = "Image API")
//@Log4j2
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/image")
//public class ImageController {
//    private static final Logger logger = LogManager.getLogger(ImageController.class);
//    private final UserService userService;
//    private final ImageService imageService;
//
//    // Login한 사용자의 UserId값이 있어야 JPA로 DB에 데이터 넘길 수 있음.
//    @PostMapping("/create")
//    public ResponseEntity<Resource> createImage(
//            HttpServletRequest request,
//            @RequestParam(value = "originalImage") MultipartFile originalImage,
//            @RequestParam(value = "background") String background,
//            @RequestParam(value = "suit") String suit,
//            @RequestParam(value = "hair") String hair,
//            @RequestParam(value = "sex") Image.SEX sex
//
//    ) throws Exception {
//
//        try {
////            String id = (String) request.getAttribute("UserId");
////            log.info("UserId : " + id);
//            CreateImageDto response = imageService.createImage(originalImage, new ImageOption(background, suit, hair, sex));
//
//            return ResponseEntity.ok()
//                    .header("imageInfoId", String.valueOf(response.getImageInfoId()))
//                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
//                    .body(response.getResource());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest()
//                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
//                    .body(null);
//        }
//    }
//
//    @GetMapping("delete/{imageInfoId}")
//    public ResponseEntity deleteImage(HttpServletRequest request, @PathVariable int imageInfoId) {
////        String id = (String) request.getAttribute("UserId");
////        log.info("UserId : " + id);
//        imageService.deleteImage(imageInfoId);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("getImageInfos")
//    public ResponseEntity<Map<Integer,ImageInfoDTO>> getImageInfos(HttpServletRequest request) {
////        int userId = (String) request.getAttribute("UserId");
////        log.info("UserId : " + userId);
////        List<ImageInfoDTO> imageInfoList = imageService.getImageInfos(userId);
//        List<ImageInfoDTO> imageInfoList = imageService.getImageInfos(1);
//        Map<Integer, ImageInfoDTO> responseDTO = imageInfoList.stream()
//                .collect(Collectors.toMap(
//                        ImageInfoDTO::getImageInfoId,
//                        Function.identity()));
//
//        return ResponseEntity.ok().body(responseDTO);
//    }
//
//    @GetMapping("getImage/orignalImage/{imageInfoId}")
//    public ResponseEntity<Resource> getOriginalImage(HttpServletRequest request, @PathVariable int imageInfoId) {
////        String id = (String) request.getAttribute("UserId");
////        log.info("UserId : " + id);
////        Resource image = null;
////                imageService.getOriginalImage(imageInfoId);
//        return ResponseEntity.ok().body(image);
//    }
//
//    @GetMapping("getImage/processedImage/{imageInfoId}")
//    public ResponseEntity<Resource> getProcessedImage(HttpServletRequest request, @PathVariable int imageInfoId) {
////        String id = (String) request.getAttribute("UserId");
////        log.info("UserId : " + id);
//        Resource image = null;
////                imageService.getOriginalImage(imageInfoId);
//        return ResponseEntity.ok().body(image);
//    }
//
//
//}