package lmd.kmp.yandexmap

import android.content.Context
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class YandexMapController constructor(
    private val apiKey: String
) {
    class LifecycleHolder<T> {
        private var data: T? = null
        private val actions = mutableListOf<(T) -> Unit>()

        fun set(data: T) {
            this.data = data

            with(actions) {
                forEach { it.invoke(data) }
                clear()
            }
        }

        fun clear() {
            this.data = null
        }

        fun doWith(block: (T) -> Unit) {
            val map = data
            if (map == null) {
                actions.add(block)
                return
            }

            block(map)
        }

        suspend fun get(): T = suspendCoroutine { continuation ->
            doWith { continuation.resume(it) }
        }
    }

    private val mapKit= LifecycleHolder<MapKit>()

    fun init(context: Context) {
        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(context)
        mapKit.set(MapKitFactory.getInstance())
    }

    fun onStop() {
        mapKit.doWith {
            it.onStop()
        }
    }

    fun onStart() {
        mapKit.doWith {
            it.onStart()
        }
    }
}