package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
        private final SetmealDishMapper setmealDishMapper;
        
        public DishServiceImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper, SetmealDishMapper setmealDishMapper) {
                this.dishMapper = dishMapper;
                this.dishFlavorMapper = dishFlavorMapper;
                this.setmealDishMapper = setmealDishMapper;
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
        
        /**
         * 菜品分页查询
         * @param dishPageQueryDTO
         * @return
         */
        @Override
        public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
                PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
                Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
                return new PageResult(page.getTotal(), page.getResult());
        }
        
        /**
         * 批量删除菜品
         * @param ids
         */
        @Transactional
        @Override
        public void deleteBatch(List<Long> ids) {
                //判断是否能删除（是否起售）
                for(Long id : ids) {
                        Dish dish = dishMapper.getById(id);
                        if(dish.getStatus() == StatusConstant.ENABLE) {
                                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
                        }
                }
                //是否被套餐关联
                List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
                if(setmealIds != null && !setmealIds.isEmpty()){
                        throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
                }
                //删除
                 for(Long id : ids){
                         dishMapper.deleteById(id);
                         //删除菜品口味
                         dishFlavorMapper.deleteByDishId(id);
                 }
       
                
        }
}
