package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface ReportMapper {
    @Select("SELECT SUM(amount)\n" +
            "FROM orders\n" +
            "WHERE status = 5\n" +
            "  AND order_time >= #{current}\n" +
            "  AND order_time < DATE_ADD(#{current}, INTERVAL 1 DAY);")
    Double getTurnoverStaticsByDate(LocalDate current);



}
