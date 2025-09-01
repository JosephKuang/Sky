package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {

    void saveWithFlavour(DishDTO dishDTO);

    PageResult selectByPage(DishPageQueryDTO dishPageQueryDTO);

    void delete(List<Long> ids);

    void startOrStop(Integer status, Long id);


    /**
     * 根据id查询菜品和对应口味数据
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}


