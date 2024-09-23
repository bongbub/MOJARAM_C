package com.example.mojaram.admin

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityAdminSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminSignUp : AppCompatActivity() {

    private lateinit var editTextStoreName: EditText
    private lateinit var editTextAddress1:EditText
    private lateinit var editTextAddress2:EditText
    private lateinit var editTextPhone:EditText
    private lateinit var editTextProof: EditText
    private lateinit var editTextHours: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var btnAdminSignUp: Button

    private lateinit var binding: ActivityAdminSignUpBinding

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // SignUpActivity에서 전달된 정보 받기
        val nickname = intent.getStringExtra("nickname")
        val useremail = intent.getStringExtra("useremail")
        val userGender = intent.getStringExtra("userGender")
        val userbirth = intent.getStringExtra("userbirth")
        val password = intent.getStringExtra("password")

        binding.buttonSignUp.setOnClickListener{
            val shop_name = binding.editTextShopname.text.toString().trim()
            val shop_addr = binding.editTextShopaddress.text.toString()
            val shop_addr2 = binding.editTextShopaddress2.text.toString()
            val shop_num = binding.editTextShopNum.text.toString().trim()
            val shop_auth = binding.editTextAuth.text.toString()      //수정
            val shop_hour = binding.editTextShopHours.text.toString()
            val shop_price = binding.editTextShopPrice.text.toString()

            if (shop_name.isEmpty() || shop_addr.isEmpty() || shop_addr2.isEmpty() || shop_auth.isEmpty() || shop_hour.isEmpty() || shop_price.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 파이어스토어에 저장
            addAdminToFirestore(nickname, useremail, password, userGender, userbirth, shop_name, shop_addr, shop_addr2, shop_num, shop_auth, shop_hour, shop_price )

        }
//        // 전화번호 형식 추가
//        binding.editTextShopNum.addTextChangedListener(object : TextWatcher {
//            private var isFormatting: Boolean = false
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable?) {
//                if (isFormatting) return
//
//                isFormatting = true
//
//                val currentText = s.toString().replace("-", "")
//                val formattedText = when {
//                    currentText.length > 7 -> "${currentText.substring(0, 3)}-${currentText.substring(3, 7)}-${currentText.substring(7)}"
//                    currentText.length > 3 -> "${currentText.substring(0, 3)}-${currentText.substring(3)}"
//                    else -> currentText
//                }
//
//                binding.editTextShopNum.setText(formattedText)
//                binding.editTextShopNum.setSelection(formattedText.length)
//
//                isFormatting = false
//            }
//        })
    }

    private fun addAdminToFirestore(
        nickname: String?, useremail: String?, password:String?, userGender:String?, userbirth:String?,
        shop_name:String?,shop_addr:String?,shop_addr2:String?,shop_num:String?,
        shop_auth:String?,shop_hour:String?,shop_price:String?){
        val adminData = mapOf(
            "nickname" to nickname,
            "useremail" to useremail,
            "password" to password,
            "userGender" to userGender,
            "userbirth" to userbirth,
            "shop_name" to shop_name,
            "shop_addr" to shop_addr,
            "shop_addr2" to shop_addr2,
            "shop_num" to shop_num,
            "shop_auth" to shop_auth,
            "shop_hour" to shop_hour,
            "shop_price" to shop_price,
            "role" to "001"
        )

        db.collection("user_admin").document(useremail!!).set(adminData)
            .addOnSuccessListener {
                Toast.makeText(this,"요청이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this,"요청 실패 : ${e.message}", Toast.LENGTH_SHORT).show()
                Log.w("Firestore","파이어스토어에 관리자 정보 업로드 오류",e)
            }
    }
}