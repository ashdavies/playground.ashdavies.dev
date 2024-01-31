package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame

@Composable
internal fun FileDialog(
    parent: Frame? = null,
    title: String = String(),
    mode: Int = FileDialog.LOAD,
    onCreate: (FileDialog) -> Unit = { },
    onClose: (String?, String?) -> Unit,
) {
    AwtWindow(
        create = {
            object : FileDialog(parent, title, mode) {
                override fun setVisible(value: Boolean) {
                    super.setVisible(value)

                    if (value) {
                        onClose(directory, file)
                    }
                }
            }.also(onCreate)
        },
        dispose = FileDialog::dispose,
    )
}
