package com.gmail.pricemonitoring.ui.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pricemonitoring.R
import com.gmail.pricemonitoring.adapter.InfoElement
import com.gmail.pricemonitoring.adapter.UrlListAdapter
import com.gmail.pricemonitoring.entity.DeviceInfoEntity
import com.gmail.pricemonitoring.entity.UrlEntity
import com.gmail.pricemonitoring.ui.ui.exportFile.ExportViewModel
import com.gmail.pricemonitoring.ui.ui.newUrl.NewUrlActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var urlViewModel: UrlViewModel

    private lateinit var exportViewModel: ExportViewModel
    private lateinit var totalsView: TextView
    private lateinit var updateView: TextView
    lateinit var adapter: UrlListAdapter
    private lateinit var observable: Disposable


    companion object {
        const val newWordActivityRequestCode = 1
        const val changeWordActivityRequestCode = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        urlViewModel = ViewModelProvider(this).get(UrlViewModel::class.java)
        exportViewModel = ViewModelProvider(this).get(ExportViewModel::class.java)

        totalsView = root.findViewById(R.id.text_view_totals)
        updateView = root.findViewById(R.id.text_view_update)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerview)
        adapter = UrlListAdapter(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        urlViewModel.allUrls.observe(viewLifecycleOwner, Observer { words ->
            adapter.clearElements()
            println("words = $words")
            var coun = 0
            totalsView.text = "Общее количество: ${words.size}"
            // adapter.clearElements()
            // Update the cached copy of the words in the adapter.
            words?.let { list ->
                observable = Observable.create<List<UrlEntity>> { observable ->
                    observable.onNext(list)
                }.flatMap {
                    Observable.fromIterable(it)
                }.map { urlEntity ->
                    var infoElement: InfoElement
                    try {
                        val doc: Document = Jsoup.connect(urlEntity.url).get()
                        val productTitle = doc.select("h1.product__title").first()

                        val codeDiv = doc.select("div.product__sku")
                        val codeSpan = codeDiv.select("span.code").first()
                        val code = codeSpan.text()


                        var priceAllElement: Elements
                        var priceAllElementTwo: Element
                        var price: String
                        val priceDivThree: Element
                        try {
                            //если есть скиндка нам надо выбирать второй элемент
                            val priceDivOne = doc.select("div.buy-block__base").first()
                            val priceDivTwo = priceDivOne.select("div.normal").first()
                            priceDivThree = priceDivTwo.select("div.normal__prices").first()

                            priceAllElement = priceDivThree.select("div.price")
                            priceAllElementTwo = priceAllElement[1].select("span").first()
                            price = priceAllElementTwo.text()
                        } catch (ex: Exception) {
                            //если скидки нет то берем первый
                            priceAllElement = doc.select("div.price")
                            priceAllElementTwo = priceAllElement.select("span").first()
                            price = priceAllElementTwo.text()
                        }


                        val divImgOne = doc.select("div.carousel__items.swiper-wrapper")
                        val image: Element = divImgOne.select("img").first()
                        val url: String = image.absUrl("data-cfsrc")

                        infoElement = InfoElement(
                            urlEntity.url,
                            productTitle.text(),
                            price,
                            code,
                            url
                        )
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                        infoElement = InfoElement(
                            urlEntity.url,
                            "error",
                            "error",
                            "error",
                            "error"
                        )
                    }
                    infoElement
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val deviceEntity =
                            DeviceInfoEntity(it.urlElement, it.productTitle, it.price)
                        exportViewModel.insert(deviceEntity)
                        adapter.add(it)
                        coun++
                        updateView.text = "$coun"
                        println(it)
                    }) {
                        it.printStackTrace()
                    }
            }
        })
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewUrlActivity.EXTRA_REPLY)?.let {
                val urlEntity = UrlEntity(it)
                urlViewModel.insert(urlEntity)
            }
        } else {
            Toast.makeText(
                context,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        observable.dispose()
    }
}
