package com.gmail.pricemonitoring.ui.ui.newUrl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gmail.pricemonitoring.R
import com.gmail.pricemonitoring.entity.UrlEntity
import com.gmail.pricemonitoring.ui.ui.exportFile.ExportViewModel
import com.gmail.pricemonitoring.ui.ui.home.UrlViewModel
import com.google.zxing.integration.android.IntentIntegrator

class NewUrlActivity : AppCompatActivity() {

    private lateinit var urlViewModel: UrlViewModel

    private lateinit var editWordView: EditText

    private lateinit var exportViewModel: ExportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_url)
        editWordView = findViewById(R.id.edit_word)
        urlViewModel = ViewModelProvider(this).get(UrlViewModel::class.java)
        exportViewModel = ViewModelProvider(this).get(ExportViewModel::class.java)

        val value: String? = intent.getStringExtra("VALUE")

        value?.let {
            editWordView.text = Editable.Factory.getInstance().newEditable(value)
        }

        val buttonQrScan = findViewById<ImageView>(R.id.button_qr_scaner)
        buttonQrScan.setOnClickListener {
            //открываем окно для сканирования qrCode
            val integrator = IntentIntegrator(this)
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            val replyIntent = Intent()
            if (TextUtils.isEmpty(editWordView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                value?.let {
                    val oldUrlEntity = UrlEntity(it)
                    urlViewModel.delete(oldUrlEntity)
                }
                val url = editWordView.text.toString()
                val newUrlEntity = UrlEntity(url)
                urlViewModel.insert(newUrlEntity)
                replyIntent.putExtra(EXTRA_REPLY, url)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        val buttonDelete = findViewById<Button>(R.id.button_delete)
        buttonDelete.setOnClickListener {
            value?.let {
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_CLEAR, it)
                setResult(Activity.RESULT_OK, replyIntent)
                val oldUrlEntity = UrlEntity(it)
                urlViewModel.delete(oldUrlEntity)
                exportViewModel.deleteAll()
            }
            finish()
        }
    }

    //endregion
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(
                    this,
                    "cancelled", Toast.LENGTH_LONG
                ).show()
            } else {
                editWordView.setText("")
                editWordView.setText(result.contents)
                Toast.makeText(
                    this,
                    "scanned" + result.contents,
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.gmail.pricemonitoring.ui.REPLY"
        const val EXTRA_CLEAR = "com.gmail.pricemonitoring.ui.CLEAR"
    }
}
