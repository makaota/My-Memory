package com.makaota.mymemory.models

import com.makaota.mymemory.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, private  val customImages: List<String>?) {


    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0


    private var indexOfSingleSelectedCards: Int? = null

    init {
        if (customImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it) }
        } else{
            val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map{MemoryCard(it.hashCode(), it)}
        }
    }

    fun flipCard(position: Int) : Boolean{

        val card = cards[position]

        numCardFlips++
        var foundMatch = false

        if(indexOfSingleSelectedCards == null){

            restoreCards()
            indexOfSingleSelectedCards = position
        }else
        {
            foundMatch = checkForMatch(indexOfSingleSelectedCards!!, position) as Boolean
            indexOfSingleSelectedCards = null

        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Any {
        if (cards[position1].identifier != cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards){
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int{

        return numCardFlips / 2
    }

}