package com.test.test_karim2.feature

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.test.test_karim2.R
import com.test.test_karim2.data.model.Book
import com.test.test_karim2.data.model.BookStokRelation
import com.test.test_karim2.util.UserInterfaceUtil
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_book.view.*

class ItemBook(val bookRelation: BookStokRelation, val returnListener: (Book, Int) -> Unit,
               val borrowListener: (Book, Int) -> Unit,
               val viewImageListener: (BookStokRelation, Int) -> Unit,
               val viewListener: (Book) -> Unit): Item(){
    @SuppressLint("ResourceAsColor")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name_tv.text = UserInterfaceUtil.capitalizeFirstString(bookRelation.books.name)
        viewHolder.itemView.genre_tv.text = "Genre : ${bookRelation.books.genre}"
        viewHolder.itemView.author_tv.text = "Author : ${bookRelation.books.author}"
        viewHolder.itemView.publisher_tv.text = "Publisher : ${bookRelation.books.publisher}"
        viewHolder.itemView.date_published_tv.text = "Date Published : ${bookRelation.books.published_date}"
        viewHolder.itemView.stok_tv.text = "Stok : ${bookRelation.book_stok_stokahir ?: bookRelation.books.qty}"
        val htmlDescTxt = HtmlCompat.fromHtml(UserInterfaceUtil.capitalizeFirstString(bookRelation.books.description), HtmlCompat.FROM_HTML_MODE_LEGACY)
        viewHolder.itemView.description_tv.text = htmlDescTxt
        viewHolder.itemView.view_bt.setOnClickListener {
            viewListener.invoke(bookRelation.books)
        }

        viewHolder.itemView.img_poster.setOnClickListener {
            viewImageListener.invoke(bookRelation, position+1)
        }
        val book_stok_stokahir = bookRelation.book_stok_stokahir
        if (book_stok_stokahir == null){
            if (bookRelation.books.qty <= 0){
                viewHolder.itemView.borrow_bt.isEnabled = false
                viewHolder.itemView.borrow_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_gray_r5)
                viewHolder.itemView.return_bt.isEnabled = false
                viewHolder.itemView.return_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_gray_r5)
            } else {
                viewHolder.itemView.borrow_bt.isEnabled = true
                viewHolder.itemView.borrow_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_chocolate_r5)
                viewHolder.itemView.return_bt.isEnabled = false
                viewHolder.itemView.return_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_gray_r5)
            }
        } else {
            if (book_stok_stokahir <= 0){
                viewHolder.itemView.borrow_bt.isEnabled = false
                viewHolder.itemView.borrow_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_gray_r5)
                viewHolder.itemView.return_bt.isEnabled = true
                viewHolder.itemView.return_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_chocolate_r5)
            } else {
                viewHolder.itemView.borrow_bt.isEnabled = true
                viewHolder.itemView.borrow_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_chocolate_r5)
                if (book_stok_stokahir >= bookRelation.books.qty) {
                    viewHolder.itemView.return_bt.isEnabled = false
                    viewHolder.itemView.return_bt.background =
                        ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_gray_r5)
                }
            }
        }
        viewHolder.itemView.return_bt.setOnClickListener {
            if (book_stok_stokahir != null){
                returnListener.invoke(bookRelation.books, book_stok_stokahir)
            } else {
                viewHolder.itemView.return_bt.isEnabled = false
                viewHolder.itemView.return_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_gray_r5)
                viewHolder.itemView.borrow_bt.isEnabled = true
                viewHolder.itemView.borrow_bt.background =
                    ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.button_chocolate_r5)
            }
            /*viewHolder.itemView.return_bt.isClickable = false
            viewHolder.itemView.return_bt.isFocusable = false
            runBlocking {
                delay(100)
                viewHolder.itemView.return_bt.isClickable = true
                viewHolder.itemView.return_bt.isFocusable = true
            }*/
        }
        viewHolder.itemView.borrow_bt.setOnClickListener {
            if (book_stok_stokahir != null){
                borrowListener.invoke(bookRelation.books, book_stok_stokahir)
            } else {
                borrowListener.invoke(bookRelation.books, bookRelation.books.qty)
            }
            /*viewHolder.itemView.borrow_bt.isClickable = false
            viewHolder.itemView.borrow_bt.isFocusable = false
            runBlocking {
                delay(100)
                viewHolder.itemView.borrow_bt.isClickable = true
                viewHolder.itemView.borrow_bt.isFocusable = true
            }*/
        }
        val ratio = 0.5f
        val metrics = DisplayMetrics()
        (viewHolder.itemView.context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        var screenWidth = metrics.widthPixels
        screenWidth = (screenWidth.toFloat()*ratio).toInt()
        viewHolder.itemView.image_ll.getLayoutParams().width = screenWidth
        //viewHolder.itemView.image_ll.getLayoutParams().height = screenWidth
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_baseline_groups_24)
            .error(R.drawable.ic_baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            //.circleCrop()
        Glide.with(viewHolder.itemView.context)
            .load(bookRelation.books.url)
            .apply(options)
            .into(viewHolder.itemView.img_poster)
    }

    override fun getLayout(): Int = R.layout.item_book

}

