package io.ashdavies.databinding.common

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

internal abstract class BaseActivity : AppCompatActivity() {

  @get:LayoutRes
  protected abstract val layoutResId: Int

  @get:IdRes
  protected abstract val toolbarId: Int

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(layoutResId)
    setSupportActionBar(findViewById(toolbarId))
  }
}
