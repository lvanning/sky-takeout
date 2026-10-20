package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

public interface ReportService {
        
        /**
         * 获取营业额统计图表数据
         * @param begin
         * @param end
         * @return
         */
        public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
        
        
        /**
         * 获取用户统计图表数据
         * @param begin
         * @param end
         * @return
         */
        public UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
        
        /**
         * 订单统计图表数据
         * @param begin
         * @param end
         * @return
         */
        OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);
        
        /**
         * 统计指定区间内的销量top10
         * @param begin
         * @param end
         * @return
         */
        SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
        
        /**
         * 导出运营数据报表
         * @param response
         */
        void exportBusinessData(HttpServletResponse response);
}
