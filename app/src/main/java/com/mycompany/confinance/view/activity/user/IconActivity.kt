package com.mycompany.confinance.view.activity.user

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mycompany.confinance.R
import com.mycompany.confinance.databinding.ActivityIconBinding
import com.mycompany.confinance.util.SharedPreferencesUtil

class IconActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIconBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIconBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleClick()

    }

    private fun handleClick() {
        binding.arrowBack.setOnClickListener {
            finish()
        }
        handleImage()
    }

    private fun handleImage() {
        val imgs = listOf(
            binding.img1,
            binding.img2,
            binding.img3,
            binding.img4,
            binding.img5,
            binding.img6,
            binding.img7,
            binding.img8,
            binding.img9,
            binding.img10,
            binding.img11,
            binding.img12,
            binding.img13,
            binding.img14,
            binding.img15,
            binding.img16,
            binding.img17,
            binding.img18,
            binding.img19,
            binding.img20
        )

        for (img in imgs){
            img.setOnClickListener {
                when(img.id){
                    R.id.img_1 -> {
                      SharedPreferencesUtil.saveImg(context = this@IconActivity,1)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    R.id.img_2 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,2)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    R.id.img_3 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,3)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    R.id.img_4 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,4)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    R.id.img_5 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,5)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_6 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,6)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_7 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,7)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_8 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,8)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_9 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,9)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_10 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,10)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_11 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,11)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_12 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,12)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_13 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,13)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_14 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,14)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_15 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,15)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_16 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,16)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_17 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,17)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_18 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,18)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_19 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,19)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    R.id.img_20 -> {
                        SharedPreferencesUtil.saveImg(context = this@IconActivity,20)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        }
    }
}