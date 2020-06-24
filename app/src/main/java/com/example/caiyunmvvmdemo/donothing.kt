package com.example.caiyunmvvmdemo

fun main(){
    val list= mutableListOf<Int>()

    for(i in 1..5) list.add(i)

    var x=0
    for(i in 1..6) x=x xor i

    println(list.find { it==5 })




}