package com.example.marvelapp.data.utils

enum class Order(val query: String) {
    NAME("name"),
    NAME_DESC("-name"),
    MODIFIED("modified"),
    MODIFIED_DESC("-modified"),
}
