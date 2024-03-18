package com.ssafy.gallery.option.service;

import com.ssafy.gallery.option.model.OptionStore;
import com.ssafy.gallery.option.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepository optionRepository;

    public List<OptionStore> getList() {
        return optionRepository.findAll();
    }
}
