package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;



    @ApiOperation("菜品上传")
    @PostMapping
    public Result saveDish(@RequestBody  DishDTO dishDTO){
        log.info("开始上传菜品：{}",dishDTO);
        dishService.saveWithFlavour(dishDTO);
        cleanCache("dish_"+dishDTO.getCategoryId() );


        return Result.success();
    }

    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult =  dishService.selectByPage(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("要删除菜品ids{}",ids);
        dishService.delete(ids);
        cleanCache("dish_*");


        return Result.success();
    }
    @PostMapping("/status/{status}")
    @ApiOperation("起售和停售")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        dishService.startOrStop(status,id);
        cleanCache("dish_*");



        return Result.success();
    }

    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> selectById(@PathVariable Long id){
       DishVO dishVo = dishService.getByIdWithFlavor(id);
        return Result.success(dishVo);
    }

    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        cleanCache("dish_*");

        return Result.success();
    }

private void cleanCache(String pattern){
    Set keys = redisTemplate.keys(pattern);
    redisTemplate.delete(keys);
}
}
