package com.example.firebasetest.lhs.imageShareApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.firebasetest.lhs.MyApplication
import com.example.firebasetest.lhs.databinding.ActivityItemDetailBinding

class ItemDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityItemDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 목록에서 클릭 해서,
        // 인텐트에 담아온 데이터를 가져오기.
        // 해당 뷰에 넣기.
        // 1번 화면에서 넘어온 데이터 받아서, 결과 뷰에 붙이기 작업.
        // 임시 저장소에 담기.

        //            var docId: String? = null
//            var email: String? = null
//            var content: String? = null
//            var date: String? = null
        val docId = intent.getStringExtra("docId")
        val email = intent.getStringExtra("email")
        val content = intent.getStringExtra("content")
        val date = intent.getStringExtra("date")

        binding.emailDetailResultView.text = email
        binding.dateDetailResultView.text = date
        binding.contentDetailResultView.text = content

        // 스토리지에서 이미지 불러와서, Glide 로 출력하기.
        val imgRef = MyApplication.storage.reference
            // 사진이 한장이라서, 게시글의 id(자동생성된값)이용하고 있음.
            .child("AndroidImageShareApp/${docId}.jpg")
        // 다운로드, 스토리지에서, 이미지의 저장소의 URL 가져오기.
        imgRef.downloadUrl
            .addOnCompleteListener{
                // 성공시 수행할 콜백 함수, task 안에, 이미지의 url 담겨 있음.
                    task ->
                if(task.isSuccessful) {
                    // with 동작을 해당 activity, 등 context로 대체 .
                    Glide.with(this)
                        // URL 주소로 이미지 불러오기.
                        .load(task.result)
                        //결과 뷰에 이미지 넣기.
                        .into(binding.imageDetailResultView)
                }

            } // addOnCompleteListener

        // 수정 가능한 요소 1) 날짜(수정 시 덮어쓰기)
        // 2) 이미지 3) 콘텐츠
        // 현재 게시글의 번호 docID 이름을 알고 있음.
        // 해당 이미지 이름도 알고 있는 것과 같다.

    }
}