package com.example.didichmnlprjct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.didichmnlprjct.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var account: Auth0
    private lateinit var binding: ActivityMainBinding
    private lateinit var accessToken: String
    private var cachedUserProfile: UserProfile? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        account = Auth0(
            "lvwiFBlmMZzKi4jesCDfsIyCkWRivLW4",
            "dev-gzkrh7j42qt7kp7d.us.auth0.com\n"
        )


        binding.btnLogin.setOnClickListener { loginWithBrowser() }
        binding.btnLogut.setOnClickListener { logout() }
        binding.btnShowUserDeatils.setOnClickListener { showUserProfile(accessToken) }

    }

    private fun loginWithBrowser() {
        // Setup the WebAuthProvider, using the custom scheme and scope.

        WebAuthProvider.login(account)
            .withScheme("demo")
            .withScope("openid profile email")
            // Launch the authentication passing the callback where the results will be received
            .start(this, object : Callback<Credentials, AuthenticationException> {
                // Called when there is an authentication failure
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                }

                // Called when authentication completed successfully
                override fun onSuccess(credentials: Credentials) {
                    // Get the access token from the credentials object.
                    // This can be used to call APIs
                     accessToken = credentials.accessToken
//                    showUserProfile(accessToken);
                    Log.e("onSuccess",accessToken)
                    binding.tvEmaailTxt.text="Logged In Successfully!"


                }

            })
    }
    private fun login() {
        // Setup the WebAuthProvider, using the custom scheme and scope.

        WebAuthProvider.login(account)
            .withScheme("https")
            .withScope("openid profile email")
            // Launch the authentication passing the callback where the results will be received
            .start(this, object : Callback<Credentials, AuthenticationException> {
                // Called when there is an authentication failure
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                    Log.e("onFailure",exception.getDescription())

                }

                // Called when authentication completed successfully
                override fun onSuccess(credentials: Credentials) {
                    // Get the access token from the credentials object.
                    // This can be used to call APIs
                    val accessToken = credentials.accessToken
                    showUserProfile(accessToken);
                    Log.e("onSuccess",accessToken)


                }
            })
    }
    private fun logout() {
        WebAuthProvider.logout(account)
            .withScheme("demo")
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    // The user has been logged out!
                    Log.e("onSuccess","logout")
                    binding.tvProfileTxt.text=""
                    binding.tvEmaailTxt.text="You were successfully logged out"

                }

                override fun onFailure(error: AuthenticationException) {
                    // Something went wrong!
                    Log.e("onFailure logout",error.getDescription())

                }
            })
    }

    private fun showUserProfile(accessToken: String) {
        var client = AuthenticationAPIClient(account)

        // With the access token, call `userInfo` and get the profile from Auth0.
        client.userInfo(accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                    Log.e("onFailure showprofilke",exception.getDescription())

                }

                override fun onSuccess(profile: UserProfile) {
                    // We have the user's profile!
                    val email = profile.email
                    val name = profile.name
                    cachedUserProfile=profile
                    if (email != null) {
                        if (name != null) {
                            updateText(email,name)
                        }
                    }

                }
            })

    }

    override fun onDestroy() {
        super.onDestroy()
        logout()
    }

    private fun updateText(email:String,name:String)
    {
        binding.tvProfileTxt.text="Hello! "+name
        binding.tvEmaailTxt.text="Confirm that your Email is "+email
    }

}