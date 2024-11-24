package com.example.alarm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.alarm.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmAdapter: AlarmAdapter
    private val alarms = mutableListOf<Alarm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNavigation()
        setupFab()
        setupAlarmButton()
    }

    private fun setupRecyclerView() {
        alarmAdapter = AlarmAdapter(alarms, { alarm, isChecked ->
            // Handle alarm toggle
            updateAlarmState(alarm, isChecked)
        }, { alarm ->
            // Handle delete alarm
            deleteAlarm(alarm)
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = alarmAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun deleteAlarm(alarm: Alarm) {
        // Remove the alarm from the list
        alarms.remove(alarm)
        alarmAdapter.notifyDataSetChanged() // Notify the adapter to refresh the list
    }
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_alarms -> true
                R.id.nav_timer -> {
                    // Handle timer navigation
                    true
                }

                R.id.nav_stopwatch -> {
                    // Handle stopwatch navigation
                    true
                }

                else -> false
            }
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            showAddAlarmDialog()
        }
    }

    private fun setupAlarmButton() {
        binding.scheduleAlarmButton.setOnClickListener {
            showAddAlarmDialog()
        }
    }

    private fun showAddAlarmDialog() {
        val intent = Intent(this, AddAlarmActivity::class.java)
        startActivityForResult(intent, ADD_ALARM_REQUEST)
    }

    private fun updateAlarmState(alarm: Alarm, isEnabled: Boolean) {
        alarm.isEnabled = isEnabled
        if (isEnabled) {
            scheduleAlarmWithWorkManager(alarm)
        } else {
            // Handle disabling the alarm if needed
            // You may want to cancel the WorkManager task here
        }
    }

    private fun scheduleAlarmWithWorkManager(alarm: Alarm) {
        val workManager = WorkManager.getInstance(this)

        // Create input data for the worker
        val inputData = Data.Builder()
            .putString(EXTRA_ALARM_TITLE, alarm.title)
            .build()

        // Set the time for the alarm
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
        }

        // Check if the alarm time is in the past
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            Toast.makeText(this, "Alarm time must be in the future.", Toast.LENGTH_SHORT).show()
            return
        }

        // Schedule the work
        val workRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInputData(inputData)
            .setInitialDelay(
                calendar.timeInMillis - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueue(workRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                val title = intent.getStringExtra(EXTRA_ALARM_TITLE) ?: "Alarm"
                val hour = intent.getIntExtra(EXTRA_HOUR, 0)
                val minute = intent.getIntExtra(EXTRA_MINUTE, 0)

                // Create a new Alarm instance
                val alarm = Alarm(
                    id = System.currentTimeMillis().toInt(), // Unique ID for the alarm
                    title = title,
                    hour = hour,
                    minute = minute,
                    isEnabled = true // Default to enabled
                )

                // Add the alarm to the list
                alarms.add(alarm)
                alarmAdapter.notifyItemInserted(alarms.size - 1) // Notify adapter of new item
                scheduleAlarmWithWorkManager(alarm) // Schedule the alarm
            }
        }
    }
    private fun showEditAlarmDialog(alarm: Alarm) {
        val intent = Intent(this, EditAlarmActivity::class.java).apply {
            putExtra("ALARM_ID", alarm.id)
            putExtra("ALARM_TITLE", alarm.title)
            putExtra("ALARM_HOUR", alarm.hour)
            putExtra("ALARM_MINUTE", alarm.minute)
        }
        startActivityForResult(intent, EDIT_ALARM_REQUEST) // Use the defined constant
    }
    companion object {
        private const val ADD_ALARM_REQUEST = 1
        private const val EDIT_ALARM_REQUEST = 2 // Define the constant for editing alarms
        const val EXTRA_ALARM_TITLE = "EXTRA_ALARM_TITLE"
        const val EXTRA_HOUR = "EXTRA_HOUR"
        const val EXTRA_MINUTE = "EXTRA_MINUTE"
    }
}