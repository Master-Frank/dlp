/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.constant;

import cn.topiam.employee.support.constant.EiamConstants;

/**
 * 同步器常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/20 22:31
 */
public final class SynchronizerConstants {

    /**
     * 同步器路径
     */
    public final static String SYNCHRONIZER_PATH  = EiamConstants.V1_API_PATH + "/synchronizer";

    /**
     * 同步器事件接收路径
     */
    public final static String EVENT_RECEIVE_PATH = SYNCHRONIZER_PATH + "/event_receive";
}
