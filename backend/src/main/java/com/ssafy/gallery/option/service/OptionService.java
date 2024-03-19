package com.ssafy.gallery.option.service;

import com.ssafy.gallery.option.model.OptionBuyLog;
import com.ssafy.gallery.option.model.OptionStore;
import com.ssafy.gallery.option.repository.OptionBuyLogRepository;
import com.ssafy.gallery.option.repository.OptionStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionStoreRepository optionStoreRepository;
    private final OptionBuyLogRepository optionBuyLogRepository;

    public List<OptionStore> getList() {
        return optionStoreRepository.findAll();
    }

    public void buyOption(int userId, int optionId) {
        OptionBuyLog optionBuyLog = OptionBuyLog.builder().userId(userId).optionId(optionId).build();
        optionBuyLogRepository.save(optionBuyLog);
    }
}
