package com.aihoo.domain.visit.util;

import com.aihoo.domain.visit.dto.VisitStatusVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/11 09:53
 */
public class VisitStatusUtil {

    public static final String WAIT = "WAIT";
    public static final String UNSUBMITTED = "UNSUBMITTED";
    public static final String SUBMITTED = "SUBMITTED";
    public static final String STARTED = "STARTED";
    public static final String ENDED = "ENDED";
    public static final String REFUND_PROCESSING = "REFUND_PROCESSING";
    public static final String REFUNDED = "REFUNDED";
    public static final String DONE = "DONE";

    private static final Map<String, List<String>> nextStatusMap = Maps.newHashMap();
    private static final Map<String, List<String>> prevStatusMap = Maps.newHashMap();

    static {
        initStatusFlow();
    }

    /**
     * 初始化状态流转关系
     */
    private static void initStatusFlow() {
        nextStatusMap.put(WAIT, Arrays.asList(UNSUBMITTED, DONE));
        nextStatusMap.put(UNSUBMITTED, Arrays.asList(SUBMITTED, REFUND_PROCESSING));
        nextStatusMap.put(SUBMITTED, Arrays.asList(STARTED, REFUND_PROCESSING));
        nextStatusMap.put(STARTED, Collections.singletonList(ENDED));
        nextStatusMap.put(ENDED, Collections.emptyList());
        nextStatusMap.put(REFUND_PROCESSING, Collections.singletonList(REFUNDED));
        nextStatusMap.put(REFUNDED, Collections.emptyList());
        nextStatusMap.put(DONE, Collections.emptyList());

        for (Map.Entry<String, List<String>> entry : nextStatusMap.entrySet()) {
            String currentStatus = entry.getKey();
            List<String> nextStatusList = entry.getValue();

            for (String nextStatus : nextStatusList) {
                prevStatusMap.computeIfAbsent(nextStatus, k -> Lists.newArrayList()).add(currentStatus);
            }
        }
    }

    /**
     * 根据当前状态获取上下级状态信息
     *
     * @param currentStatus 当前订单状态
     * @return VisitStatusVo 包含当前状态及其上下级状态
     */
    public static VisitStatusVo getStatusFlow(String currentStatus) {
        VisitStatusVo statusVo = new VisitStatusVo();
        statusVo.setNow(currentStatus);

        // 获取上级状态
        List<String> prevStatusList = prevStatusMap.getOrDefault(currentStatus, new ArrayList<>());
        statusVo.setBefore(prevStatusList);

        // 获取下级状态
        List<String> nextStatusList = nextStatusMap.getOrDefault(currentStatus, new ArrayList<>());
        statusVo.setAfter(nextStatusList);

        return statusVo;
    }

    /**
     * 检查状态转换是否合法
     *
     * @param fromStatus 原状态
     * @param toStatus   目标状态
     * @return true表示合法，false表示非法
     */
    public static boolean isValidTransition(String fromStatus, String toStatus) {
        List<String> allowedNextStatus = nextStatusMap.get(fromStatus);
        if (allowedNextStatus == null) {
            return false;
        }
        return allowedNextStatus.contains(toStatus);
    }

    /**
     * 获取所有可能的状态列表
     *
     * @return 所有状态列表
     */
    public static List<String> getAllStatus() {
        return Lists.newArrayList(WAIT, UNSUBMITTED, SUBMITTED, STARTED, ENDED,
                REFUND_PROCESSING, REFUNDED, DONE);
    }

    /**
     * 获取成功状态流的所有状态
     *
     * @return 成功状态流
     */
    public static List<String> getSuccessFlow() {
        return Lists.newArrayList(WAIT, UNSUBMITTED, SUBMITTED, STARTED, ENDED);
    }

    /**
     * 获取失败状态流1的所有状态
     *
     * @return 失败状态流1
     */
    public static List<String> getFailedFlow1() {
        return Lists.newArrayList(WAIT, UNSUBMITTED, SUBMITTED, REFUND_PROCESSING, REFUNDED);
    }

    /**
     * 获取失败状态流2的所有状态
     *
     * @return 失败状态流2
     */
    public static List<String> getFailedFlow2() {
        return Lists.newArrayList(WAIT, UNSUBMITTED, REFUND_PROCESSING, REFUNDED);
    }

    /**
     * 获取超时状态流的所有状态
     *
     * @return 超时状态流
     */
    public static List<String> getTimeoutFlow() {
        return Lists.newArrayList(WAIT, DONE);
    }

    /**
     * 判断是否为终端状态（没有后续状态的状态）
     *
     * @param status 待检查状态
     * @return 是否为终端状态
     */
    public static boolean isTerminalStatus(String status) {
        List<String> nextStatus = nextStatusMap.get(status);
        return nextStatus == null || nextStatus.isEmpty();
    }

}
