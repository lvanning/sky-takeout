package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

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
}
