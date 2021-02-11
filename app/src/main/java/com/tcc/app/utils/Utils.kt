package com.tcc.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.RectF
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.TextView.BufferType

import java.math.RoundingMode
import java.net.URLConnection.guessContentTypeFromName
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt


object Utils {


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    var lastClickTime: Long = 0
    val DOUBLE_CLICK_TIME_DELTA: Long = 500

    fun isDoubleClick(): Boolean {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            lastClickTime = clickTime
            return true
        }
        lastClickTime = clickTime
        return false
    }

    fun calculateRectOnScreen(view: View): RectF {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return RectF(
            location[0].toFloat(),
            location[1].toFloat(),
            (location[0] + view.measuredWidth).toFloat(),
            (location[1] + view.measuredHeight).toFloat()
        )
    }

    fun calculateRectInWindow(view: View): RectF {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return RectF(
            location[0].toFloat(),
            location[1].toFloat(),
            (location[0] + view.measuredWidth).toFloat(),
            (location[1] + view.measuredHeight).toFloat()
        )
    }

    fun pxToDp(px: Float): Float {
        return px / Resources.getSystem().displayMetrics.density
    }

    fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }

    fun convertDpToPixel(dp: Float, context: Context): Int {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
        return px.roundToInt()
    }

    fun convertPixelToDp(px: Float, context: Context): Int {
        val dp = px / context.resources.displayMetrics.density
        return dp.roundToInt()
    }

    fun convertPixelToSp(px: Float, context: Context): Int {
        val sp = px / context.resources.displayMetrics.scaledDensity
        return sp.roundToInt()
    }

    fun getUriFromPath(context: Context, path: String): Uri {
        val res = context.resources
        return Uri.parse(
            "android.resource://"
                    + context.packageName + "/" + path
        )
    }

    fun toUri(context: Context, resourceId: Int): Uri {
        val res = context.resources
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + res.getResourcePackageName(resourceId)
                    + '/'.toString() + res.getResourceTypeName(resourceId) + '/'.toString() + res.getResourceEntryName(
                resourceId
            )
        )
    }

    fun getRealPathFromURI(context: Context?, uri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context?.contentResolver?.query(uri, proj, null, null, null)
        if (cursor != null) {
            try {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                return cursor.getString(columnIndex)
            } catch (ex: Exception) {
                ex.message?.let { Logger.e(it) }
            } finally {
                cursor.close()
            }
        }
        return null
    }

    fun getFileSize(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()

        return DecimalFormat("#,##0.#")
            .format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    fun getSize(size: Double): Int {
        if (size <= 0) return 0
        val digitGroups = (log10(size) / log10(1024.0)).toInt()
        return (size / 1024.0.pow(digitGroups.toDouble())).roundToInt()
    }

    fun isImageFile(path: String): Boolean {
        val mimeType = guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }

    fun isVideoFile(path: String): Boolean {
        val mimeType = guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("video")
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun getOneMonthAgoDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val date: Date = calendar.time
        val format = SimpleDateFormat("MMM", Locale.US)
        return format.format(date) + calendar.get(Calendar.YEAR)
    }

    fun makeTextViewResizable(
        tv: TextView,
        maxLine: Int,
        expandText: String,
        viewMore: Boolean
    ) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val text: String
                val lineEndIndex: Int
                val obs = tv.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (maxLine == 0) {
                    lineEndIndex = tv.layout.getLineEnd(0)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                } else {
                    lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                }
                tv.text = text
                tv.movementMethod = LinkMovementMethod.getInstance()
                tv.setText(
                    addClickablePartTextViewResizable(
                        Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                        viewMore
                    ), BufferType.SPANNABLE
                )
            }
        })
    }

    private fun addClickablePartTextViewResizable(
        strSpanned: Spanned, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    tv.layoutParams = tv.layoutParams
                    tv.setText(tv.tag.toString(), BufferType.SPANNABLE)
                    tv.invalidate()
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false)
                    } else {
                        makeTextViewResizable(tv, 3, "View More", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }

    fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    /*----Method to Check GPS is enable or disable ----- */
    fun displayGpsStatus(context: Context): Boolean {
        val contentResolver: ContentResolver = context
            .contentResolver
        val gpsStatus: Boolean = Settings.Secure
            .isLocationProviderEnabled(
                contentResolver,
                LocationManager.GPS_PROVIDER
            )
        return gpsStatus
    }

    fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Running Tracker Service Channel"
            val description = "Final Coursework G53MDP"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                "defualt",
                name,
                importance
            )
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
    }


    //this function is used for converting seconds to hh:mm:ss format
    fun secondFormatString(seconds: Long): String? {
        val s = seconds % 60
        var h = seconds / 60
        val m = h % 60
        h = h / 60
        return String.format("%02d", h) + ":" + String.format(
            "%02d",
            m
        ) + ":" + String.format("%02d", s)
    }

    //round up float number
    fun roundUp(num: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(num.toDouble())
    }

    //this function is used for converting date in "String" data type to "Date" data type
    fun convertStringToDate(dateString: String?): Date? {
        val formatter =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = null
        try {
            date = formatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }


    fun isTenMinCompleted(milisec: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val LastSyncTime = currentTime - milisec
        return LastSyncTime >= Constant.TEN_MILISEC
    }

}
