package com.xjd.mvplearntakeout.ui.iui

import com.xjd.mvplearntakeout.model.bean.Seller

/**
 * Created by Administrator on 2018-11-07.
 */

interface IHomeFragment {

    fun onHomeFailed()
    fun onHomeSuccess(nearbySellers: List<Seller>, otherSellers: List<Seller>)
}
