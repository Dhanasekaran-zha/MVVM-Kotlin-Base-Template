package com.ds.basetemplate.base

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ds.basetemplate.R

open class BaseActivity : AppCompatActivity() {

    private var dialog: Dialog? = null

    fun showLoading() {
        hideLoading()
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun hideLoading() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

    fun setNewFragment(fragment: Fragment, title: String, addbackstack: Boolean) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.f_layout, fragment)
        if (addbackstack) transaction.addToBackStack(title)
        transaction.commit()
    }

    fun setBundle(fragment: Fragment, bundle: Bundle): Fragment {
        fragment.arguments = bundle
        return fragment
    }
}