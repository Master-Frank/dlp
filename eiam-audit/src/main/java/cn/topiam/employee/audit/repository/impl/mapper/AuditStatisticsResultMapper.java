/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.repository.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import cn.topiam.employee.audit.repository.result.AuditStatisticsResult;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/10/04 22:25
 */
@SuppressWarnings("DuplicatedCode")
public class AuditStatisticsResultMapper implements RowMapper<AuditStatisticsResult> {
    /**
     * Implementations must implement this method to map each row of data
     * in the ResultSet. This method should not call {@code next()} on
     * the ResultSet; it is only supposed to map values of the current row.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return the result object for the current row (may be {@code null})
     * @throws SQLException if an SQLException is encountered getting
     *                      column values (that is, there's no need to catch SQLException)
     */
    @Override
    public AuditStatisticsResult mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        //@formatter:off
        AuditStatisticsResult user = new AuditStatisticsResult();
        if (StringUtils.isNoneBlank(rs.getString("key_"))) {
            user.setKey(rs.getString("key_").replace("\"", ""));
        }
        user.setCount(rs.getLong("count_"));
        //@formatter:on
        return user;
    }
}
