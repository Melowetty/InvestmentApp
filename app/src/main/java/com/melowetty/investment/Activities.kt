package com.melowetty.investment

import android.content.Context
import android.content.Intent

enum class Activities {
    MAIN {
        override fun backToOldActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("target", MAIN)
            context.startActivity(intent)
        }
    },
    SEARCH {
        override fun backToOldActivity(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    },
    FAVOURITE {
        override fun backToOldActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("target", FAVOURITE)
            context.startActivity(intent)
        }
    };
    abstract fun backToOldActivity(context: Context)
}