

package com.foodsecurity.xupdate.proxy;

import com.foodsecurity.xupdate.entity.UpdateEntity;

import java.util.List;
/**
 * 版本更新解析器
 *
 * @author zhujianwei134
 * @since 2018/6/29 下午8:30
 */
public interface IUpdateParser {

    /**
     * 将请求的json结果解析为版本更新信息实体
     *
     * @param json
     * @return
     */
    UpdateEntity parseJson(String json) throws Exception;

    List<UpdateEntity> parseBundleJson(String json) throws Exception;
}
