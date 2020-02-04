

package com.foodsecurity.xupdate.proxy.impl;

import android.text.TextUtils;

import com.foodsecurity.xupdate.Xupdate;
import com.foodsecurity.xupdate.entity.BundleVersionResult;
import com.foodsecurity.xupdate.entity.ApkVersionResult;
import com.foodsecurity.xupdate.entity.PluginVersionEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.entity.VersionEntity;
import com.foodsecurity.xupdate.proxy.IUpdateParser;
import com.foodsecurity.xupdate.utils.UpdateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本更新解析器
 *
 * @author zhujianwei134
 * @since 2018/7/5 下午4:36
 */
public class DefaultUpdateParser implements IUpdateParser {

    @Override
    public UpdateEntity parseJson(String json) {
        if (!TextUtils.isEmpty(json)) {
            ApkVersionResult checkResult = UpdateUtils.fromJson(json, ApkVersionResult.class);
            if (checkResult != null && checkResult.isOk()) {
                VersionEntity versionEntity = doLocalCompare(checkResult.getData());
                UpdateEntity updateEntity = new UpdateEntity();
                if (versionEntity.getUpdateStatus() == ApkVersionResult.NO_NEW_VERSION) {
                    updateEntity.setHasUpdate(false);
                } else {

                    updateEntity.setHasUpdate(true)
                            .setIsIgnorable(versionEntity.isIgnorable())
                            .setUpdateContent(versionEntity.getUpdateContent())
                            .setDownloadUrl(versionEntity.getDownloadUrl())
                            .setSize(versionEntity.getFileInfo().getSize())
                            .setFileName(versionEntity.getFileInfo().getName())
                            .setMd5(versionEntity.getFileInfo().getMd5())
                            .setVersionCode(versionEntity.getVersionCode())
                            .setVersionName(versionEntity.getVersionName());

                    if (versionEntity.getUpdateStatus() == ApkVersionResult.HAVE_NEW_VERSION_FORCED_UPLOAD) {
                        updateEntity.setForce(true);
                    }
                }
                return updateEntity;
            }
        }
        return null;
    }

    @Override
    public List<UpdateEntity> parseBundleJson(String json) throws Exception {
        if (!TextUtils.isEmpty(json)) {
            BundleVersionResult checkResult = UpdateUtils.fromJson(json, BundleVersionResult.class);
            if (checkResult != null && checkResult.isOk()) {
                List<PluginVersionEntity> versionEntities = checkResult.getData();
                if (null != versionEntities && versionEntities.size() > 0) {
                    List<UpdateEntity> updateEntities = new ArrayList<>();
                    for (int i = 0; i < versionEntities.size(); i++) {
                        PluginVersionEntity versionEntity = versionEntities.get(i);
                        UpdateEntity updateEntity = new UpdateEntity();
                        if (versionEntity.getUpdateStatus() == ApkVersionResult.NO_NEW_VERSION) {
                            updateEntity.setHasUpdate(false);
                        } else {
                            if (versionEntity.getUpdateStatus() == ApkVersionResult.HAVE_NEW_VERSION_FORCED_UPLOAD) {
                                updateEntity.setForce(true);
                            }
                            updateEntity.setHasUpdate(true)
                                    .setIsSilent(versionEntity.isSilent())
                                    .setUpdateContent(versionEntity.getUpdateContent())
                                    .setDownloadUrl(versionEntity.getDownloadUrl())
                                    .setSize(versionEntity.getFileInfo().getSize())
                                    .setFileName(versionEntity.getFileInfo().getName())
                                    .setMd5(versionEntity.getFileInfo().getMd5())
                                    .setAlias(versionEntity.getPluginAlias())
                                    .setVersionCode(versionEntity.getVersionCode())
                                    .setVersionName(versionEntity.getVersionName());
                        }
                        updateEntities.add(updateEntity);
                    }
                    return updateEntities;
                }
            }
        }
        return null;
    }

    /**
     * 进行本地版本判断[防止服务端出错，本来是不需要更新，但是服务端返回是需要更新]
     *
     * @param checkResult
     * @return
     */
    private VersionEntity doLocalCompare(VersionEntity checkResult) {
        //服务端返回需要更新
        int lastVersionCode = checkResult.getVersionCode();
        if (lastVersionCode <= UpdateUtils.getVersionCode(Xupdate.getContext())) {
            //最新版本小于等于现在的版本，不需要更新
            checkResult.setUpdateStatus(ApkVersionResult.NO_NEW_VERSION);
        } else {
            if (checkResult.isForceUpdate()) {
                checkResult.setUpdateStatus(ApkVersionResult.HAVE_NEW_VERSION_FORCED_UPLOAD);
            } else {
                checkResult.setUpdateStatus(ApkVersionResult.HAVE_NEW_VERSION);
            }
        }
        return checkResult;
    }
}
