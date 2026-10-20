package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@Slf4j
@RequestMapping("/user/shop")
@Tag(name = "店铺相关接口")
public class ShopController {
        public static final String KEY = "SHOP_STATUS";
        private RedisTemplate redisTemplate;
        public ShopController(RedisTemplate redisTemplate){
                this.redisTemplate = redisTemplate;
        }
        
        /**
         * 获取店铺状态
         * @return
         */
        @GetMapping("/status")
        @Operation(summary = "获取店铺状态")
        public Result<Integer> getStatus(){
                Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
                log.info("获取店铺状态为: {}", status == 1 ? "营业中" : "打烊中");
                return Result.success(status);
        }
}
