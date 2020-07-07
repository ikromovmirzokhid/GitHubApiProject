package com.imb.githubapiproject.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.imb.githubapiproject.R
import com.imb.githubapiproject.viewmodels.GitViewModel
import kotlinx.android.synthetic.main.authorization_fragment.*

class AuthorizationFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.authorization_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        progressDialog = ProgressDialog(activity)

        val viewModel = ViewModelProvider(this).get(GitViewModel::class.java)

        signInBtn.setOnClickListener {
            val loginText = loginEditText.text.toString()
            val passwordText = passwordEditText.text.toString()
            if (loginText.isNotBlank()) {
                if (passwordText.isNotBlank()) {
                    progressDialog.setTitle("Logging In...")
                    progressDialog.setCancelable(false)
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()

                    viewModel.login(loginText, passwordText)!!
                        .observe(viewLifecycleOwner, Observer {
                            if (it != null) {
                                Snackbar.make(
                                    passwordEditText,
                                    "Successfully Logged In",
                                    Snackbar.LENGTH_SHORT
                                ).show()

                                progressDialog.dismiss()
                                com.imb.githubapiproject.utils.Settings.setUserIn(true)
                                com.imb.githubapiproject.utils.Settings.setUserAvatarURL(it.avatarUrl)
                                com.imb.githubapiproject.utils.Settings.setUserName(it.username)

                                navController.navigate(R.id.action_authorizationFragment_to_mainFragment)
                            } else {
                                progressDialog.dismiss()
                                errorTv.visibility = View.VISIBLE
                                Snackbar.make(
                                    passwordEditText,
                                    "Wrong password or username",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        })

                } else {
                    passwordEditText.error = "This field can not be empty"
                    Snackbar.make(
                        loginEditText,
                        "Please fill up every required field",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            } else {
                loginEditText.error = "This field can not be empty"
                Snackbar.make(
                    loginEditText,
                    "Please fill up every required field",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}