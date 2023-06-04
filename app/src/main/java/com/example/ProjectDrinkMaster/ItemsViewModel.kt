
package com.example.ProjectDrinkMaster

import android.media.Image

class ItemsViewModel(text: String?, image: Int, title: String?) {
    private var text: String
    private var image: Int
    private var title: String

    init {
        this.text = text!!
        this.image = image!!
        this.title = title!!
    }

    fun getText(): String? {
        return text
    }
    fun setText(name: String?) {
        text = name!!
    }
    fun image(): Int? {
        return image
    }
    fun setYear(image: Int?) {
        this.image = image!!
    }
    fun getTitle(): String? {
        return title
    }
    fun setTitle(genre: String?) {
        this.title = genre!!
    }

}