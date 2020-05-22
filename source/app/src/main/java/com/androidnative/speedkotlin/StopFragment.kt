package com.androidnative.speedkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

const val ARG_PARAM1 = "ARG_PARAM1"

class StopFragment : Fragment() {
    private var mAverage: Short = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mAverage = it.getShort(ARG_PARAM1, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_stop, container, false)

        val speed : TextView = view.findViewById(R.id.result_speed)
        val average : TextView = view.findViewById(R.id.title_stop_result)

        speed.text = "$mAverage"
        average.visibility = if (mAverage == 0.toShort()) View.INVISIBLE else View.VISIBLE

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Short) =
            StopFragment().apply {
                arguments = Bundle().apply {
                    putShort(ARG_PARAM1, param1)
                }
            }
    }
}
