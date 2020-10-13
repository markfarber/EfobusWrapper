package il.co.blaster.efobuswrapper

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPerms()
        initView()
    }

    private fun requestPerms() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOC_REQUEST
        )
    }

    private fun initView() {
        w_view?.apply {
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                setGeolocationEnabled(true)
                databaseEnabled = true
                domStorageEnabled = true
            }

            webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String?,
                    callback: GeolocationPermissions.Callback?
                ) {
                    callback?.invoke(origin, true, false)
//                    super.onGeolocationPermissionsShowPrompt(origin, callback)
                }
            }
            webViewClient = WClient(w_swipe)

            loadUrl("https://www.efobus.com/")
        }

        w_swipe?.setOnRefreshListener {
            w_view?.reload()
        }
    }

    class WClient(private val swipeView: SwipeRefreshLayout?) : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            swipeView?.isRefreshing = false
        }
    }

    companion object {
        const val LOC_REQUEST = 8906
    }
}