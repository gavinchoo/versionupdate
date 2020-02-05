

package com.foodsecurity.xupdate.entity;

import java.util.List;

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
public class PluginsResult extends BaseResponse<List<PluginEntity>> {

}
