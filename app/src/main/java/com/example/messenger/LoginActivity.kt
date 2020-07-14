package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance();

    }
    //회원 가입을 위한 Function
    fun signinAndSignup(){
        auth?.createUserWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener{
            task -> if(task.isSuccessful) {
                //회원가입 성공
                moveMainPage(task.result.user)
            }else if(!task.exception?.message.isNullOrEmpty()){
                //회원가입 실패
                Toast.makeText(
                    this,
                    task.exception?.message,
                    Toast.LENGTH_LONG
                ).show()
            }else{
                //회원가입과 에러가 아닐 경우 -> 로그인 화면으로 이동
                singinEmail()
            }
        }
    }

    //Login을 위한 Function
    fun singinEmail(){
        auth?.signInWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener{
            task -> if(task.isSuccessful) {
                //ID Password가 일치할 경우

            }else{
                //ID Password가 일치하지 않을 경우

            }
        }
    }
    //로그인 성공 후 MainPage로 이동하는 Function
    fun moveMainPage(user : FirebaseUser?){
        if(user != null){
            startActivity(Intent(
                this,
                MainActivity::class.java)
            )
        }
    }

}