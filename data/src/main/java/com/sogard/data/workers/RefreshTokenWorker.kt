package com.sogard.data.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sogard.domain.repositories.AuthenticationRepository
import extensions.applyIoScheduler
import java.util.concurrent.TimeUnit

class RefreshTokenWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

//    private val authenticationRepository: AuthenticationRepository by inject()

    companion object {
        const val TAG = "REFRESH_TOKEN_WORKER"
    }
    override fun doWork(): Result {
//        val requestError: Throwable? = authenticationRepository.fetchToken()
//            .applyIoScheduler()
//            .blockingGet(10, TimeUnit.SECONDS)

        val requestError = null
        val result: Result = requestError?.run { Result.failure() } ?: Result.success()
        Log.i("REFRESH TOKEN WORKER: ", result.toString())

        return result
    }

}