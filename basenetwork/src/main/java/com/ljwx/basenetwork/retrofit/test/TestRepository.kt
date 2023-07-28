package com.ljwx.basenetwork.retrofit.test

import android.content.res.Resources.NotFoundException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ActivityUtils
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.ljwx.baseapp.response.DataResult
import com.ljwx.baseapp.response.ResponseException
import com.ljwx.baseapp.vm.BaseDataRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TestRepository : BaseDataRepository<TestService>() {

    val mGiteeTestRetrofit by lazy {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(ActivityUtils.getTopActivity()))
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        Retrofit.Builder()
            .baseUrl("https://v.api.aa1.cn")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    fun requestTest(callback: DataResult.Result<String>): MutableLiveData<String> {
        val liveData = MutableLiveData<String>()
        mGiteeTestRetrofit.create(TestService::class.java).search1("8")
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("ljwx2", "请求结果成功")
                    if (response.isSuccessful) {
                        val result = response.body().toString()
                        callback.call(DataResult.Success(result))
                    }
                    callback.call(DataResult.Error(ClassNotFoundException()))
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    call.request()
                    Log.d("ljwx2", "请求结果失败:${t.message}")
                }
            })
        return liveData
    }

    fun requestTest2() {
        mGiteeTestRetrofit.create(TestService::class.java).search2("7")
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : QuickObserver3<String>() {
                override fun onResponseSuccess(value: String) {

                }
            })
    }

    override fun createServer(): TestService {
        return mGiteeTestRetrofit.create(TestService::class.java)
    }

}