package com.example.restaurantkodatagetter

data class Steps
    (val UUID: String, val userId: String,val numberOfSteps:Int,  val time: String, val location: String, var enabled: Boolean) {
}