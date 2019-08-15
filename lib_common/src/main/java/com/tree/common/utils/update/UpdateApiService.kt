package com.tree.common.utils.update

import com.tree.common.bean.UpdateInfo
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Create By Hosigus at 2019/5/11
 */
interface UpdateApiService {
    @GET("/app/cyxbsAppUpdate.xml")
    fun getUpdateInfo(): Observable<UpdateInfo>
}