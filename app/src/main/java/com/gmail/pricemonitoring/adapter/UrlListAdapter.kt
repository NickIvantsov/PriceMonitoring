package com.gmail.pricemonitoring.adapter

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.pricemonitoring.R
import com.gmail.pricemonitoring.ui.ui.newUrl.NewUrlActivity
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.parcel.Parcelize


class UrlListAdapter internal constructor(
    private val context: Context?
) : RecyclerView.Adapter<UrlListAdapter.UrlViewHolder>() {

    val data: MutableList<InfoElement> = ArrayList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    inner class UrlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.text_view_url)
        val productTitleView: TextView = itemView.findViewById(R.id.text_view_product_title)
        val categoryView: TextView = itemView.findViewById(R.id.text_view_category)
        val priceView: TextView = itemView.findViewById(R.id.text_view_price)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val codeView: TextView = itemView.findViewById(R.id.text_view_code)

        init {
            wordItemView.setOnClickListener {
                val intent = Intent(it.context, NewUrlActivity::class.java)
                intent.putExtra("VALUE", wordItemView.text)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return UrlViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UrlViewHolder, position: Int) {
        val infoElement = data[position]
        holder.wordItemView.text = infoElement.urlElement
        val tmpUrl = "https://www.citrus.ua/"
        val tmpUrl2 = infoElement.urlElement.indexOf("/", tmpUrl.length, true)
        Observable.create<String> {
            it.onNext(infoElement.urlElement.subSequence(tmpUrl.length, tmpUrl2).toString())
        }.subscribe({
            holder.categoryView.text = it
        }) {
            it.printStackTrace()
        }
        holder.productTitleView.text = infoElement.productTitle.trim()
        holder.priceView.text = infoElement.price.trim()
        holder.codeView.text = infoElement.code.trim()
        Glide.with(context!!).load(infoElement.urlImg).into(holder.imageView);
    }

    internal fun add(newElement: InfoElement) {
        loop@ for (oldElement in data) {
            when (oldElement.urlElement) {
                newElement.urlElement -> {
                    if (oldElement.urlElement == newElement.urlElement) {
                        return
                    }
                    break@loop
                }
            }
        }
        data.add(newElement)
        notifyItemInserted(data.size - 1)
    }

    fun removeAt(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }

    fun removeElement(newElement: String) {
        loop@ for (oldElement in data) {
            when (oldElement.urlElement) {
                newElement -> {
                    data.remove(oldElement)
                    notifyItemInserted(data.size - 1)
                }
            }
        }
    }


    fun clearElements() {
        data.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount() = data.size
}

@Parcelize
data class InfoElement(
    val urlElement: String,
    val productTitle: String,
    val price: String,
    val code: String,
    val urlImg: String
) : Parcelable