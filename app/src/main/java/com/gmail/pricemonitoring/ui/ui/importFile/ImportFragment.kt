package com.gmail.pricemonitoring.ui.ui.importFile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.pricemonitoring.R

class ImportFragment : Fragment() {

    private lateinit var importViewModel: ImportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        importViewModel =
            ViewModelProvider(this).get(ImportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_import, container, false)
        val textView: TextView = root.findViewById(R.id.text_import)
        importViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
