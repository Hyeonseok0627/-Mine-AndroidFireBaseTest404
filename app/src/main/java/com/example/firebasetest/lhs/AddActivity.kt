package com.example.firebasetest.lhs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firebasetest.lhs.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    // 갤러리에서 선택된, 파일의 위치
    // 초기화는 밑에서 하기.
    lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //
        // 1) 버튼,메뉴등 : 갤러리에서 사진을 선택 후,
        // 2)가져와서 처리하는 , 후처리 함수 만들기.
        val requestLauncher = registerForActivityResult(
            // 갤러리에서, 사진을 선택해서 가져왔을 때, 수행할 함수.
            ActivityResultContracts.StartActivityForResult()
        ) {
            // it 이라는 곳에 사진 이미지가 있음.
            if(it.resultCode === android.app.Activity.RESULT_OK) {
                // 이미지 불러오는 라이브러리 glide 사용하기, 코루틴이 적용이되어서, 매우 빠름.
                // OOM(Out Of Memory 해결), gif 움직이는 사진도 가능.
                // Glide 설치하기. build.gradle
                // with(this) : this는 현재 액티비티를 가리킴 -> 이것을 대신해서 밑에 두 가지도 사용가능
                // 1) applicationContext
                // 2) getApplicationContext()
                Glide
                    .with(getApplicationContext())
                    // 사진을 읽기.
                    .load(it.data?.data)
                    // 크기 지정 , 가로,세로
                    .apply(RequestOptions().override(250,200))
                    // 선택된 사진 크기 자동 조정
                    .centerCrop()
                    // 결과 뷰에 사진 넣기.
                    .into(binding.resultImageView)

                // filePath, 갤러리에서 불러온 이미지 파일 정보 가져오기.
                // 통으로 샘플코드 처리 사용하면 됨.
                // 커서에 이미지 파일이름이 등록이 되어 있음.
                val cursor = contentResolver.query(it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA),null,
                    null,null);

                cursor?.moveToFirst().let {
                    filePath = cursor?.getString(0) as String
                }
                Log.d("lhs", "filePath : ${filePath}")
                Toast.makeText(this,"filePath : ${filePath}",Toast.LENGTH_LONG).show()
                binding.resultFilepath.text = filePath
            } // 조건문 닫는 블록
        }

        // 1) 버튼 이용해서, 갤러리 (2번째 앱) 호출해서, 사진 선택 후 작업하기.
        binding.galleryBtn.setOnClickListener {
            // 샘플코드처럼, 갤러리 호출, 호출된 이미지 선택 부분
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*"
            )
            requestLauncher.launch(intent)
        }

        // 업로드 버튼을 눌러서, 이미지 업로드만 하고, 다운로드 보기.
        binding.uploadBtn.setOnClickListener {
            // 스토리지 접근 도구 인스턴스
            val storage = MyApplication.storage
            // 스토리지에 저장할 파일명 인스턴스
            val storageRef = storage.reference
            //
            val
        }


    } // onCreate




}