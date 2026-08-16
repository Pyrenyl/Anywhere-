package com.absinthe.anywhere_.view.home

import android.content.Context
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.GlobalValues
import com.absinthe.libraries.utils.extensions.getColorByAttr
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView

object FabBuilder {

  fun build(fab: SpeedDialView) {
    val context = fab.context
    fab.addActionItem(
      create(
        context,
        R.id.fab_advanced,
        R.drawable.ic_advanced_card,
        context.getString(R.string.btn_add_advanced_card)
      )
    )
    fab.addActionItem(
      create(
        context,
        R.id.fab_collector,
        R.drawable.ic_logo,
        GlobalValues.collectorMode
      )
    )
    fab.addActionItem(
      create(
        context,
        R.id.fab_activity_list,
        R.drawable.ic_activity_list,
        context.getString(R.string.btn_activity_list)
      )
    )
  }

  private fun create(context: Context, id: Int, iconRes: Int, label: String): SpeedDialActionItem {
    return SpeedDialActionItem.Builder(id, iconRes)
      .setFabBackgroundColor(context.getColorByAttr(com.google.android.material.R.attr.colorSurface))
      .setFabImageTintColor(context.getColorByAttr(com.google.android.material.R.attr.colorPrimary))
      .setLabel(label)
      .setLabelClickable(false)
      .create()
  }
}
