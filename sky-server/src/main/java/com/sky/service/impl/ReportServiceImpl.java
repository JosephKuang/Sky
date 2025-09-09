package com.sky.service.impl;

import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();

        for (int i = 0; !begin.plusDays(i).isAfter(end); i++) {
            LocalDate current = begin.plusDays(i);
            Double turnover = reportMapper.getTurnoverStaticsByDate(current);
            turnover = turnover == null ? 0.0 : turnover;

            // 拼接日期和分隔符
            dateList.add(current);
            turnoverList.add(turnover);
        }
        // 去掉最后一个逗号

        log.info("时间：{}", dateList);
        log.info("营业额：{}", turnoverList);
        return new TurnoverReportVO(StringUtils.join(dateList, ","), StringUtils.join(turnoverList, ","));
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (int i = 0; !begin.plusDays(i).isAfter(end); i++) {
            LocalDate current = begin.plusDays(i);
            LocalDateTime beginTime = LocalDateTime.of(current, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(current,LocalTime.MAX);
            Map map = new HashMap();
            map.put("endTime",endTime);
            int userCount = userMapper.getUserCount(map);
            map.put("beginTime",beginTime);
            int newUserCount = userMapper.getNewUserCount(map);
            dateList.add(current);
            newUserList.add(newUserCount);
            totalUserList.add(userCount);
        }
        log.info("时间：{}", dateList);
        log.info("用户数量：{}", totalUserList);
        log.info("新增用户：{}",newUserList);

        return new UserReportVO(StringUtils.join(dateList, ","),
                StringUtils.join(totalUserList, ","),
                StringUtils.join(newUserList,","));
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        for (int i = 0; !begin.plusDays(i).isAfter(end); i++) {
            LocalDate current  = begin.plusDays(i);
            LocalDateTime beginTime = LocalDateTime.of(current,LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(current,LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            int validOrderCount = reportMapper.getVaildOrderCount(map);
            int orderCount = reportMapper.getorderCount(map);
            validOrderCountList.add(validOrderCount);
            orderCountList.add(orderCount);
            dateList.add(current);
        }

        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        //计算订单完成率
     Double  orderCompletionRate = totalOrderCount==0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;
        log.info("时间：{}",dateList);
        log.info("有效订单列表：{}",validOrderCountList);
        log.info("订单总数列表：{}",orderCountList);
        log.info("订单总数：{}",totalOrderCount);
        log.info("有效订单总数：{}",validOrderCount);


        return new OrderReportVO(StringUtils.join(dateList, ","),
                StringUtils.join(orderCountList, ","),
                StringUtils.join(validOrderCountList,","),totalOrderCount,validOrderCount,orderCompletionRate);
    }
}
