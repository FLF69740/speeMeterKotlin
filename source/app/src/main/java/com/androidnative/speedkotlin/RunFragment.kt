package com.androidnative.speedkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_run.*

class RunFragment : Fragment() {

    private lateinit var speedText: TextView
    private lateinit var averageText: TextView

    companion object{ fun newInstance(): RunFragment = RunFragment()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_run, container, false)

        speedText = view.findViewById(R.id.counter_speed)
        averageText = view.findViewById(R.id.average)

        return view
    }

    fun updateUI(speed: String, averageResult: String){
        speedText.text = speed
        averageText.text = averageResult
    }

}
