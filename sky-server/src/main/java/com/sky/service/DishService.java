package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;


public interface DishService {

    void saveWithFlavour(DishDTO dishDTO);

    PageResult selectByPage(DishPageQueryDTO dishPageQueryDTO);
}
