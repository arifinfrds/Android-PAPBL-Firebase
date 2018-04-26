package com.arifinfrds.codechallangeutsarifin.extension

import android.content.Context
import android.widget.Toast

/**
 * Created by arifinfrds on 4/25/18.
 */


fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()