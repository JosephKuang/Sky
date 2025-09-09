package com.sky.mapper;


import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {
    @Select("SELECT SUM(amount)\n" +
            "FROM orders\n" +
            "WHERE status = 5\n" +
            "  AND order_time >= #{current}\n" +
            "  AND order_time < DATE_ADD(#{current}, INTERVAL 1 DAY);")
    Double getTurnoverStaticsByDate(LocalDate current);


    int getVaildOrderCount(Map map);

    int getorderCount(Map map);


    List<GoodsSalesDTO> getTop10(Map map);
}
