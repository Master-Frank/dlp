/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.analysis;

import java.util.List;

import cn.topiam.employee.audit.repository.result.AuthnQuantityResult;
import cn.topiam.employee.audit.repository.result.AuthnZoneResult;
import cn.topiam.employee.console.pojo.query.analysis.AnalysisQuery;
import cn.topiam.employee.console.pojo.result.analysis.AppVisitRankResult;
import cn.topiam.employee.console.pojo.result.analysis.AuthnHotProviderResult;
import cn.topiam.employee.console.pojo.result.analysis.OverviewResult;

/**
 * 统计 service
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/22 22:25
 */
public interface AnalysisService {

    /**
     * 概述
     *
     * @return {@link OverviewResult}
     */
    OverviewResult overview();

    /**
     * 认证量统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List<AuthnQuantityResult>}
     */
    List<AuthnQuantityResult> authnQuantity(AnalysisQuery params);

    /**
     * 应用热点统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List<AppVisitRankResult>}
     */
    List<AppVisitRankResult> appVisitRank(AnalysisQuery params);

    /**
     * 热门认证方式统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List<AuthnHotProviderResult>}
     */
    List<AuthnHotProviderResult> authnHotProvider(AnalysisQuery params);

    /**
     * 登录区域统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List< AuthnZoneResult >}
     */
    List<AuthnZoneResult> authnZone(AnalysisQuery params);
}
