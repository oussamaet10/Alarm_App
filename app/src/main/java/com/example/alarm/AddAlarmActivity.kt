package com.example.alarm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alarm.databinding.ActivityAlarmBinding
import java.util.Calendar

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSaveButton()
        setupCancelButton()
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().takeIf { it.isNotBlank() } ?: "Alarm"
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute

            // Calculate the selected time in milliseconds
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val selectedTime = calendar.timeInMillis

            // Check if the selected time is in the past
            val currentTime = System.currentTimeMillis()
            if (selectedTime < currentTime) {
                Toast.makeText(this, "Please select a future time.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Return the alarm details to MainActivity
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(MainActivity.EXTRA_ALARM_TITLE, title)
                putExtra(MainActivity.EXTRA_HOUR, hour)
                putExtra(MainActivity.EXTRA_MINUTE, minute)
            })
            Toast.makeText(this, "Alarm set for $hour:$minute", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupCancelButton() {
        binding.btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}