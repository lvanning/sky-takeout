package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserOrderController")
@RequestMapping("/user/order")
@Slf4j
@Tag(name = "C端订单接口")
public class OrderController {
        
        private OrderService orderService;
        
        public OrderController(OrderService orderService) {
                this.orderService = orderService;
        }
        
        /**
         *
         * 提交订单
         * @return
         */
        @PostMapping("/submit")
        @Operation(summary = "提交订单")
        public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO orderSubmitDTO) {
                log.info("提交订单: {}", orderSubmitDTO);
                OrderSubmitVO submitVO = orderService.submitOrder(orderSubmitDTO);
                return Result.success(submitVO);
        }
}
