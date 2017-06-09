package com.example.startpage;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

//改造Application（兼容性更好）
public class MyApplication1 extends TinkerApplication {

    public MyApplication1() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.example.startpage.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
