package com.example.mojaram.faq

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityFaqBinding

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding
    private var isAnswer1Showing: Boolean = false
    private var isAnswer2Showing: Boolean = false
    private var isAnswer3Showing: Boolean = false
    private var isAnswer4Showing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq)

        with(binding) {
            with(imageviewBack) {
                setOnClickListener {
                    finish()
                }
            }

            with(tvFaq1) {
                setOnClickListener {
                    isAnswer1Showing = !isAnswer1Showing
                    with(layoutAnswer1) {
                        isVisible = isAnswer1Showing
                        if (isAnswer1Showing) {
                            animate().alpha(1.0f).duration = 500
                        } else {
                            alpha = 0.0f
                        }
                    }
                    with(ivFaq1) {
                        if (isAnswer1Showing) {
                            ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 180f).setDuration(100)
                                .start()
                        } else {
                            ObjectAnimator.ofFloat(this, View.ROTATION, 180f, 360f).setDuration(100)
                                .start()
                        }
                    }
                }
            }

            with(tvFaq2) {
                setOnClickListener {
                    isAnswer2Showing = !isAnswer2Showing
                    with(layoutAnswer2) {
                        isVisible = isAnswer2Showing
                        if (isAnswer2Showing) {
                            animate().alpha(1.0f).duration = 500
                        } else {
                            alpha = 0.0f
                        }
                    }
                    with(ivFaq2) {
                        if (isAnswer2Showing) {
                            ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 180f).setDuration(100)
                                .start()
                        } else {
                            ObjectAnimator.ofFloat(this, View.ROTATION, 180f, 360f).setDuration(100)
                                .start()
                        }
                    }
                }
            }

            with(tvFaq3) {
                setOnClickListener {
                    isAnswer3Showing = !isAnswer3Showing
                    with(layoutAnswer3) {
                        isVisible = isAnswer3Showing
                        if (isAnswer3Showing) {
                            animate().alpha(1.0f).duration = 500
                        } else {
                            alpha = 0.0f
                        }
                    }
                    with(ivFaq3) {
                        if (isAnswer3Showing) {
                            ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 180f).setDuration(100)
                                .start()
                        } else {
                            ObjectAnimator.ofFloat(this, View.ROTATION, 180f, 360f).setDuration(100)
                                .start()
                        }
                    }
                }
            }
            with(tvFaq4) {
                setOnClickListener {
                    isAnswer4Showing = !isAnswer4Showing
                    with(layoutAnswer4) {
                        isVisible = isAnswer4Showing
                        if (isAnswer4Showing) {
                            animate().alpha(1.0f).duration = 500
                        } else {
                            alpha = 0.0f
                        }
                    }
                    with(ivFaq4) {
                        if (isAnswer4Showing) {
                            ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 180f).setDuration(100)
                                .start()
                        } else {
                            ObjectAnimator.ofFloat(this, View.ROTATION, 180f, 360f).setDuration(100)
                                .start()
                        }
                    }
                }
            }
        }
    }
}