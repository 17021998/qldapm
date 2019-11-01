package com.example.hearthrate.View

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.R
import java.io.Serializable

class AlertDialogFragment : DialogFragment(), DialogInterface.OnKeyListener {

    companion object {

        private val TAG = "AlertDialogFragment"

        private val ARGUMENTS_TAG = "ARGUMENTS_TAG"
        private val ARGUMENTS_BUNDLE = "ARGUMENTS_BUNDLE"
        private val ARGUMENTS_LAYOUT_ID = "ARGUMENTS_LAYOUT_ID"
        private val ARGUMENTS_TEXT_VIEWS = "ARGUMENTS_TEXT_VIEWS"
        private val ARGUMENTS_DISMISS_VIEWS = "ARGUMENTS_DISMISS_VIEWS"

        internal fun newInstance(tag: String,
                                 bundle: Bundle?,
                                 layoutId: Int,
                                 textViews: HashMap<Int, String>,
                                 dismissViews: ArrayList<Int>): AlertDialogFragment {
            val fragment = AlertDialogFragment()
            val arguments = Bundle()
            arguments.putString(ARGUMENTS_TAG, tag)
            arguments.putBundle(ARGUMENTS_BUNDLE, bundle)
            arguments.putInt(ARGUMENTS_LAYOUT_ID, layoutId)
            arguments.putSerializable(ARGUMENTS_TEXT_VIEWS, textViews)
            arguments.putSerializable(ARGUMENTS_DISMISS_VIEWS, dismissViews)
            fragment.arguments = arguments
            return fragment
        }
    }


    private var mLayoutId: Int = 0

    private var mTextViews: java.util.HashMap<Int, String>? = null

    private var mDismissViews: java.util.ArrayList<Int>? = null

    lateinit var mData: Intent

    var mTag: String? = null

    var mListener: OnDialogFragmentResult? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mData = Intent()
        val arguments = arguments!!
        mTag = arguments.getString(ARGUMENTS_TAG)
        val bundle = arguments.getBundle(ARGUMENTS_BUNDLE)
        if (bundle != null) {
            mData.putExtras(bundle)
        }

        mLayoutId = arguments.getInt(ARGUMENTS_LAYOUT_ID)
        mTextViews = arguments.getSerializable(ARGUMENTS_TEXT_VIEWS) as java.util.HashMap<Int, String>
        mDismissViews = arguments.getSerializable(ARGUMENTS_DISMISS_VIEWS) as java.util.ArrayList<Int>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewInflated = inflater.inflate(mLayoutId, container)
        return viewInflated
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set TextViews
        if (mTextViews != null) {
            for ((key, value) in mTextViews!!) {
                val v = view.findViewById<View>(key)
                if (v == null) {
                    Log.e(TAG, "Set TextViews - view is null on mTag = $mTag")
                } else {
                    (v as TextView).text = value
                }
            }
        }

        //setDismissView
        if (mDismissViews != null && !mDismissViews!!.isEmpty()) {
            for (viewID in mDismissViews!!) {
                if (view.findViewById<View>(viewID) == null) {
                    Log.e(TAG, "Set action - view is null on mTag = $mTag")
                } else {
                    view.findViewById<View>(viewID).setOnClickListener {
                        dismiss()
                        if (mListener != null) {
                            mListener!!.onDialogFragmentResult(mTag!!, viewID, mData)
                        }
                    }
                }

            }
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parentFragment = parentFragment
        if (parentFragment != null) {
            if (parentFragment is OnDialogFragmentResult) {
                mListener = parentFragment
            }
        }
        if (mListener == null && context is OnDialogFragmentResult) {
            mListener = context
        }

    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val window = dialog.window
        if (window != null) {
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window!!.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
            dialog.setOnKeyListener(this)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        val prev = manager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
                .add(this, tag)
                .commitAllowingStateLoss()
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        dismiss()
        return true
    }

    override fun dismiss() {
        val fragmentManager = fragmentManager
        fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
    }




    class Builder(private val mLayoutId: Int) {

        private val mTextViews = HashMap<Int, String>()
        private val mDismissViews = ArrayList<Int>()

        @NonNull
        fun create(tag: String): AlertDialogFragment {
            return create(tag, null)
        }

        @NonNull
        fun create(tag: String, arguments: Bundle?): AlertDialogFragment {
            return AlertDialogFragment.newInstance(
                    tag,
                    arguments,
                    mLayoutId,
                    mTextViews,
                    mDismissViews
            )
        }

        fun addTextView(@IdRes viewId: Int, text: String): Builder {
            mTextViews[viewId] = text
            return this
        }

        fun addDismissView(@IdRes viewId: Int): Builder {
            mDismissViews.add(viewId)
            return this
        }

        fun show(manager: FragmentManager, tag: String): AlertDialogFragment {
            return show(manager, tag, DialogFragment.STYLE_NO_TITLE, R.style.AppTheme)
        }

        fun show(manager: FragmentManager, tag: String, style: Int, theme: Int): AlertDialogFragment {
            val dialogFragment = create(tag)
            dialogFragment.setStyle(style, theme)
            dialogFragment.show(manager, tag)
            return dialogFragment
        }

    }

    interface OnDialogFragmentResult {
        fun onDialogFragmentResult(tag: String, actionId: Int, data: Intent)
    }
}