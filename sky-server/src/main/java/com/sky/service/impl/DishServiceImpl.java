package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper1;
    @Autowired
    private DishMapper dishMapper2;


    @Override
    @Transactional
    public void saveWithFlavour(DishDTO dishDTO) {
        //向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper1.saveDish(dish);
        log.info("上传菜品:{}", dish);
        Long dishId = dish.getId();
        //向口味表插入n条数据
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if(dishFlavors!=null&&dishFlavors.size()>0){
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishMapper2.insertBatch(dishFlavors);
        }




        dishMapper2.insertBatch(dishFlavors);
    }

    @Override
    public PageResult selectByPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper1.selectByPage(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());


    }
}
