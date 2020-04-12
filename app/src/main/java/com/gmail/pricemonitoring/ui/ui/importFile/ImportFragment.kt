package com.gmail.pricemonitoring.ui.ui.importFile

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import com.gmail.pricemonitoring.R
import com.gmail.pricemonitoring.entity.UrlEntity
import com.gmail.pricemonitoring.ui.ui.home.UrlViewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class ImportFragment : Fragment() {

    private lateinit var importViewModel: ImportViewModel
    private lateinit var urlViewModel: UrlViewModel

    private lateinit var dialog: FilePickerDialog
    var properties = DialogProperties()
    val urlNeedInsert: StringBuilder = java.lang.StringBuilder("")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        importViewModel =
            ViewModelProvider(this).get(ImportViewModel::class.java)
        urlViewModel =
            ViewModelProvider(this).get(UrlViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_import, container, false)

        properties.selection_mode = DialogConfigs.SINGLE_MODE
        properties.selection_type = DialogConfigs.FILE_SELECT
        properties.root = File(DialogConfigs.DEFAULT_DIR)
        properties.error_dir = File(DialogConfigs.DEFAULT_DIR)
        properties.offset = File(DialogConfigs.DEFAULT_DIR)
        properties.extensions = null
        properties.show_hidden_files = false

        val dialog = FilePickerDialog(context, properties)
        dialog.setTitle("Select a File")
        dialog.setDialogSelectionListener {
            it.forEach { path ->
                println("path: $path")
                try {
                    FileInputStream(path).use { fin ->
                        var i = -1
                        loop@ while (fin.read().also { count -> i = count } != -1) {
                            when (i) {
                                44 -> {
                                    val urlEntity = UrlEntity(urlNeedInsert.toString())
                                    urlViewModel.insert(urlEntity)
                                    urlNeedInsert.clear()
                                    continue@loop
                                }
                            }
                            urlNeedInsert.append(i.toChar())
                            println("i = $i; ${i.toChar()}")
                        }
                    }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }

        val importView: Button = root.findViewById(R.id.button_import)

        importView.setOnClickListener {
            dialog.show()
        }
        return root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.show()
                } else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(
                        context,
                        "Permission is Required for getting list of files",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
