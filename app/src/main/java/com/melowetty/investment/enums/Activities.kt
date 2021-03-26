package com.melowetty.investment.enums

import android.content.Context
import android.content.Intent
import com.melowetty.investment.activities.MainActivity
import com.melowetty.investment.activities.SearchActivity

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