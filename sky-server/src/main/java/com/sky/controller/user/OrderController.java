package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
        
        /**
         * 订单支付
         *
         * @param ordersPaymentDTO
         * @return
         */
        @PutMapping("/payment")
        @Operation(summary = "订单支付")
        public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
                log.info("订单支付：{}", ordersPaymentDTO);
                OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
                log.info("生成预支付交易单：{}", orderPaymentVO);
                return Result.success(orderPaymentVO);
        }

        /**
         * 历史订单查询
         *
         * @param page
         * @param pageSize
         * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
         * @return
         */
        @GetMapping("/historyOrders")
        @Operation(summary = "历史订单查询")
        public Result<PageResult> page(int page, int pageSize, Integer status) {
                log.info("历史订单查询：page={}, pageSize={}, status={}", page, pageSize, status);
                PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);
                return Result.success(pageResult);
        }

        /**
         * 查询订单详情
         *
         * @param id
         * @return
         */
        @GetMapping("/orderDetail/{id}")
        @Operation(summary = "查询订单详情")
        public Result<OrderVO> details(@PathVariable("id") Long id) {
                OrderVO orderVO = orderService.details(id);
                return Result.success(orderVO);
        }

        /**
         * 用户取消订单
         *
         * @return
         */
        @PutMapping("/cancel/{id}")
        @Operation(summary = "取消订单")
        public Result cancel(@PathVariable("id") Long id) throws Exception {
                log.info("用户取消订单：{}", id);
                orderService.userCancelById(id);
                return Result.success();
        }

        /**
         * 再来一单
         *
         * @param id
         * @return
         */
        @PostMapping("/repetition/{id}")
        @Operation(summary = "再来一单")
        public Result repetition(@PathVariable Long id) {
                log.info("再来一单：{}", id);
                orderService.repetition(id);
                return Result.success();
        }

}
