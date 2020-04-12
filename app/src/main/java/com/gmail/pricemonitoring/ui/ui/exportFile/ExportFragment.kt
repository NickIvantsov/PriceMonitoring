package com.gmail.pricemonitoring.ui.ui.exportFile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.pricemonitoring.R
import com.gmail.pricemonitoring.entity.DeviceInfoEntity
import com.gmail.pricemonitoring.ui.DialogFragment
import com.gmail.pricemonitoring.ui.ui.home.UrlViewModel
import com.gmail.pricemonitoring.util.AppApplication
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.util.*


class ExportFragment : Fragment(), DialogFragment.IOnDialogBtnListener {

    private lateinit var exportViewModel: ExportViewModel
    private lateinit var urlViewModel: UrlViewModel
    val data = StringBuilder()
    private lateinit var alertDialog: DialogFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_export, container, false)
        exportViewModel = ViewModelProvider(this).get(ExportViewModel::class.java)
        urlViewModel = ViewModelProvider(this).get(UrlViewModel::class.java)
        alertDialog = DialogFragment(this@ExportFragment)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deleteAllView = view.findViewById<Button>(R.id.button_delete_all)
        deleteAllView.setOnClickListener {
            alertDialog.show(parentFragmentManager,"AlertDialog")
        }
        val exportView: Button = view.findViewById(R.id.button_export)
        data.append("Divice/Brand/Model name,Price")
        exportViewModel.allUrls.observe(viewLifecycleOwner, Observer { words ->
            Observable.create<List<DeviceInfoEntity>>() {
                it.onNext(words)
            }.flatMap {
                Observable.fromIterable(it)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    data.append("\n${it.device},${it.price}")
                }) { ex ->
                    ex.printStackTrace()
                }
        })
        exportView.setOnClickListener {

            //generate data
            try {
                //saving the file into device
                val nameFile = "${Calendar.getInstance().time.toString()}.csv"
                val out: FileOutputStream =
                    context!!.openFileOutput(nameFile, Context.MODE_PRIVATE)
                out.write(data.toString().toByteArray())
                out.close()

                //exporting
                val filelocation = File(context?.filesDir, nameFile)
                val path: Uri = FileProvider.getUriForFile(
                    context!!,
                    "com.gmail.pricemonitoring.fileprovider",
                    filelocation
                )
                val fileIntent = Intent(Intent.ACTION_SEND)
                fileIntent.type = "text/csv"
                fileIntent.putExtra(Intent.EXTRA_SUBJECT, "${Calendar.getInstance().time}")
                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                fileIntent.putExtra(Intent.EXTRA_STREAM, path)
                startActivity(Intent.createChooser(fileIntent, "Send mail"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun positivePressed(dialog: DialogInterface, which: Int) {
        exportViewModel.deleteAll()
        urlViewModel.deleteAll()
        dialog.dismiss()
        Toast.makeText(context, "Данные скоро будут удалены!", Toast.LENGTH_LONG)
            .show()
    }
}
