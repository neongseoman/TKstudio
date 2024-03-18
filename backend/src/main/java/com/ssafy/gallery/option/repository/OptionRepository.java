package com.ssafy.gallery.option.repository;

import com.ssafy.gallery.option.model.OptionStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<OptionStore, Integer> {
}
