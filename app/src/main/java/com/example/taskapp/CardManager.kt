package com.example.taskapp
import com.example.taskapp.Card

import java.util.*

class CardManager {
    private var cards:Vector<Card> = Vector()

    fun addCard(nm:String){
        var card:Card = Card(nm)
        cards.addElement(card)
    }
    fun getCardName(pos:Int):String{
        return cards.get(pos).getName()
    }
    fun getCardSize():Int{
        return cards.size
    }
}