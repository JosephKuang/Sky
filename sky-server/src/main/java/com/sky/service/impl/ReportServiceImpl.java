package com.sky.service.impl;

import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;


    @Override
    public TurnoverReportVO getTurnoverStatics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();

        for (int i = 0; !begin.plusDays(i).isAfter(end); i++) {
            LocalDate current = begin.plusDays(i);
         Double turnover =  reportMapper.getTurnoverStaticsByDate(current);
         turnover = turnover == null? 0.0: turnover;

            // 拼接日期和分隔符
            dateList.add(current);
            turnoverList.add(turnover);
        }
            // 去掉最后一个逗号

        log.info("时间：{}",dateList);
        log.info("营业额：{}",turnoverList);
        return  new TurnoverReportVO(StringUtils.join(dateList,","), StringUtils.join(turnoverList,","));
    }
}
