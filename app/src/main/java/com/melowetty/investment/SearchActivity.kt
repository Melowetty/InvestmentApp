package com.melowetty.investment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener

class SearchActivity : AppCompatActivity() {
    lateinit var search: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val back = findViewById<ImageView>(R.id.search_back)
        val clear = findViewById<ImageView>(R.id.clear)
        search = findViewById(R.id.search_label)

        back.setOnClickListener {
            val stockView = Intent(this, MainActivity::class.java)
            startActivity(stockView)
        }

        search.addTextChangedListener {
            if (it != null) {
                if (it.isNotEmpty()) clear.visibility = View.VISIBLE
                else clear.visibility = View.GONE
            }
        }

        clear.setOnClickListener {
            search.text = null
            clear.visibility = View.GONE
        }
    }
}