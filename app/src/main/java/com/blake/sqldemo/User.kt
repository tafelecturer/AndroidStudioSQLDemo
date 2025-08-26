package com.blake.sqldemo

class User (var id: Int, val name:String , val age: String) {
    override fun toString(): String { // return the record detail
        return "$id: $name($age)"
    }
}