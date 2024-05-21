package com.example.mojaram

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mojaram.ui.login.vrPage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference


class HomeFragment : Fragment() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private val PICK_IMAGE_REQUEST = 1 //이미지 선택

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        mAuth = FirebaseAuth.getInstance()

        val banner: LinearLayout = rootView.findViewById(R.id.vrhairEX)
        val gridban: GridLayout = rootView.findViewById(R.id.grid)

        // xml에 있는 요소들
        val viewPager2 : ViewPager2 = rootView.findViewById(R.id.viewpager2)
        val tabLayout : TabLayout = rootView.findViewById(R.id.tablayout)

        //임시 이미지 업로드 버튼
        val imgBtn: Button = rootView.findViewById(R.id.imgbtn)
        imgBtn.setOnClickListener { openFileChooser() } //버튼 클릭시 파일 선택
        //이미지 업로드 구현


        // 임시 로그아웃 버튼 가져오기
        val logoutBtn: Button = rootView.findViewById(R.id.testLogoutBtn)

        // 로구아웃 구현해보기
        logoutBtn.setOnClickListener{
            mAuth.signOut()  //Firebase 로그아웃
            val intent = Intent(requireContext(), LogInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        val testbtnbtn : Button = rootView.findViewById(R.id.testbtnbtn)
        testbtnbtn.setOnClickListener{
            val intent = Intent(requireContext(), Reservation::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        viewPager2.apply {
            adapter = MyPageradapter(context as FragmentActivity)
        }

        TabLayoutMediator(tabLayout, viewPager2) {tab, position->
            when(position){
                0->{
                    tab.text="안양동 추천"
                }
                1->{
                    tab.text="스타일 추천"
                }
                else->{
                    tab.text="홈"
                }

            }
        }.attach()

        banner.setOnClickListener {
            val intent = Intent(requireContext(), vrPage::class.java)
            startActivity(intent)
        }
        gridban.setOnClickListener {
            val intent = Intent(requireContext(), vrPage::class.java)
            startActivity(intent)

        }
        return rootView
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)  // 이미지 선택
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            uploadImageToFirebase(imageUri)  // 선택한 이미지를 업로드
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        imageUri?.let {
            val fileReference = storageRef.child("images/${System.currentTimeMillis()}.jpg")

            fileReference.putFile(imageUri)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }

        companion object {
            const val TAG: String = "홈 로그"

            fun newInstance(): HomeFragment {
                return HomeFragment()
            }
        }

}