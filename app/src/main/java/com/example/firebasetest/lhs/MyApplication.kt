package com.example.firebasetest.lhs

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class MyApplication : MultiDexApplication(){

    companion object {
        // 원래는 자바에서, 해당 클래스의 멤버를 사용하는 방법 2가지.
        // 1) 인스턴스를 생성해서 접근 2) static으로 클래스명으로 접근.
        // 예) 자바: A a = new A(); -> a. 접근.
        // 예) 코틀린 : val a = A(); -> a. 접근.
        // static과 비슷, 해당 클래스명으로 멤버에 접근이 가능함.
        // 인증 기능에 접근하는 인스턴스가 필요함.
        // 선언만 하고, 초기화를 안했음.
        // 초기화를 밑에 onCreate
        lateinit var auth: FirebaseAuth
        // 인증할 이메일
        var email : String? = null

        lateinit var storage : FirebaseStorage

        lateinit var db : FirebaseFirestore

        // MyApplication.checkAuth() : 이렇게 클래스명.함수 및 특정 변수에 접근이 가능함(companion object 이것을 사용해서 가능)
        // 즉, 인스턴스를 생성하지 않아도 바로 MyApplication의 함수 및 특정 변수를 사용가능(자동으로 메모리에 생성되어 있다는 개념)
        // 이것을 사용하지않으면 인스턴스를 생성 후 해당 클래스 접근이 가능
        // 그렇기 때문에, MyApplication은 동작을 하면 바로 초기화가 되어 메모리상에 둥둥 떠 다님 -> 그래서, 바로 해당 클래스에 접근하여 사용 가능

        // 해당 로직은 auth 자체의 로직(개발자가 임의로 설정한 게 아님)
        fun checkAuth():Boolean {
            // auth: 인증 관련 도구
            // currentUser: 도구 안에 기능 중에서, 현재 유저를 확인하는 함수
            var currentUser = auth.currentUser
            // 현재 유저가 있다면, 해당 유저의 이메일 정보를 가지고오고
            // 유저 이메일 인증 확인 했는지의 유무에 따라서 false
            return currentUser?.let {
                email = currentUser.email
                // 인증을 했다면, true 결과값을 반환.
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }
    } // companion object

    // 생명주기, 최초 1회 동작을 함.
    override fun onCreate() {
        super.onCreate()
        // 초기화를 함.
        auth = Firebase.auth
        storage = Firebase.storage
        db = FirebaseFirestore.getInstance()
    }
}