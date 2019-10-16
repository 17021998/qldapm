package com.example.measurehearthrate.View

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.measurehearthrate.R

class DialogLoadingFragment : DialogFragment() {

    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG,"onCreate")
        arguments?.let {
            title = it.getString(DIALOG_TITLE) ?: ""
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_loading,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.tv_title)

        if(TextUtils.isEmpty(title)) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = title
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    companion object {
        const val TAG = "DialogLoadingFragment"
        const val DIALOG_TITLE = "DIALOG TITLE"

        fun newInstance(title: String =""): DialogLoadingFragment {
            val fragment = DialogLoadingFragment()
            val args = Bundle()
            args.putString(DIALOG_TITLE,title)
            fragment.arguments = args
            return fragment
        }
    }
}