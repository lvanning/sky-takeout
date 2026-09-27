package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        
        /**
         * 菜品分页查询
         * @param dishPageQueryDTO
         * @return
         */
        @GetMapping("/page")
        @Operation(summary = "菜品分页查询")
        public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO){
                log.info("菜品分页查询：{}", dishPageQueryDTO);
                PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
                return Result.success(pageResult);
        }
        
        /**
         * 删除菜品
         * @param ids
         * @return
         */
        @DeleteMapping
        @Operation(summary = "删除菜品")
        public Result delete(@RequestParam List<Long> ids){
                log.info("删除菜品：{}", ids);
                dishService.deleteBatch(ids);
                return Result.success();
        }
        
        /**
         * 根据id查询菜品
         * @param id
         * @return
         */
        @GetMapping("/{id}")
        @Operation(summary = "根据id查询菜品")
        public Result<DishVO> getById(@PathVariable Long id){
                log.info("根据id查询菜品：{}", id);
                DishVO dishVO = dishService.getByIdWithFlavor(id);
                return Result.success(dishVO);
        }
        
        @PutMapping
        @Operation(summary = "修改菜品")
        public Result update(@RequestBody DishDTO dishDTO){
                log.info("修改菜品：{}", dishDTO);
                dishService.updateWithFlavor(dishDTO);
                return Result.success();
        }
        
        @PostMapping("/status/{status}")
        @Operation(summary = "修改菜品状态（起售停售）")
        public Result updateStatus(@PathVariable Integer status, @RequestParam Long id){
                log.info("修改菜品状态：{}", status);
                dishService.updateStatus(status, id);
                return Result.success();
        }

        /**
         * 根据分类id查询菜品
         * @param categoryId
         * @return
         */
        @GetMapping("/list")
        @Operation(summary = "根据分类id查询菜品")
        public Result<List<Dish>> list(Long categoryId){
                log.info("根据分类id查询菜品：{}", categoryId);
                List<Dish> list = dishService.list(categoryId);
                return Result.success(list);
        }
}
