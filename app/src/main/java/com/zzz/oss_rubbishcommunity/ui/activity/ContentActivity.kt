package com.zzz.oss_rubbishcommunity.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.zzz.oss_rubbishcommunity.R
import java.io.Serializable

class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        intent?.extras?.let {
            (it.getSerializable(EXTRA_DES) as? Destination)?.let { des ->
                findNavController(R.id.nav_host_fragment).navigate(
                    des.id,
                    it.getBundle(EXTRA_ARGS_BUNDLE),
                    navOptions { popUpTo(R.id.bridgeFragment) { inclusive = true } }
                )
            }

            (it.getInt(EXTRA_DES_ID)).let { desId ->
                if (desId == 0) return
                findNavController(R.id.nav_host_fragment).navigate(
                    desId,
                    null,
                    navOptions { popUpTo(R.id.bridgeFragment) { inclusive = true } })
            }
        }
    }

    companion object {
        private const val EXTRA_DES = "extra_des"
        private const val EXTRA_DES_ID = "extra_des_id"
        const val EXTRA_ARGS = "extra_args"
        const val EXTRA_ARGS_BUNDLE = "extra_args_bundle"
        const val EXTRA_ARGS_INDUSTRY = "extra_args_industry"
        const val REQUEST_CODE_REFRESH_MEDIAS = 10001
        const val RESULT_CODE_REFRESH_MEDIAS = 10001

        fun createIntent(
                context: Context,
                des: Destination,
                s: Serializable? = null,
                bundle: Bundle? = null
        ): Intent {
            return Intent(context, ContentActivity::class.java)
                .putExtra(EXTRA_DES, des)
                .putExtra(EXTRA_ARGS_BUNDLE, bundle ?: bundleOf(EXTRA_ARGS to s))
        }

        fun needRefreshMedias(resultCode: Int): Boolean {
            return resultCode == RESULT_CODE_REFRESH_MEDIAS
        }

        // desId may not contains in the navGraph
        fun createIntentUnsafe(context: Context, @IdRes desId: Int): Intent {
            return Intent(context, ContentActivity::class.java)
                .putExtra(EXTRA_DES_ID, desId)
        }
    }

    enum class Destination(@IdRes val id: Int) {
        NewsDetail(R.id.navigation_news_detail),
    }
}
