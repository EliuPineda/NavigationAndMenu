package com.eargel.navandmen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    var score = 0

    val images = listOf(
        R.drawable.image__000, R.drawable.image__001,
        R.drawable.image__002, R.drawable.image__003,
        R.drawable.image__004, R.drawable.image__005,
        R.drawable.image__006, R.drawable.image__007,
        R.drawable.image__008, R.drawable.image__009,
        R.drawable.image__010, R.drawable.image__011,
        R.drawable.image__012, R.drawable.image__013,
        R.drawable.image__014, R.drawable.image__015,
        R.drawable.image__016, R.drawable.image__017,
        R.drawable.image__018, R.drawable.image__019,
        R.drawable.image__020, R.drawable.image__021,
        R.drawable.image__018,R.drawable.image__015,
        R.drawable.image__016,R.drawable.image__020,
        R.drawable.image__002
    )

    var awaitingCard: ImageView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_third).setOnClickListener {
            findNavController().navigateUp()
        }

        val cards = view.findViewById<ConstraintLayout>(R.id.memory_layout).children.toList().filterIsInstance<ImageView>()

        view.findViewById<Button>(R.id.button_reset).setOnClickListener {
            score = 0
            view.findViewById<TextView>(R.id.memoryscore_text).text = "${score}%"
            for (card in cards) {
                card.setImageResource(R.drawable.empty)
                card.isClickable = true
                card.tag = null
            }
        }

        cards.forEach {
                card -> card.setOnClickListener {
                    if (card.tag == null) {
                        val imgs = images.shuffled().slice(0..9)
                        val pairs = ((0..9) + (0..9)).shuffled()
                        for (i in 0..19) cards[i].tag = imgs[pairs[i]]
                    }

                    card.setImageResource(card.tag as Int)
                    card.isClickable = false

                    if (awaitingCard == null) {
                        awaitingCard = card
                    } else if (awaitingCard!!.tag == card.tag) {
                        score += 10
                        view.findViewById<TextView>(R.id.memoryscore_text).text = "${score}%"

                        card.tag = null
                        awaitingCard!!.tag = null
                        awaitingCard = null
                        // if (cards.all { it.tag == null }) {
                        //    JUGADOR GANÓ
                        // }
                    } else {
                        GlobalScope.launch(context = Dispatchers.Main) {
                            for (card in cards) if (card.tag != null) card.isClickable = false

                            delay(600)
                            card.setImageResource(R.drawable.empty)
                            awaitingCard!!.setImageResource(R.drawable.empty)
                            awaitingCard = null
                            delay(600)
                            card.setImageResource(card.tag as Int)


                            for (card in cards) if (card.tag != null) card.isClickable = true
                        }
                    }
                }
        }
    }
}