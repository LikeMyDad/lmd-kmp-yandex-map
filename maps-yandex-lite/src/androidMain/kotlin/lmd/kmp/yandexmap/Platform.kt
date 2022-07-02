package lmd.kmp.yandexmap

import com.yandex.mapkit.MapKitFactory

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    init {

    }
}