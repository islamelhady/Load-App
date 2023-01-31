package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


        val extras = intent.extras
        if (extras != null) {
            val fileName = extras.getString(getString(R.string.key_file_name))
            val fileDescription = extras.getString(getString(R.string.key_description))
            val status = extras.getString(getString(R.string.key_status))


            fileNameView.text = fileName
            fileDescriptionView.text = fileDescription
            fileStatusView.text = status

            when (status) {
                getString(R.string.success) -> fileStatusView.setTextColor(
                    resources.getColor(
                        R.color.green,
                        null
                    )
                )
                getString(R.string.failed) -> fileStatusView.setTextColor(
                    resources.getColor(
                        R.color.red,
                        null
                    )
                )
                getString(R.string.unknown) -> fileStatusView.setTextColor(
                    resources.getColor(
                        R.color.red,
                        null
                    )
                )
            }
            fileStatusView.text = status
        }

        okButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


