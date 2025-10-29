package com.lyf.lingyingfacompose.ui.wx.utils

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * @author: njb
 * @date:   2025/8/18 1:46
 * @desc:   描述
 */
object RxBus {
    val bus = PublishSubject.create<Any>().toSerialized()

    // 发送事件
    fun post(event: Any) {
        bus.onNext(event)
    }

    inline fun <reified T : Any> toObservable(): Observable<T> {
        return Observable.create { emitter ->
            val disposable = bus.subscribe(
                { item ->
                    // 此时 item 是 Any 类型，T 是 Any 子类，is 判断安全
                    if (item is T) {
                        if (!emitter.isDisposed) {
                            emitter.onNext(item)
                        }
                    }
                },
                { error ->
                    if (!emitter.isDisposed) {
                        emitter.onError(error)
                    }
                },
                {
                    if (!emitter.isDisposed) {
                        emitter.onComplete()
                    }
                }
            )
            emitter.setDisposable(disposable)
        }
    }
}