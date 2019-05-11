

package com.foodsecurity.xupdate.entity;

/**
 * 版本更新检查返回的结果
 * <p>
 * 0:无版本更新
 * 1:有版本更新，不需要强制升级
 * 2:有版本更新，需要强制升级
 *
 * @author zhujianwei134
 * @date 2019/4/27
 */
public class ApkVersionResult extends BaseResponse<VersionEntity> {
    /**
     * 0: 无版本更新
     */
    public final static int NO_NEW_VERSION = 0;
    /**
     * 1: 有版本更新，不需要强制升级
     */
    public final static int HAVE_NEW_VERSION = 1;
    /**
     * 2: 有版本更新，需要强制升级
     */
    public final static int HAVE_NEW_VERSION_FORCED_UPLOAD = 2;
}
