package com.gutnorbert.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gutnorbert.smack.Utilites.URL_LOGIN
import com.gutnorbert.smack.Utilites.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

     fun registerUser(context: Context, email:String, password:String, complete:(Boolean)-> Unit){ //Unit ha semmit nem returnulunk vissza

         val url = URL_REGISTER

         val jsonBody = JSONObject()
         jsonBody.put("email",email)
         jsonBody.put("password",password)
         val requestBody = jsonBody.toString()

         val registerRequest = object : StringRequest(Request.Method.POST,url, Response.Listener { response -> //Stringrequest  = a responset Stringben szeretnénk megkapni, ez lehet JSONObjectrequest is, JSONArray...
             println(response)
             complete(true)
         }, Response.ErrorListener {
                 error -> Log.d("ERROR","Could not register user $error")
         complete(false)}) { //eddig tartott a 2 fő része
             override fun getBodyContentType(): String { //ugyanolyan mint postmanben a content Type -pl. json, utf8 külön megadhatjuk
                 return "application/json; charset= utf-8"
             }

             override fun getBody(): ByteArray { //a fentebb definiált body, átalakítjuk byteArrayre
                 return requestBody.toByteArray()
             }
         }

         Volley.newRequestQueue(context).add(registerRequest)
     }

    fun loginUser(context:Context, email:String, password: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN,null,Response.Listener { response ->
            //itt parsoljuk a JSON-t

            try {
                println(response)
                userEmail = response.getString("user")
                authToken = response.getString("token")
                isLoggedIn = true
                complete(true)
            } catch (e : JSONException){
                Log.d("JSON","EXC:" + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
                Log.d("ERROR","Could not register user $error")
                complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset= utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }
}