package com.example.caiyunmvvmdemo.testRxJava

import androidx.work.impl.Schedulers
import io.reactivex.*
import org.reactivestreams.Subscription

//观察者
fun <T>createObserver(): FlowableSubscriber<T> {
    val subscriber: FlowableSubscriber<T> =
        object : FlowableSubscriber<T> {
            override fun onSubscribe(s: Subscription) {
                println("onSubscribe")
                //订阅时候的操作
                s.request(Long.MAX_VALUE) //请求多少事件，这里表示不限制
            }

            override fun onError(t: Throwable) {
                println("${t.printStackTrace()}")
            }

            override fun onComplete() {
                println("onComplete")
            }

            override fun onNext(t: T) {
                println("$t")
            }
        }
    return subscriber
}

//被观察者
fun <T>createObservable(): Flowable<T> {
    val flowable = Flowable.create(FlowableOnSubscribe<T> {
        it.onNext("hello" as T)
        it.onNext("world" as T)
        it.onComplete()

    },BackpressureStrategy.BUFFER)
    return flowable
}

fun main() {
    createObservable<String>()
        .subscribe(createObserver<String>())


    Flowable.create(FlowableOnSubscribe<String> {
        it.onNext("hello")
        it.onNext("world")
        it.onComplete()
    }, BackpressureStrategy.BUFFER)
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .doOnNext {
            println("doOnNext $it")
        }
        .doOnComplete {
            println("doOnComplete")
        }
        .subscribe({
            println("onNext $it")
        }, {
            it.printStackTrace()
        }, {
            println("onComplete")
        }, {
            println("onSubcribe")
        })



}

