package com.example.mojaram.admin

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var binding: ActivityAdminSignUpBinding

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener{
            val shop_name = binding.editTextShopname.text.toString()
            val shop_addr = binding.editTextShopaddress.text.toString()
            val shop_addr2 = binding.editTextShopaddress2.text.toString()
            val shop_num = binding.editTextShopNum.text.toString()
            val shop_auth = binding.editTextAuth.text.toString()
            val shop_hour = binding.editTextShopHours.text.toString()
            val shop_price = binding.editTextShopPrice.text.toString()

            if (shop_name.isNotEmpty() && shop_addr.isNotEmpty() && shop_addr2.isNotEmpty() && shop_num.isNotEmpty()
                && shop_auth.isNotEmpty() && shop_hour.isNotEmpty() && shop_price.isNotEmpty()){
                signUpAdmin(shop_name, shop_addr, shop_addr2, shop_num, shop_auth, shop_hour, shop_price)
            }else{
                Toast.makeText(this,"모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 전화번호 형식 추가
        binding.editTextShopNum.addTextChangedListener(object : TextWatcher {
            private var isFormatting: Boolean = false
            private var prevText: String = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 저장된 이전 텍스트를 사용해 변화 감지
                prevText = s.toString()
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 변환 중 상태를 확인
                if (isFormatting) return
            }
            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                isFormatting = true

                // 기존 입력 텍스트하이픈 제거
                val currentText = s.toString().replace("-", "")
                // 입력된 텍스트 길이 파악 후 하이픈 추가
                val formattedText = when {
                    currentText.length > 7 -> "${currentText.substring(0, 3)}-${currentText.substring(3, 7)}-${currentText.substring(7)}"
                    currentText.length > 3 -> "${currentText.substring(0, 3)}-${currentText.substring(3)}"
                    else -> currentText
                }
                // 업데이트 시 이미 있는 텍스트의 커서 위치 조정
                if (formattedText != prevText) {
                    binding.editTextShopNum.setText(formattedText)
                    binding.editTextShopNum.setSelection(formattedText.length)
                }

                isFormatting = false
            }
        })

        binding.pricePlusbtn.setOnClickListener{

        }
    }

    private fun signUpAdmin(
        shop_name: String, shop_addr: String, shop_addr2:String, shop_num:String, shop_auth:String, shop_hour:String, shop_price:String){

        val user = auth.currentUser
        if(user != null){
            val email = user.email ?: return
            val adminData = hashMapOf(
                "shopName" to shop_name,
                "shopAddr" to shop_addr,
                "shopAddr2" to shop_addr2,
                "shopNum" to shop_num,
                "shopAuth" to shop_auth,
                "shopHour" to shop_hour,
                "shopPrice" to shop_price
            )

            db.collection("user_admin").document(email).set(adminData)
                .addOnSuccessListener {
                    Toast.makeText(this,"제출이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener{ e ->
                    Toast.makeText(this,"제출이 실패하였습니다 : ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(this,"로그인 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }
}