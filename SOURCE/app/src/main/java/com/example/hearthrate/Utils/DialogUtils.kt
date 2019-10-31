package com.example.hearthrate.Utils

import androidx.fragment.app.FragmentManager
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.R
import com.example.hearthrate.View.AlertDialogFragment

object DialogUtils {

    private const val NO_INTERNET_CONNECTION = "NO_INTERNET_CONNECTION"

    fun showError(error: Int, fragment: FragmentManager) {
        when(error) {
            ErrorCode.NO_INTERNET_CONNECTION -> {
                showNoInternetConnection(fragment)}
        }
    }

    private fun showNoInternetConnection(activity: FragmentManager) {
        AlertDialogFragment.Builder(R.layout.dialog_confirm)
                .addDismissView(R.id.tv_ok)
                .addDismissView(R.id.tv_cancel)
                .addTextView(R.id.tv_ok,MyApplication.instance.getString(R.string.Text_Confirm__Ok))
                .addTextView(R.id.tv_cancel,MyApplication.instance.getString(R.string.Text_Confirm__TryAgain))
                .addTextView(R.id.tv_title,MyApplication.instance.getString(R.string.ConnectionError_Title__NetworkError))
                .addTextView(R.id.tv_description, MyApplication.instance.getString(R.string.ConnectionError_Text__PleaseCheckYourInternetConnectionAnd))
                .show(activity, NO_INTERNET_CONNECTION)
    }
}