package com.example.mojaram.withdrawal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityWithdrawalBinding

class WithdrawalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWithdrawalBinding
    private lateinit var optionAdapter: WithdrawalOptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdrawal)

        with(binding) {
            with(imageviewBack) {
                setOnClickListener {
                    finish()
                }
            }
            with(rvOptionList) {
                optionAdapter = WithdrawalOptionAdapter().apply {
                    unitSelect {
                        binding.isSelected = it.isSelected
                    }
                }
                adapter = optionAdapter
                optionAdapter.submitList(getOptions())
            }
        }
    }

    private fun getOptions(): List<WithdrawalOptionModel> {
        return arrayListOf(
            WithdrawalOptionModel(
                optionText = "불편했던 부분을 직접 작성할게요.",
                isSelected = false,
                isUserInput = true,
            ),
            WithdrawalOptionModel(
                optionText = "항공권을 구매해서 더 이상 안 필요해요",
                isSelected = false,
                isUserInput = false,
            ),
            WithdrawalOptionModel(
                optionText = "여행 계획이 취소돼서 더 이상 안 필요해요",
                isSelected = false,
                isUserInput = false,
            ),
            WithdrawalOptionModel(
                optionText = "이용 방법이 너무 복잡해요",
                isSelected = false,
                isUserInput = false,
            ),
            WithdrawalOptionModel(
                optionText = "찾는 지역의 정보가 없어요",
                isSelected = false,
                isUserInput = false,
            ),
            WithdrawalOptionModel(
                optionText = "찾는 일정의 정보가 없어요",
                isSelected = false,
                isUserInput = false,
            ),
            WithdrawalOptionModel(
                optionText = "예약 방법이 너무 어려워요",
                isSelected = false,
                isUserInput = false,
            ),
            WithdrawalOptionModel(
                optionText = "서비스가 불안해요",
                isSelected = false,
                isUserInput = false,
            ),
        )
    }
}