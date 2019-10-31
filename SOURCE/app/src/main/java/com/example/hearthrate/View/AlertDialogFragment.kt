package com.example.hearthrate.View

import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableString
import android.view.KeyEvent
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.hearthrate.R

class AlertDialogFragment: DialogFragment(), DialogInterface.OnKeyListener  {
    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        private val TAG = "AlertDialogFragment"

        private val ARGUMENTS_TAG = "ARGUMENTS_TAG"
        private val ARGUMENTS_BUNDLE = "ARGUMENTS_BUNDLE"
        private val ARGUMENTS_LAYOUT_ID = "ARGUMENTS_LAYOUT_ID"
        private val ARGUMENTS_TEXT_VIEWS = "ARGUMENTS_TEXT_VIEWS"
        private val ARGUMENTS_DISMISS_VIEWS = "ARGUMENTS_DISMISS_VIEWS"
        private val ARGUMENTS_ALLOW_BACK_PRESS = "ARGUMENTS_ALLOW_BACK_PRESS"

        internal fun newInstance(tag: String,
                                 bundle: Bundle?,
                                 layoutId: Int,
                                 textViews: HashMap<Int, String> ,
                                 dismissViews: ArrayList<Int> ,
                                 isAllowBackPress: Boolean): AlertDialogFragment {
            val fragment = AlertDialogFragment()
            val arguments = Bundle()
            arguments.putString(ARGUMENTS_TAG, tag)
            arguments.putBundle(ARGUMENTS_BUNDLE, bundle)
            arguments.putInt(ARGUMENTS_LAYOUT_ID, layoutId)
            arguments.putSerializable(ARGUMENTS_TEXT_VIEWS, textViews)
            arguments.putSerializable(ARGUMENTS_DISMISS_VIEWS, dismissViews)
            arguments.putBoolean(ARGUMENTS_ALLOW_BACK_PRESS, isAllowBackPress)
            fragment.arguments = arguments
            return fragment
        }

    }

    class Builder(private val mLayoutId: Int) {

        private var mIsAllowBackPress = true
        private val mTextViews = HashMap<Int, String>()
        private val mDismissViews = ArrayList<Int>()


        @NonNull
        fun create(tag: String): AlertDialogFragment {
            return create(tag, null)
        }

        @NonNull
        fun create(tag: String, arguments: Bundle?): AlertDialogFragment {
            return newInstance(
                    tag,
                    arguments,
                    mLayoutId,
                    mTextViews,
                    mDismissViews,
                    mIsAllowBackPress
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
}