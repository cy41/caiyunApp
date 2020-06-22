package com.example.caiyunmvvmdemo

fun main(){
    val list= mutableListOf<Int>()

    for(i in 1..5) list.add(i)

    println(list.find { it==5 })




}