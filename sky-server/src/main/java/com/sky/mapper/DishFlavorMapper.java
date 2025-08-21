package com.sky.mapper;

import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> dishFlavors);


    void deleteByDishId(List<Long> dishIds);


    @Select("select * from dish_flavor where dish_id = #{dishId} ")
    List<DishFlavor> getByDishid(Long dishId);

    void update(List<DishFlavor> dishFlavors);
}
