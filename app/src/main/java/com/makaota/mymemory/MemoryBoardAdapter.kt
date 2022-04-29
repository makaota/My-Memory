package com.makaota.mymemory


import android.content.Context
import android.util.Log
import android.util.LogPrinter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.makaota.mymemory.models.BoardSize
import com.makaota.mymemory.models.MemoryCard
import com.squareup.picasso.Picasso
import java.security.PrivateKey
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickedListener: CardClickListener
)
    : RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object{
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }

    interface CardClickListener {
        fun onCardClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryBoardAdapter.ViewHolder {


        val cardWidth = parent.width / boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight = parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength = min(cardWidth,cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)

        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)



        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoryBoardAdapter.ViewHolder, position: Int) {

        holder.bind(position)
    }

    override fun getItemCount() = boardSize.numCards


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int) {
            val memoryCard = cards[position]

            if(memoryCard.isFaceUp){
                if (memoryCard.imageUrl != null){
                    Picasso.get().load(memoryCard.imageUrl).placeholder(R.drawable.ic_image).into(imageButton)
                }else{
                    imageButton.setImageResource(memoryCard.identifier)
                }
            }else {
                imageButton.setImageResource(R.drawable.ic_launcher_background)
            }
            imageButton.alpha = if (memoryCard.isMatched) .4f else 1.0f
            val colorStateList = if (memoryCard.isMatched) ContextCompat.getColorStateList(context, R.color.color_gray) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)

            imageButton.setOnClickListener(View.OnClickListener {
                Log.i(TAG,"Clicked on position $position")
                cardClickedListener.onCardClicked(position)
            })
        }

    }

}
