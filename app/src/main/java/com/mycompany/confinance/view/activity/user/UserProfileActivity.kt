package com.mycompany.confinance.view.activity.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mycompany.confinance.R
import com.mycompany.confinance.databinding.*
import com.mycompany.confinance.model.UserModel
import com.mycompany.confinance.util.SharedPreferencesUtil
import com.mycompany.confinance.view.activity.MainActivity
import com.mycompany.confinance.viewmodel.user.UserProfileViewModel

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private val viewModel: UserProfileViewModel by viewModels()
    private lateinit var user: UserModel
    private var isEditNameEnabled = false
    private var isEditEmailEnabled = false
    private var dialogExit: AlertDialog? = null
    private var dialogSucess: AlertDialog? = null
    private var dialogInvalidation: AlertDialog? = null
    private var dialogEditErro: AlertDialog? = null
    private var photo: Int? = null

    companion object {
        private const val REQUEST_CODE_ICON_ACTIVITY = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUser()
        observe()
        handleClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ICON_ACTIVITY && resultCode == Activity.RESULT_OK) {
            imgIcone()
        }
    }




    private fun observe() {
        viewModel.user.observe(this) {
            user = it
            photo = it.photo
            imgIcone()
            binding.editName.setText(user.name)
            binding.editEmail.setText(user.email)
        }
        viewModel.isLoadingUpdate.observe(this) {
            if (it == true) {
                handleDialogSucess()
            } else if (it == false) {
                handleDialogUnauthorized()
            } else {
                handleErro()
            }
        }
        viewModel.resultDeleteUser.observe(this) {
            if (it) {
                startActivity(Intent(this, CreateAccountActivity::class.java))
                finish()
            } else {
                handleErro()
            }
        }
    }

    private fun handleDialogSucess() {
        if (dialogSucess != null && dialogSucess?.isShowing == true) {
            dialogSucess?.dismiss()
        }
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding = CustomDialogUserSucessBinding.inflate(LayoutInflater.from(this))
        dialogBinding.buttonOk.setOnClickListener {
            dialogSucess?.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        dialogSucess = build.setView(dialogBinding.root).create()
        dialogSucess?.show()
    }

    private fun handleDialogUnauthorized() {
        if (dialogInvalidation != null && dialogInvalidation?.isShowing == true) {
            dialogInvalidation?.dismiss()
        }
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding =
            CustomDialogDadosInvalidationBinding.inflate(LayoutInflater.from(this))
        dialogBinding.buttonTryAgain.setOnClickListener {
            dialogInvalidation?.dismiss()
        }

        dialogInvalidation = build.setView(dialogBinding.root).create()
        dialogInvalidation?.show()
    }

    @Suppress("DEPRECATION")
    private fun handleClick() {
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.buttonDelete.setOnClickListener {
            handleDialogDelete()
        }

        binding.imageUpdateTextName.setOnClickListener {
            if (isEditNameEnabled) {
                binding.editName.text!!.clear()
                binding.editName.text!!.append(user.name)
                binding.editName.isEnabled = false
                binding.editName.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.editName.windowToken, 0)
            } else {
                binding.editName.isEnabled = true
                binding.editName.isFocusableInTouchMode = true
                binding.editName.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.editName, InputMethodManager.SHOW_IMPLICIT)
            }
            isEditNameEnabled = !isEditNameEnabled
        }

        binding.editName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.editName.isEnabled = false
                isEditNameEnabled = false
                true
            } else {
                false
            }
        }

        binding.imageUptadeTextEmail.setOnClickListener {
            if (isEditEmailEnabled) {
                binding.editEmail.text!!.clear()
                binding.editEmail.text!!.append(user.email)
                binding.editEmail.isEnabled = false
                binding.editEmail.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.editEmail.windowToken, 0)
            } else {
                binding.editEmail.isEnabled = true
                binding.editEmail.isFocusableInTouchMode = true
                binding.editEmail.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.editEmail, InputMethodManager.SHOW_IMPLICIT)
            }
            isEditEmailEnabled = !isEditEmailEnabled
        }

        binding.editEmail.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.editEmail.isEnabled = false
                isEditEmailEnabled = false
                true
            } else {
                false
            }
        }

        binding.imageConfirm.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val name = binding.editName.text.toString()
            viewModel.updateNameOrEmailOrPhoto(email, name, photo, user)
        }

        binding.buttonUpdatePassword.setOnClickListener {
            val password = binding.editPassword.text.toString()
            val newPassword = binding.editNewPassword.text.toString()
            val newPasswordAgain = binding.editNewPasswordAgain.text.toString()

            viewModel.uptadePassword(password, newPassword, newPasswordAgain)
        }

        binding.imagePerfil.setOnClickListener {
            startActivityForResult(Intent(this, IconActivity::class.java), REQUEST_CODE_ICON_ACTIVITY)
        }
    }


    private fun handleDialogDelete() {
        if (dialogExit != null && dialogExit?.isShowing == true) {
            dialogExit?.dismiss()
        }

        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val bindingDialog = CustomDialogDeleteAccountBinding.inflate(LayoutInflater.from(this))
        bindingDialog.buttonYesDelete.setOnClickListener {
            dialogExit?.dismiss()
            viewModel.deleteUser()
        }
        bindingDialog.buttonNo.setOnClickListener {
            dialogExit?.dismiss()
        }

        dialogExit = build.setView(bindingDialog.root).create()
        dialogExit?.show()
    }

    private fun handleErro() {
        if (dialogEditErro != null && dialogEditErro?.isShowing == true) {
            dialogEditErro?.dismiss()
        }

        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding =
            CustomDialogErrorBinding.inflate(LayoutInflater.from(this))

        dialogBinding.button.setOnClickListener {
            dialogEditErro?.dismiss()
        }

        dialogEditErro = build.setView(dialogBinding.root).create()
        dialogEditErro?.show()

    }

    @SuppressLint("SuspiciousIndentation")
    private fun imgIcone() {
        val img = SharedPreferencesUtil.getImg(context = this)

            when (img) {
                1 -> {
                    photo = 1
                    binding.imagePerfil.setImageResource(R.drawable.perfil_5)
                }

                2 -> {
                    photo = 2
                    binding.imagePerfil.setImageResource(R.drawable.perfil_4)
                }

                3 -> {
                    photo = 3
                    binding.imagePerfil.setImageResource(R.drawable.perfil_3)
                }

                4 -> {
                    photo = 4
                    binding.imagePerfil.setImageResource(R.drawable.perfil_2)
                }

                5 -> {
                    photo = 5
                    binding.imagePerfil.setImageResource(R.drawable.perfil_1)
                }

                6 -> {
                    photo = 6
                    binding.imagePerfil.setImageResource(R.drawable.perfil_h1)
                }

                7 -> {
                    photo = 7
                    binding.imagePerfil.setImageResource(R.drawable.perfil_m1)
                }

                8 -> {
                    photo = 8
                    binding.imagePerfil.setImageResource(R.drawable.perfil_m2)
                }

                9 -> {
                    photo = 9
                    binding.imagePerfil.setImageResource(R.drawable.perfil_h2)
                }

                10 -> {
                    photo = 10
                    binding.imagePerfil.setImageResource(R.drawable.perfil_h3)
                }
                11 -> {
                    photo = 11
                    binding.imagePerfil.setImageResource(R.drawable.perfil_6)
                }
                12 -> {
                    photo = 12
                    binding.imagePerfil.setImageResource(R.drawable.perfil_7)
                }
                13 -> {
                    photo = 13
                    binding.imagePerfil.setImageResource(R.drawable.perfil_8)
                }
                14 -> {
                    photo = 14
                    binding.imagePerfil.setImageResource(R.drawable.perfil_9)
                }
                15 -> {
                    photo = 15
                    binding.imagePerfil.setImageResource(R.drawable.perfil_10)
                }
                16 -> {
                    photo = 16
                    binding.imagePerfil.setImageResource(R.drawable.perfil_11)
                }
                17 -> {
                    photo = 17
                    binding.imagePerfil.setImageResource(R.drawable.perfil_12)
                }18-> {
                    photo = 18
                    binding.imagePerfil.setImageResource(R.drawable.perfil_13)
                }19 -> {
                    photo = 19
                    binding.imagePerfil.setImageResource(R.drawable.perfil_14)
                }20 -> {
                    photo = 20
                    binding.imagePerfil.setImageResource(R.drawable.perfil_15)
                }
        }
    }

}