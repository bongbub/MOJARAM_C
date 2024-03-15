package com.example.mojaram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mojaram.databinding.FragmentMyReviewsBinding

class meFragment : Fragment() {

    companion object{
        const val TAG: String = "마이페이지 로그"

        fun newInstance() : meFragment {
            return meFragment()
        }
    }


    // 프래그먼트와 레이아웃 연결
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        Log.d(TAG, "MeFregment - OnCreateView() called")

        val view = inflater.inflate(R.layout.fragment_me, container, false)

        // 리뷰 내역
        val myreviewPage : Button = view.findViewById(R.id.btn_my1)
        myreviewPage.setOnClickListener{
            //val intent = Intent(requireContext(), MyReviews::class.java)
            //startActivity(intent)
            // 위의 코드는 activity만 되는 듯

            //val fragment = MyReviews.newInstance()
            //parentFragmentManager.beginTransaction()
            //    .replace(R.id.fragment_container, fragment)
            //    .addToBackStack(null)
            //    .commit()
            val fragment = MyReviews.newInstance()
            addFragment(fragment)
        }
        fun addFragment(fragment: Fragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        // 예약 내역
        val reserHis : Button = view.findViewById(R.id.btn_my2)
        reserHis.setOnClickListener{
            val intent = Intent(requireContext(), ReservationHistory::class.java)
            startActivity(intent)
        }

        // 상담 내역
        val consulhis : Button = view.findViewById(R.id.btn_my3)
        consulhis.setOnClickListener {
            val intent = Intent(requireContext(), consultationHistory::class.java)
            startActivity(intent)
        }

        // 찜 목록 페이지
        val bookmark : Button = view.findViewById(R.id.btn_my4)
        bookmark.setOnClickListener{
            val intent = Intent(requireContext(), Bookmark::class.java)
            startActivity(intent)
        }

        // 환경 설정 페이지
        val settings : Button = view.findViewById(R.id.btn_my5)
        settings.setOnClickListener{
            val intent = Intent(requireContext(), settingsPage::class.java)
            startActivity(intent)
        }

        return view

        }
    }

