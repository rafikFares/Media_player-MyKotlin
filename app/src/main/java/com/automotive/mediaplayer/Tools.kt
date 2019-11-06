package com.automotive.mediaplayer

fun <E> MutableList<E>.addNotExistingItems(list: MutableList<E>) {
    list.forEach{
        if (it !in this) this.add(it)
    }
}


fun List<Int>.allNonZero() =  all { 0 !in this }

fun List<Int>.containsZero() =  any { 0 in this }