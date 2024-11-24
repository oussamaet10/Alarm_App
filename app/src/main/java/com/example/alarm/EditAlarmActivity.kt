package com.example.alarm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alarm.databinding.ActivityAlarmBinding

class EditAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var alarmId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the alarm details from intent
        alarmId = intent.getIntExtra("ALARM_ID", -1)
        val title = intent.getStringExtra("ALARM_TITLE") ?: ""
        val hour = intent.getIntExtra("ALARM_HOUR", 0)
        val minute = intent.getIntExtra("ALARM_MINUTE", 0)

        // Set the existing alarm details
        binding.etTitle.setText(title)
        binding.timePicker.hour = hour
        binding.timePicker.minute = minute

        binding.btnSave.setOnClickListener {
            // Save the edited alarm
            val updatedTitle = binding.etTitle.text.toString()
            val updatedHour = binding.timePicker.hour
            val updatedMinute = binding.timePicker.minute

            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("ALARM_ID", alarmId)
                putExtra("ALARM_TITLE", updatedTitle)
                putExtra("ALARM_HOUR", updatedHour)
                putExtra("ALARM_MINUTE", updatedMinute)
            })
            finish()
        }
    }
}