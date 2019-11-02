package com.automotive.mediaplayer

fun <E> MutableList<E>.addNotExistingItems(list: MutableList<E>) {
    list.forEach{
        if (it !in this) this.add(it)
    }
}
