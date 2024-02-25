package com.example.mojaram

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class mapFragment : Fragment() {

    companion object{
        const val TAG : String = "로그"

        fun newInstance() : mapFragment{
            return mapFragment()
        }
    }

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        Log.d(TAG, "맵 프레그먼트 onCreate() 실행")
    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context){
        super.onAttach(context)
    }

    // 뷰가 생성되었을 때
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "맵화면 oncreateview 실행")
        val view = inflater.inflate(R.layout.activity_map,container,false)
        return view

    }

    fun getMapAsync(mapActivity: MapActivity) {

    }

}