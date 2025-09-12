/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.topiam.employee.audit.endpoint.pojo.AuditListQuery;
import cn.topiam.employee.audit.endpoint.pojo.AuditListResult;
import cn.topiam.employee.audit.endpoint.pojo.DictResult;
import cn.topiam.employee.audit.entity.AuditEntity;
import cn.topiam.employee.audit.event.type.EventType;
import cn.topiam.employee.audit.repository.AuditRepository;
import cn.topiam.employee.audit.service.AuditService;
import cn.topiam.employee.audit.service.converter.AuditDataConverter;
import cn.topiam.employee.support.exception.BadParamsException;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;
import cn.topiam.employee.support.security.userdetails.UserType;
import cn.topiam.employee.support.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import static cn.topiam.employee.support.security.userdetails.UserType.USER;

/**
 * 审计 service impl
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/10 23:06
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    /**
     * List
     *
     * @param query {@link AuditListQuery}
     * @param page {@link PageModel}
     * @return {@link Page}
     */
    @Override
    public Page<AuditListResult> getAuditList(AuditListQuery query, PageModel page) {
        if (USER.equals(SecurityUtils.getCurrentUser().getUserType())
            && !USER.getType().equals(query.getUserType())) {
            throw new BadParamsException("用户类型错误");
        }
        //查询入参转查询条件
        Specification<AuditEntity> specification = auditDataConverter
            .auditListRequestConvertToSpecification(query, page);
        if (Objects.isNull(specification)) {
            return new Page<>();
        }
        // 排序
        List<Sort.Order> orders = new ArrayList<>();
        for (PageModel.Sort sort : page.getSorts()) {
            orders.add(new Sort.Order((sort.getAsc() ? Sort.Direction.ASC : Sort.Direction.DESC),
                sort.getSorter()));
        }
        //分页条件
        PageRequest request = PageRequest.of(page.getCurrent(), page.getPageSize(),
            Sort.by(orders));
        //查询列表
        return auditDataConverter
            .entityConvertToAuditListResult(auditRepository.findAll(specification, request), page);
    }

    /**
     * 获取字典类型
     *
     * @param userType {@link String}
     * @return {@link List}
     */
    @Override
    public List<DictResult> getAuditDict(String userType) {
        List<EventType> types = Arrays.asList(EventType.values());
        //获取分组
        List<DictResult> list = types.stream().map(EventType::getResource).toList().stream()
            .distinct().toList().stream().map(resource -> {
                DictResult group = new DictResult();
                group.setName(resource.getName());
                group.setCode(resource.getCode());
                return group;
            }).collect(Collectors.toList());
        //处理每个分组的审计类型
        list.forEach(dict -> {
            Set<DictResult.AuditType> auditTypes = new LinkedHashSet<>();
            types.stream()
                .filter(auditType -> auditType.getResource().getCode().equals(dict.getCode()))
                .forEach(auditType -> {
                    if (auditType.getUserTypes().stream().map(UserType::getType).toList()
                        .contains(userType)) {
                        DictResult.AuditType type = new DictResult.AuditType();
                        type.setName(auditType.getDesc());
                        type.setCode(auditType.getCode());
                        auditTypes.add(type);
                    }
                });
            dict.setTypes(auditTypes);
        });
        list = list.stream().filter(i -> !CollectionUtils.isEmpty(i.getTypes()))
            .collect(Collectors.toList());
        return list;
    }

    /**
     * AuditDataConverter
     */
    private final AuditDataConverter auditDataConverter;

    /**
     * AuditRepository
     */
    private final AuditRepository    auditRepository;
}
