package com.ssafy.gallery.image.controller;

import com.ssafy.gallery.image.model.ImageOption;
import com.ssafy.gallery.image.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    @DisplayName("Image Receive Test")
    public void createImageTest() throws Exception{
        ImageOption imageOption = new ImageOption("background","suit","hair");
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "originalImage",
                "test.jpg",
                "image/jpeg",
                "This is a test image".getBytes()
        );


//        Mockito.doNothing().when(imageService).sendImage(mockMultipartFile, imageOption);

        ResultActions actions =
                mockMvc.perform(multipart("/api/v1/image/create")
                        .file(mockMultipartFile)
                        .param("background", imageOption.getBackground())
                        .param("suit", imageOption.getSuit())
                        .param("hair", imageOption.getHair()))
                        .andExpect(status().isOk());


    }
}