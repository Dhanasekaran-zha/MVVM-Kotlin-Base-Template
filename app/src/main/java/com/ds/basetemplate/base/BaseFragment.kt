package com.ds.basetemplate.base

import android.app.Dialog
import android.view.Window
import androidx.fragment.app.Fragment
import com.ds.basetemplate.R

open class BaseFragment : Fragment() {

    private var dialog: Dialog? = null

    fun showLoading() {
        hideLoading()
        dialog = Dialog(requireActivity())
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun hideLoading() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }
}