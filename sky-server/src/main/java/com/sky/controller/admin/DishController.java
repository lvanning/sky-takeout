package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "菜品管理")
@RestController
@RequestMapping("/admin/dish")
public class DishController {
        
        private DishService dishService;
        
        public DishController(DishService dishService) {
                this.dishService = dishService;
        }
        
        /**
         * 新增菜品和对应口味
         * @param dishDTO
         * @return
         */
        @PostMapping
        @Operation(summary = "新增菜品")
        private Result save(@RequestBody DishDTO dishDTO){
                log.info("新增菜品：{}", dishDTO);
                dishService.saveWithFlavor(dishDTO);
                return Result.success();
        }
}
