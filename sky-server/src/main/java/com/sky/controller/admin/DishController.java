package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api("菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    @ApiOperation("菜品上传")
    @PostMapping
    public Result saveDish(@RequestBody  DishDTO dishDTO){
        log.info("开始上传菜品：{}",dishDTO);
        dishService.saveWithFlavour(dishDTO);

        return Result.success();
    }
}
