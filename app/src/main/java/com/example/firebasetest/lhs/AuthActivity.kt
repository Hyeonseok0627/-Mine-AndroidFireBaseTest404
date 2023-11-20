package com.example.firebasetest.lhs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.lhs.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    lateinit var binding : ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다음 시간에, 이메일, 구글 인증, 후
        // 인증 후 2번째 페이지 접근 확인하고,
        // 그리고 나서, 2번째 페이지 부분을 아래의 기능으로 구성.
        // 스토어, 스토리지에 접근 및 이용하는 부분을 보여주기.


        // MyApplication 인증 함수를 이용해서, 인증이 되면, 특정 뷰로 인증됨을 확인
        if(MyApplication.checkAuth()){
            Log.d("lhs", "로그인 인증이 됨")
            // 인증이 되면, mode에 따라 보여주는 함수를 동작시키기
            changeVisi("login")
        } else {
            Log.d("lhs", "로그인 인증이 안됨")
            // 인증이 되면, mode에 따라 보여주는 함수를 동작시키기
            changeVisi("logout")
        }
        // 구글 로그인 기능 확인.
        // 1) 구글 인증 버튼 눌러서, 2) 후처리 함수를 호출하기.
        // 1) 구글 인증 버튼 -> 구글의 파이어베이스 서버에 접속하고, 관련 인증을 가지고 돌아오기.
        // 2) 후처리 함수 만들기. 구글의 계정의 정보를 가지고와서, 처리하는 로직.
        // val: 상수, var: 변수 (코틀린 내)
        val requestLauncher = registerForActivityResult(
            // 후처리 하는 함수가 정해져 있는데, 이 함수를 인증, 권한 확인용
            ActivityResultContracts.StartActivityForResult()
        ){
            // 실제 작업은 여기에 이루어짐.
            // 구글 인증 결과 처리.
            // it.data: 이 부분이 구글로부터 받아온 계정 정보.
            // 구글의 계정정보를 받아오는 인스턴스의 함수(GoogleSignIn.getSignedInAccountFromIntent(it.data))이며, 이걸 task에 담는다.
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            // 로그인 정보가 있는지? 없는지? 또는 네트워크 연결 오류 등으로
            // 정보가 받거나 못 받거나 할 가능성이 있으면, 무조건, try- catch 구문 사용함.
            try {
                // 계정 정보 가져오기.
                val account = task.getResult(ApiException::class.java)
                // 계정의 정보 가져오기.
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                // 우리가 만든 MyApplication(전역)에서 auth로 확인
                MyApplication.auth.signInWithCredential(credential)
                    // 정보를 잘 가지고 왔을 때 , 수행이 될 콜백함수,
                    .addOnCompleteListener(this) {
                        // 수행할 작업.
                            task ->
                        if(task.isSuccessful){
                            MyApplication.email = account.email
                            changeVisi("login")
                        } else {
                            changeVisi("logout")
                        }
                    }
            } catch (e:ApiException){
                changeVisi("logout")
            }
        }

        // 구글 인증 버튼 클릭 시, 해당 구글 계정 선택 화면으로 이동하는 인텐트 추가하기.
        binding.googleAuthInBtn.setOnClickListener {
            // 샘플코드
            // 옵션, 이메일, 아이디토큰 가져오는 옵션
            val gso = GoogleSignInOptions
                // 구글인증 버튼 클릭 시, 뷰가 안 뜨는 문제 발생
                // 오타,DEFAULT_SIGN_IN인데, 여기서 GAME_SIGN_IN으로 해버림
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // com.firebase.ui.auth.R.string.default_web_client_id 이 부분이 변경된 것
                // 원래는 R.string.default_web_client_id 이 형태였음
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build()
            // 구글 인증 화면으로 이동하는 코드
            val signInIntent = GoogleSignIn.getClient(this,gso).signInIntent
            // 후처리 함수 동작 연결시키기.
            requestLauncher.launch(signInIntent)
        }

        // onCreate
    }

    //임의의 함수를 만들기.
    // 인증의 상태에 따라서, 로그인 화면을 표시 여부.
    // 예) 로그인이 되면, 로그아웃 버튼을 보이고,
    // 예) 로그인이 안되면, 로그아웃 버튼을 사라지게 만들기. 등
    fun changeVisi(mode: String) {
        if (mode === "login") {
            // 로그인이 되었다면, 인증된 이메일도 이미 등록이 되어서, 가져와서 사용하기.
            binding.authMainText.text = "${MyApplication.email} 님 반가워요."
            binding.logoutBtn.visibility = View.VISIBLE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.GONE
            binding.googleAuthInBtn.visibility = View.GONE
            binding.signInBtn2.visibility = View.GONE
            binding.authEmailEdit.visibility = View.GONE
            binding.authPasswordEdit.visibility = View.GONE
            binding.logInBtn.visibility = View.GONE
        } else if( mode === "logout") {
            binding.authMainText.text ="로그인 하거나 회원가입 해주세요."
            binding.logoutBtn.visibility = View.GONE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.VISIBLE
            binding.signInBtn2.visibility = View.VISIBLE
            binding.googleAuthInBtn.visibility = View.VISIBLE
            binding.authEmailEdit.visibility = View.VISIBLE
            binding.authPasswordEdit.visibility = View.VISIBLE
            binding.logInBtn.visibility = View.VISIBLE

        } else if( mode === "signIn") {
            binding.logoutBtn.visibility = View.GONE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.GONE
            binding.signInBtn2.visibility = View.GONE
            binding.googleAuthInBtn.visibility = View.GONE
            binding.authEmailEdit.visibility = View.VISIBLE
            binding.authPasswordEdit.visibility = View.VISIBLE
            binding.logInBtn.visibility = View.GONE

        }
    }


}