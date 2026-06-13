package com.aihoo.api.doctor.app.service;

/**
 * 兼容垫片：所有方法都委托到 im 域的 com.aihoo.domain.im.service.ProposalService。
 * 任务 #56 阶段将删除此垫片，所有调用方应改用 com.aihoo.domain.im.service.ProposalService。
 */
public interface ProposalService extends com.aihoo.domain.im.service.ProposalService {
}
