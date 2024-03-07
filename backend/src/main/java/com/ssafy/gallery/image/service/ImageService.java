package com.ssafy.gallery.image.service;

import com.ssafy.gallery.image.model.ImageOption;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class ImageService {
    private static final Logger logger = LogManager.getLogger(ImageService.class);

    public void sendImageToAIServer(MultipartFile image, ImageOption imageOption){
        logger.info(imageOption.toString());
    }
}
