package com.jiachang.tv_launcher;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * @author Mickey.Ma
 * @date 2020-03-24
 * @description
 */
public class SampleApplication extends TinkerApplication {

    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.jiachang.tv_launcher.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

}
