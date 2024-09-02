package com.intec.template.dataShareStream

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Surface
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareApi
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareBean
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareError
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareListener
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.intec.template.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class SurfaceShareDataEngine private constructor() {
    private var mCallBack: MessageCallBack? = null
    private var mImageReader: ImageReader? = null
    private var mHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null
    private var mSurfaceShareBean: SurfaceShareBean? = null
    private var mCurrentError = 0
    private var log_interval: Long = 0
    private var mImageHandler: MyHandler? = null
    private var mImageHandleThread: HandlerThread? = null
    private var mRemoteSurface: Surface? = null

    private object InstanceHolder {
        val instance = SurfaceShareDataEngine()
    }

    fun setMessageCallBack(callBack: MessageCallBack) {
        Log.i(TAG, "set message callBack: $callBack")
        mCallBack = callBack
    }

    private fun initSurfaceResource(): Boolean {
        Log.d(TAG, "initSurfaceResource ..")
        startBackgroundThread()
        val init = initImageSurface()
        if (!init) {
            Log.e(TAG, "Remote Surface instance has been used")
            return false
        }
        startHandleImageThread()
        return true
    }

    private fun startHandleImageThread() {
        mImageHandleThread = HandlerThread("ImageHandleThread")
        mImageHandleThread!!.start()
        mImageHandler = MyHandler(mImageHandleThread!!.getLooper())
    }

    fun startRequest() {
        initSurfaceResource()
        if (mSurfaceShareBean == null) {
            mSurfaceShareBean = SurfaceShareBean()
            mSurfaceShareBean!!.name = "VideoCall"
        }
        mRemoteSurface = mImageReader!!.surface
        Log.d(
            TAG,
            "startRequest getSurface:$mRemoteSurface"
        )
        SurfaceShareApi.getInstance()
            .requestImageFrame(mRemoteSurface, mSurfaceShareBean, object : SurfaceShareListener() {
                override fun onError(error: Int, message: String) {
                    super.onError(error, message)
                    Log.d(
                        TAG,
                        "requestImageFrame onError error :$error"
                    )
                    mCurrentError = error
                    if (mCallBack != null) {
                        mCallBack!!.onError(error)
                    }
                }

                override fun onStatusUpdate(status: Int, message: String) {
                    super.onStatusUpdate(status, message)
                    Log.d(
                        TAG,
                        "requestImageFrame onStatusUpdate status:$status"
                    )
                }
            })
    }

    private fun initImageSurface(): Boolean {
        if (mImageReader != null) {
            Log.d(TAG, "initImageSurface has an ImageReader instance already!")
            return false
        }
        mImageReader = ImageReader.newInstance(
            VISION_IMAGE_WIDTH,
            VISION_IMAGE_HEIGHT,
            ImageFormat.YUV_420_888,
            MAX_CACHE_IMAGES
        )
        mImageReader!!.setOnImageAvailableListener(MyOnImageAvailableListener(), mHandler)
        return true
    }

    private fun startBackgroundThread() {
        if (mBackgroundThread != null && mHandler != null) {
            Log.d(TAG, "startBackgroundThread has already start a mBackgroundThread")
            return
        }
        mBackgroundThread = HandlerThread("ExternalBackground")
        mBackgroundThread!!.start()
        mHandler = Handler(mBackgroundThread!!.getLooper())
    }

    private inner class MyOnImageAvailableListener : ImageReader.OnImageAvailableListener {
        override fun onImageAvailable(reader: ImageReader) {
            val startTime = System.currentTimeMillis()
            var image: Image? = null
            try {
                image = reader.acquireLatestImage()
                if (image == null) {
                    Log.d(TAG, "onImageAvailable image is null")
                    return
                }
                if (mBackgroundThread == null) {
                    Log.d(TAG, "onImageAvailable mBackgroundThread is null")
                    return
                }

                    CoroutineScope(Dispatchers.IO).launch {
                        delay(100)
                        image.let{
                            EventRepository.requestImage(image)
                        }
                        Log.d("camara", " evento $image")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (log_interval % 60 == 0L) {
                    Log.d(
                        TAG,
                        "onImageAvailable push image time:" + (System.currentTimeMillis() - startTime) + "ms"
                    )
                }
                log_interval++
            }
        }
    }

    private inner class MyHandler internal constructor(looper: Looper?) : Handler(looper!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg != null) {
                val data = msg.obj as Map<Int, ByteArray>
                if (mCallBack != null) {
                    Log.d("Camara", "$data")
                    mCallBack!!.onData(data)
                }
            }
        }
    }

    private fun stopBackgroundThread() {
        if (mBackgroundThread == null) {
            Log.i(TAG, "the background thread is null")
            return
        }
        Log.i(
            TAG,
            "stopBackgroundThread thread id:" + mBackgroundThread!!.name + "," + mBackgroundThread!!.threadId
        )
        mBackgroundThread!!.quitSafely()
        try {
            mBackgroundThread!!.join(50)
            mBackgroundThread = null
            mHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun stopHandleThread() {
        if (mImageHandleThread == null) {
            Log.i(TAG, "the mImageHandleThread is null")
            return
        }
        mImageHandleThread!!.quitSafely()
        try {
            mImageHandleThread!!.join(50)
            mImageHandleThread = null
            mImageHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun closeImageReader() {
        Log.d(TAG, "closeImageReader == ")
        if (mImageReader != null) {
            mImageReader!!.close()
            mImageReader = null
        }
        Log.d(TAG, "closeImageReader mRemoteSurface release")
        if (mRemoteSurface != null) {
            mRemoteSurface!!.release()
            mRemoteSurface = null
        }
    }

    fun stopPushStream() {
        if (mCurrentError == SurfaceShareError.ERROR_SURFACE_SHARE_USED) {
            Log.d(TAG, "stopPushStream ERROR_SURFACE_SHARE_USED .")
            return
        }
        SurfaceShareApi.getInstance().abandonImageFrame(mSurfaceShareBean)
        stopBackgroundThread()
        stopHandleThread()
        closeImageReader()
        mSurfaceShareBean = null
        mCurrentError = 0
        log_interval = 0
    }

    interface MessageCallBack {
        fun onData(bytes: Map<Int, ByteArray>?)
        fun onError(error: Int)
    }


    companion object {
        private const val TAG = "SurfaceShareDataEngine"
        const val IMAGE_DATA_FOR_LIVEME = 0
        const val IMAGE_DATA_FOR_LOCAL_PREVIEW = 1
        const val VISION_IMAGE_WIDTH = 640
        const val VISION_IMAGE_HEIGHT = 480
        private const val MAX_CACHE_IMAGES = 4 //maxImages 保持和VisionSdk一致
        val instance: SurfaceShareDataEngine
            get() = InstanceHolder.instance
    }
}