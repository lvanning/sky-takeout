package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Tag(name = "C端-分类接口")
public class DishController {
        private final DishService dishService;
        private final RedisTemplate redisTemplate;
        
        public DishController(DishService dishService, RedisTemplate redisTemplate) {
                this.dishService = dishService;
                this.redisTemplate = redisTemplate;
        }
        
        /**
         * 根据分类id查询菜品
         * @param categoryId
         * @return
         */
        @GetMapping("/list")
        @Operation(summary = "根据分类id查询菜品")
        public Result<List<DishVO>> list(Long categoryId) {
                //构造redis key，规则：dish_分类id
                String redisKey = "dish_" + categoryId;
                //查询redis中是否存在菜品数据
                List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(redisKey);
                if (list != null && !list.isEmpty()) {
                        //如果redis中存在菜品数据，则直接返回
                        return Result.success(list);
                }
                
                //如果redis中不存在菜品数据，则查询数据库
                Dish dish = new Dish();
                dish.setCategoryId(categoryId);
                dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
                
                list = dishService.listWithFlavor(dish);
                redisTemplate.opsForValue().set(redisKey, list);//将查询到的菜品数据存入redis中
                
                return Result.success(list);
        }
        
}
