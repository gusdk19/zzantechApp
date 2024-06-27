package com.example.zzantechapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient

class SettingTarget : AppCompatActivity() {

    private lateinit var etGoalAmount: EditText
    private lateinit var btnConfirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_target)

        val helloUserText = findViewById<TextView>(R.id.hello_user_text)

        UserApiClient.instance.me { user, error ->
            if (user != null) {
                helloUserText.text = "${user.kakaoAccount?.profile?.nickname}님, 안녕하세요!\n"
            }
        }

        etGoalAmount = findViewById(R.id.goal_amount)
        btnConfirm = findViewById(R.id.button_confirm)

        btnConfirm.setOnClickListener {
            val goalAmountText = etGoalAmount.text.toString()

            if (goalAmountText.isEmpty()) {
                etGoalAmount.error = "목표 금액을 입력해주세요!"
            } else {
                try {
                    val goalAmount = goalAmountText.toInt()

                    // goalAmount를 인텐트에 담아서 ZzanMain 액티비티로 전환
                    val resultIntent = Intent(this, ZzanMain::class.java)
                    resultIntent.putExtra("goalAmount", goalAmount)
                    startActivity(resultIntent)

                    // 현재 액티비티 종료
                    finish()
                } catch (e: NumberFormatException) {
                    etGoalAmount.error = "유효한 금액을 입력하세요"
                }
            }
        }
    }
}
