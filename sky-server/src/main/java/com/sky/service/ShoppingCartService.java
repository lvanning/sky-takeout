package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
        
        /**
         * 添加购物车
         * @param shoppingCartDTO
         */
        public void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
        
        /**
         * 查看购物车
         * @return
         */
        public List<ShoppingCart> listShoppingCart();
        
        /**
         * 清空购物车
         */
        public void cleanShoppingCart();
        
        /**
         * 减少购物车数量
         * @param shoppingCartDTO
         */
        void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
