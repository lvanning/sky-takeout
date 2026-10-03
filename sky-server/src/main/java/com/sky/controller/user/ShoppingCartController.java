package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Tag(name = "C端-购物车接口")
public class ShoppingCartController {
        private ShoppingCartService shoppingCartService;
        public ShoppingCartController(ShoppingCartService shoppingCartService) {
                this.shoppingCartService = shoppingCartService;
        }

        @PostMapping("/add")
        @Operation(summary = "添加购物车")
        public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
                log.info("添加购物车: {}", shoppingCartDTO);
                shoppingCartService.addShoppingCart(shoppingCartDTO);
                return Result.success();
        }
        
        @GetMapping("/list")
        @Operation(summary = "查询购物车")
        public Result list() {
                log.info("查询购物车");
                List<ShoppingCart> shoppingCarts = shoppingCartService.listShoppingCart();
                return Result.success(shoppingCarts);
        }
        
        /**
         * 清空购物车
         * @return
         */
        @DeleteMapping("/clean")
        @Operation(summary = "清空购物车")
        public Result clean() {
                log.info("清空购物车");
                shoppingCartService.cleanShoppingCart();
                return Result.success();
        }
        
        /**
         * 减少购物车数量
         * @param shoppingCartDTO
         * @return
         */
        @PostMapping("/sub")
        @Operation(summary = "减少购物车数量")
        public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
                log.info("减少购物车: {}", shoppingCartDTO);
                shoppingCartService.subShoppingCart(shoppingCartDTO);
                return Result.success();
        }
}
