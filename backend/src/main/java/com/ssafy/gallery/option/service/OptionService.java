package com.ssafy.gallery.option.service;

import com.ssafy.gallery.option.model.OptionBuyLog;
import com.ssafy.gallery.option.model.OptionCategory;
import com.ssafy.gallery.option.model.OptionStore;
import com.ssafy.gallery.option.repository.OptionBuyLogRepository;
import com.ssafy.gallery.option.repository.OptionCategoryRepository;
import com.ssafy.gallery.option.repository.OptionStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OptionService {
    private final OptionStoreRepository optionStoreRepository;
    private final OptionBuyLogRepository optionBuyLogRepository;
    private final OptionCategoryRepository optionCategoryRepository;

    @Cacheable(cacheNames = "optionList",cacheManager = "cacheManager")
    public List<OptionStore> getList() {
        return optionStoreRepository.findAll();
    }

    @Cacheable(cacheNames = "optionCategory", cacheManager = "cacheManager")
    public List<OptionCategory> getCategory() {
        return optionCategoryRepository.findAll();
    }

    @Cacheable(cacheNames = "buyOption", key = "#userId", condition = "#userId != null", cacheManager = "cacheManager")
    public List<OptionBuyLog> getBuyOptionList(int userId) {
        return optionBuyLogRepository.findAllByUserId(userId);
    }

    @CacheEvict(cacheNames = "buyOption", key = "#userId", cacheManager = "cacheManager")
    public void buyOption(int userId, int optionId) {
        OptionBuyLog optionBuyLog = OptionBuyLog.builder().userId(userId).optionId(optionId).build();
        optionBuyLogRepository.save(optionBuyLog);
    }
}
