/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.topiam.employee.common.entity.account.UserDetailEntity;
import cn.topiam.employee.common.repository.account.UserDetailRepositoryCustomized;

/**
 * User Detail Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/29 21:27
 */
@Repository
public class UserDetailRepositoryCustomizedImpl implements UserDetailRepositoryCustomized {

    /**
     * 批量更新或新增
     *
     * @param data {@link List}
     */
    @Override
    public void batchSave(List<UserDetailEntity> data) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO eiam_user_detail (id_, user_id, id_type, id_card, website_,address_,create_by,create_time,update_by,update_time,remark_,is_deleted) values (?,?,?,?,?,?,?,?,?,?,?,?)",
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                    UserDetailEntity entity = data.get(i);
                    ps.setString(1, entity.getId());
                    ps.setString(2, entity.getUserId());
                    ps.setString(3,
                        Objects.isNull(entity.getIdType()) ? null : entity.getIdType().getCode());
                    ps.setString(4, entity.getIdCard());
                    ps.setString(5, entity.getWebsite());
                    ps.setString(6, entity.getAddress());
                    ps.setString(7, entity.getCreateBy());
                    ps.setTimestamp(8, Timestamp.valueOf(entity.getCreateTime()));
                    ps.setString(9, entity.getUpdateBy());
                    ps.setTimestamp(10, Timestamp.valueOf(entity.getUpdateTime()));
                    ps.setString(11, entity.getRemark());
                    ps.setBoolean(12, false);
                }

                @Override
                public int getBatchSize() {
                    return data.size();
                }
            });
    }

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    @Override
    public void batchUpdate(ArrayList<UserDetailEntity> list) {
        jdbcTemplate.batchUpdate(
            "UPDATE  eiam_user_detail SET user_id=?,id_type=?, id_card=?, website_=? ,address_=?,create_by=?,create_time=?,update_by=?,update_time=?,remark_=? WHERE id_=?",
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                    UserDetailEntity entity = list.get(i);
                    ps.setString(1, entity.getUserId());
                    ps.setString(2,
                        Objects.isNull(entity.getIdType()) ? null : entity.getIdType().getCode());
                    ps.setString(3, entity.getIdCard());
                    ps.setString(4, entity.getWebsite());
                    ps.setString(5, entity.getAddress());
                    ps.setString(6, entity.getCreateBy());
                    ps.setTimestamp(7, Timestamp.valueOf(entity.getCreateTime()));
                    ps.setString(8, entity.getUpdateBy());
                    ps.setTimestamp(9, Timestamp.valueOf(entity.getUpdateTime()));
                    ps.setString(10, entity.getRemark());
                    ps.setString(11, entity.getId());
                }

                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });
    }

    private final JdbcTemplate jdbcTemplate;

    public UserDetailRepositoryCustomizedImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
