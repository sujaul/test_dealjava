package com.test.test_karim2.feature

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.test.test_karim2.R
import com.test.test_karim2.data.model.FilmAndFilmstokRelation
import com.test.test_karim2.data.model.Persons
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_event.view.*
import kotlinx.android.synthetic.main.item_film.view.*


class ItemPerson(val persons: Persons, val viewListener: (Persons, Int) -> Unit): Item(){
    @SuppressLint("ResourceAsColor")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txt_name.text = persons.first_name+" "+persons.last_name

        viewHolder.itemView.setOnClickListener {
            viewListener(persons, position)
        }

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_baseline_groups_24)
            .error(R.drawable.ic_baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(viewHolder.itemView.context)
            .load(persons.avatar)
            .apply(options)
            .into(viewHolder.itemView.img_avatar)

    }

    override fun getLayout(): Int = R.layout.item_event

}

class ItemFilm(val film: FilmAndFilmstokRelation, val viewListener: (FilmAndFilmstokRelation, Int) -> Unit): Item(){
    @SuppressLint("ResourceAsColor")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name_tv.text = film.films.name
        viewHolder.itemView.genre_tv.text = "Genre : ${film.films.genre}"
        viewHolder.itemView.stok_tv.text = "Stok : ${film.film_stok_ahir.toString()}"
        viewHolder.itemView.description_tv.text = film.films.description
        viewHolder.itemView.setOnClickListener {
        }
        /*val ratio = 0.35f
        val metrics = DisplayMetrics()
        (viewHolder.itemView.context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        var screenWidth = metrics.widthPixels
        screenWidth = (screenWidth.toFloat()*ratio).toInt()
        viewHolder.itemView.img_guest.getLayoutParams().width = screenWidth
        viewHolder.itemView.img_guest.getLayoutParams().height = screenWidth*/
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_baseline_groups_24)
            .error(R.drawable.ic_baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            //.circleCrop()
        Glide.with(viewHolder.itemView.context)
            .load(film.films.url)
            .apply(options)
            .into(viewHolder.itemView.img_poster)
    }

    override fun getLayout(): Int = R.layout.item_film

}

