package com.xjd.mvplearntakeout.presenter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xjd.mvplearntakeout.model.bean.ResponseInfo
import com.xjd.mvplearntakeout.model.bean.Seller
import com.xjd.mvplearntakeout.ui.iui.IHomeFragment
import retrofit2.Retrofit
import com.xjd.mvplearntakeout.model.net.TakeoutService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.ObjDoubleConsumer

/**
 * Created by Administrator on 2018-11-07.
 */

class HomeFragmentPresenter(val ihomeFragment: IHomeFragment) {
    val takeoutService: TakeoutService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.2.118:8080/TakeoutService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        takeoutService = retrofit.create<TakeoutService>(TakeoutService::class.java!!)
    }


    fun getHomeInfo() {
        val homeCall = takeoutService.getHomeInfo()
        homeCall.enqueue(object : Callback<ResponseInfo> {
            override fun onResponse(call: Call<ResponseInfo>?, response: Response<ResponseInfo>?) {
                if (response == null) {
                    Log.e("home", "服务器没有返回成功")
                } else {
                    if (response.isSuccessful) {
                        val responseInfo = response.body()
                        if (responseInfo.code.equals("0")) {
                            val json = responseInfo.data
                            parserJson(json)
                        } else {
                            Log.e("home", "服务器返回的数据错误responseInfo.code:${responseInfo.code}")
                        }
                    } else {
                        Log.e("home", "服务器返回的代码错误response.code():${response.code()}")
                    }

                }
            }

            override fun onFailure(call: Call<ResponseInfo>?, t: Throwable?) {
                Log.e("home", "服务器没有连接成功")
            }

        })

//        ihomeFragment.onHomeFailed()
//        ihomeFragment.onHomeSuccess()
    }

    private fun parserJson(json: String) {
        val gson = Gson()
        val jsonObject = JSONObject(json)
        val nearby = jsonObject.getString("nearbySellerList")
        val nearbySellers: List<Seller> = gson.fromJson(nearby, object : TypeToken<List<Seller>>() {}.type)

        val other = jsonObject.getString("otherSellerList")
        val otherSellers: List<Seller> = gson.fromJson(other, object : TypeToken<List<Seller>>() {}.type)

        if(nearbySellers.isNotEmpty() ||otherSellers.isNotEmpty()){
            ihomeFragment.onHomeSuccess(nearbySellers,otherSellers)
        }else{
            ihomeFragment.onHomeFailed()
        }
    }

}


