

//OUTDATED CLASS THAT IS BEING ARCHIVED. USE FOR REFERENCE



//package com.example.taskapp
//import android.nfc.Tag
//import android.os.Bundle
//import android.os.PersistableBundle
//import android.provider.ContactsContract
//import androidx.appcompat.app.AppCompatActivity
//import com.example.taskapp.Card
//import com.google.android.material.tabs.TabLayout
//
//import java.util.*
//import kotlin.collections.ArrayList
//
//class CardManager() : AppCompatActivity() {
//
//    private var cards:ArrayList<Card> = ArrayList()
//    private lateinit var db:DataManager
//
//    //Initializes Card and adds to the Card Array
//    fun addCard(nm:String){
//
//        var card:Card = Card(nm)
//        cards.add(card)
//    }
//
//    //Removes Card at desired index
//    fun removeCard(index: Int): Boolean{
//        if (index < 0 || index >= cards.size){
//            return false
//        }
//
//        cards.removeAt(index)
//        return true
//    }
//
//    //Edits Card name
//    fun editCard(nm: String, index: Int){
//        cards[index].setName(nm)
//    }
//
//    //Returns name of Card
//    fun getCardName(index: Int):String{
//        return cards[index].getName()
//    }
//
//    //Returns the size of the Card Array
//    fun getCardSize():Int{
//        return cards.size
//    }
//
//    //Returns a Card object at given index
//    fun getCard(index: Int):Card{
//        return cards[index]
//    }
//
//    fun setCards(cardArr: ArrayList<Card>){
//        cards = cardArr
//    }
//}