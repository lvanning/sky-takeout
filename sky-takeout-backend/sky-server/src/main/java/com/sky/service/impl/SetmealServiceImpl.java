package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

        private final SetmealMapper setmealMapper;
        private final SetmealDishMapper setmealDishMapper;
        private final DishMapper dishMapper;

        public SetmealServiceImpl(SetmealMapper setmealMapper, SetmealDishMapper setmealDishMapper, DishMapper dishMapper) {
                this.setmealMapper = setmealMapper;
                this.setmealDishMapper = setmealDishMapper;
                this.dishMapper = dishMapper;
        }

        /**
         * 新增套餐，同时需要保存套餐和菜品的关联关系
         * @param setmealDTO
         */
        @Transactional
        @Override
        public void saveWithDish(SetmealDTO setmealDTO) {
                //向套餐表插入数据
                Setmeal setmeal = new Setmeal();
                BeanUtils.copyProperties(setmealDTO, setmeal);
                setmealMapper.insert(setmeal);

                //获取生成的套餐id
                Long setmealId = setmeal.getId();

                //保存套餐和菜品的关联关系
                List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
                if (setmealDishes != null && !setmealDishes.isEmpty()) {
                        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
                        setmealDishMapper.insertBatch(setmealDishes);
                }
        }

        /**
         * 分页查询套餐数据
         * @param setmealPageQueryDTO
         * @return
         */
        @Override
        public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
                PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
                Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
                return new PageResult(page.getTotal(), page.getResult());
        }

        /**
         * 批量删除套餐
         * @param ids
         */
        @Transactional
        @Override
        public void deleteBatch(List<Long> ids) {
                //起售中的套餐不能删除
                ids.forEach(id -> {
                        Setmeal setmeal = setmealMapper.getById(id);
                        if (StatusConstant.ENABLE.equals(setmeal.getStatus())) {
                                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
                        }
                });

                ids.forEach(id -> {
                        //删除套餐表中的数据
                        setmealMapper.deleteById(id);
                        //删除套餐菜品关系表中的数据
                        setmealDishMapper.deleteBySetmealId(id);
                });
        }

        /**
         * 根据id查询套餐和关联的菜品数据
         * @param id
         * @return
         */
        @Override
        public SetmealVO getByIdWithDish(Long id) {
                //查询套餐
                Setmeal setmeal = setmealMapper.getById(id);
                //查询套餐关联的菜品
                List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
                //封装
                SetmealVO setmealVO = new SetmealVO();
                BeanUtils.copyProperties(setmeal, setmealVO);
                setmealVO.setSetmealDishes(setmealDishes);
                return setmealVO;
        }

        /**
         * 修改套餐
         * @param setmealDTO
         */
        @Transactional
        @Override
        public void update(SetmealDTO setmealDTO) {
                //1、修改套餐表
                Setmeal setmeal = new Setmeal();
                BeanUtils.copyProperties(setmealDTO, setmeal);
                setmealMapper.update(setmeal);

                Long setmealId = setmealDTO.getId();

                //2、删除套餐和菜品的关联关系
                setmealDishMapper.deleteBySetmealId(setmealId);

                //3、重新插入套餐和菜品的关联关系
                List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
                if (setmealDishes != null && !setmealDishes.isEmpty()) {
                        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
                        setmealDishMapper.insertBatch(setmealDishes);
                }
        }

        /**
         * 套餐起售、停售
         * @param status
         * @param id
         */
        @Override
        public void startOrStop(Integer status, Long id) {
                //起售套餐时，判断套餐内是否有停售菜品，有停售菜品则不能起售
                if (StatusConstant.ENABLE.equals(status)) {
                        List<Dish> dishList = dishMapper.getBySetmealId(id);
                        if (dishList != null && !dishList.isEmpty()) {
                                dishList.forEach(dish -> {
                                        if (StatusConstant.DISABLE.equals(dish.getStatus())) {
                                                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                                        }
                                });
                        }
                }

                Setmeal setmeal = Setmeal.builder()
                        .id(id)
                        .status(status)
                        .build();
                setmealMapper.update(setmeal);
        }
        
        /**
         * 条件查询
         * @param setmeal
         * @return
         */
        public List<Setmeal> list(Setmeal setmeal) {
                List<Setmeal> list = setmealMapper.list(setmeal);
                return list;
        }
        
        /**
         * 根据id查询菜品选项
         * @param id
         * @return
         */
        public List<DishItemVO> getDishItemById(Long id) {
                return setmealMapper.getDishItemBySetmealId(id);
        }
}
