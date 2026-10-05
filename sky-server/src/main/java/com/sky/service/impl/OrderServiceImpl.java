package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
        private final AddressBookMapper addressBookMapper;
        private final OrderMapper orderMapper;
        private final OrderDetailMapper orderDetailMapper;
        private final ShoppingCartMapper shoppingCartMapper;
        public OrderServiceImpl(OrderMapper orderMapper, OrderDetailMapper orderDetailMapper, ShoppingCartMapper shoppingCartMapper, AddressBookMapper addressBookMapper) {
                this.orderMapper = orderMapper;
                this.orderDetailMapper = orderDetailMapper;
                this.shoppingCartMapper = shoppingCartMapper;
                this.addressBookMapper = addressBookMapper;
        }
        
        /**
         * 提交订单
         * @param orderSubmitDTO
         * @return
         */
        @Transactional
        @Override
        public OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO) {
                //处理各种业务异常（如：地址不存在、购物车为空）
                AddressBook addressBook = addressBookMapper.getById(orderSubmitDTO.getAddressBookId());
                if(addressBook == null){
                        throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
                }
                
                //查询购物车数据
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setUserId(BaseContext.getCurrentId());
                List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
                if(shoppingCarts == null || shoppingCarts.isEmpty()){
                        throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
                }
                //向订单表插入1条数据
                Orders orders = new Orders();
                BeanUtils.copyProperties(orderSubmitDTO, orders);
                orders.setOrderTime(LocalDateTime.now());
                orders.setPayStatus(Orders.UN_PAID);
                orders.setStatus(Orders.PENDING_PAYMENT);
                orders.setNumber(String.valueOf(System.currentTimeMillis()));
                orders.setPhone(addressBook.getPhone());
                orders.setConsignee(addressBook.getConsignee());
                orders.setUserId(BaseContext.getCurrentId());
                
                orderMapper.insert(orders);
                //向订单明细表插入多条数据
                List<OrderDetail> orderDetailList = new ArrayList<>();
                for(ShoppingCart cart : shoppingCarts){
                        OrderDetail orderDetail = new OrderDetail();
                        BeanUtils.copyProperties(cart, orderDetail);
                        orderDetail.setOrderId(orders.getId());
                        orderDetailList.add(orderDetail);
                }
                orderDetailMapper.insertBatch(orderDetailList);
                //清空购物车数据
                shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
                //封装VO对象返回
                OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .build();
                return orderSubmitVO;
        }
}
