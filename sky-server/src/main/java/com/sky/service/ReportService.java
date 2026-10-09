package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {
        
        /**
         * 获取营业额统计图表数据
         * @param begin
         * @param end
         * @return
         */
        public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
