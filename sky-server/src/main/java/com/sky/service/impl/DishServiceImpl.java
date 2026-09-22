package com.sky.service.impl;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Tag(name = "菜品管理")
public class DishServiceImpl implements DishService {
        
        private final DishMapper dishMapper;
        private final DishFlavorMapper dishFlavorMapper;
        
        public DishServiceImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper) {
                this.dishMapper = dishMapper;
                this.dishFlavorMapper = dishFlavorMapper;
        }
        
        /**
         * 新增菜品和对应口味
         * @param dishDTO
         */
        @Transactional
        @Override
        public void saveWithFlavor(DishDTO dishDTO) {
                
                //插入一条菜品信息
                Dish dish = new Dish();
                BeanUtils.copyProperties(dishDTO, dish);
                dishMapper.insert(dish);
                
                Long dishId = dish.getId();
                
                //插入N条菜品口味
                List<DishFlavor> flavors = dishDTO.getFlavors();
                if(flavors != null && !flavors.isEmpty()){
                        flavors.forEach(flavor->{
                                flavor.setDishId(dishId);
                        });
                        dishFlavorMapper.insertBatch(flavors);
                }
        }
}
