package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
        /**
         * 动态查询购物车列表
         * @param shoppingCart
         * @return
         */
        List<ShoppingCart> list(ShoppingCart shoppingCart);

        /**
         * 根据ID更新购物车数量
         * @param existingCart
         */
        @Update("update shopping_cart set number = #{number} where id = #{id}")
        void updateNumberById(ShoppingCart existingCart);
        
        /**
         * 插入购物车记录
         * @param shoppingCart
         */
        @Insert("insert into shopping_cart(user_id, dish_id, setmeal_id, name, image, dish_flavor, amount, number, create_time) " +
                        "values(#{userId}, #{dishId}, #{setmealId}, #{name}, #{image}, #{dishFlavor}, #{amount}, #{number}, #{createTime})")
        void insert(ShoppingCart shoppingCart);
        
        @Delete("delete from shopping_cart where user_id = #{userId}")
        void deleteByUserId(Long userId);
        
        /**
         * 根据菜品ID或套餐ID删除购物车记录
         * @param existingCart
         */
        void deleteByDishOrSetmealId(ShoppingCart existingCart);

        /**
         * 批量插入购物车数据
         * @param shoppingCartList
         */
        void insertBatch(List<ShoppingCart> shoppingCartList);
}
