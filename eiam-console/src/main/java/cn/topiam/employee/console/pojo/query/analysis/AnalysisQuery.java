/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.analysis;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import static cn.topiam.employee.support.constant.EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 统计查询入惨
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/22 23:16
 */
@Data
@Schema(description = "统计查询入惨")
public class AnalysisQuery implements Serializable {

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    @Schema(description = "开始日期")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime startTime;

    /**
     * 截止日期
     */
    @NotNull(message = "截止日期不能为空")
    @Schema(description = "截止日期")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime endTime;

    public Interval getTimeInterval() {
        Period interval = Period.between(startTime.toLocalDate(), endTime.toLocalDate());
        boolean today = interval.getDays() < 1;
        boolean isDay = interval.getMonths() < 1;
        return today ? Interval.HOUR : isDay ? Interval.DAY : Interval.MONTH;
    }

    @Getter
    public enum Interval {
                          /**
                           * HOUR
                           */
                          HOUR("hour", "%H时"),
                          /**
                           * DAY
                           */
                          DAY("day", "%d日"),
                          /**
                           * MONTH
                           */
                          MONTH("month", "%m月");

        private final String type;
        private final String format;

        Interval(String type, String format) {
            this.type = type;
            this.format = format;
        }
    }
}
