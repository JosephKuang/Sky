package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    void save(ShoppingCart shoppingCart);

    List<ShoppingCart> list(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart shoppingCart);

    @Delete("delete  from shopping_cart where  user_id =#{userId}")
    void clean(Long userId);

    /**
     * 批量插入购物车数据
     *
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
