package io.ashdavies.playground.conferences

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal class ErrorDialogFragment : DialogFragment() {

    private val navArgs: ErrorDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setMessage(navArgs.message)
            .setTitle(navArgs.title)
            .create()
    }
}
