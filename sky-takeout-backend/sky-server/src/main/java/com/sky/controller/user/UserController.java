package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
@Tag(name = "C端用户接口")
public class UserController {
        
        private final JwtProperties jwtProperties;
        private UserService userService;
        public UserController(UserService userService, JwtProperties jwtProperties) {
                this.userService = userService;
                this.jwtProperties = jwtProperties;
        }
        
        /**
         * 用户登录
         * @param userLoginDTO
         * @return
         */
        @PostMapping("/login")
        @Operation(summary = "用户登录")
        public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
                 log.info("用户登录，userLoginDTO:{}", userLoginDTO.getCode());
                
                User user = userService.wxLogin(userLoginDTO);
                Map<String, Object> claims = Map.of(JwtClaimsConstant.USER_ID, user.getId());
                String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
                UserLoginVO userLoginVO = UserLoginVO.builder()
                                        .id(user.getId())
                                        .openid(user.getOpenid())
                                        .token(token)
                                        .build();
                return Result.success(userLoginVO);
        }
}
