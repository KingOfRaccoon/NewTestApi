package com.example.newtestapi

import android.accounts.Account
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.company.didi.model.DiDiDriverWithPhoto
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes

class MainActivity : AppCompatActivity() {
    val SIGN_IN_CODE = 7
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val message = Message("Кто", "Я", "Кто я")
        Log.e("id", message.toString())
        try {
            requestSignIn(this)
        }catch (e: Exception){
            Log.e("data", e.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE &&  resultCode != 0) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val acc = task.getResult(Exception::class.java)
            Log.e("a", acc.toString())
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    val account = task.result
                    if (account != null) {
                        val scopes = listOf(SheetsScopes.SPREADSHEETS)
                        val credential = GoogleAccountCredential.usingOAuth2(this, scopes)
                        credential.selectedAccount = Account("hunter.for.luck1@gmail.com", "com.google")
//                        credential.selectedAccount = account.account
                        val jsonFactory = JacksonFactory.getDefaultInstance()
                        val httpTransport = newCompatibleTransport()
                        val service = Sheets.Builder(httpTransport, jsonFactory, credential)
                                .setApplicationName(getString(R.string.app_name))
                                .build()
                        Log.e("data", account.account.toString())
//                        ManageDataGoogleSheets.(service, Message("Кто", "Я", "Нет не я"))
//                        ManageDataGoogleSheets.readData(service)
                        val d = DiDiDriverWithPhoto(
                                findViewById<TextInputEditText>(R.id.last_name_user).text.toString(),
                                findViewById<TextInputEditText>(R.id.name_user).text.toString(),
                                findViewById<TextInputEditText>(R.id.name_user1).text.toString()
                        )
                        ManageDataGoogleSheets.writeData(service, d
//                                DiDiDriverWithPhoto("Баранул", "123123123", "Привет", "Тут что-то есть")
                        )
                    }
                }
                else
                    Log.e("data", it.exception?.message.toString())
            }
        }
    }

    private fun requestSignIn(context: Context) {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
                .build()
        val client = GoogleSignIn.getClient(context, signInOptions)
        startActivityForResult(Intent(client.signInIntent), SIGN_IN_CODE)
    }
}