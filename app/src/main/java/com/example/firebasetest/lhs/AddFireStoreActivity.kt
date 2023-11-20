package com.example.firebasetest.lhs

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.lhs.Utils.MyUtil
import com.example.firebasetest.lhs.databinding.ActivityAddFireStoreBinding
import java.util.Date

class AddFireStoreActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddFireStoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFireStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FireStore crud 확인.
        // 아이디(기본값), 게시글 내용, 날짜(기본값),
        binding.insertBtn.setOnClickListener {
            val data = mapOf(
//                "email" to MyApplication.email, -> 테스트라서 인증없이 하기위해 주석처리
                "content" to binding.inputEdt.text.toString(),
                "date" to MyUtil.dateToString(Date())
            )
            // 스토어에 넣기. NOSQL 기반, JSON과 비슷함.
            MyApplication.db.collection("TestBoard")
                // 데이터 추가
                .add(data)
                // 데이터 추가 성공 후 실행할 콜백함수
                .addOnCompleteListener{
                    Log.d("lhs","글쓰기 성공")
                    Toast.makeText(this, "글쓰기 성공", Toast.LENGTH_SHORT).show()
                }
                // 데이터 추가 실패 후 실행할 콜백함수
                .addOnFailureListener(){
                    Log.d("lhs","글쓰기 실패")
                    Toast.makeText(this, "글쓰기 실패", Toast.LENGTH_SHORT).show()
                }
        }

    }
}