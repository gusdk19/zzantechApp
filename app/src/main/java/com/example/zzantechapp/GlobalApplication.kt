package com.example.zzantechapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // kakao SDK 초기화
        KakaoSdk.init(this, "2a324d6caca419a41a74e47bf0dfd973")
    }
}
