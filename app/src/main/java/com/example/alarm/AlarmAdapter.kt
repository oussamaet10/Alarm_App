package com.example.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarm.databinding.CardItemBinding

class AlarmAdapter(
    private val alarms: MutableList<Alarm>,
    private val onAlarmToggled: (Alarm, Boolean) -> Unit,
    private val onAlarmDeleted: (Alarm) -> Unit // Add delete callback
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(private val binding: CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alarm: Alarm) {
            binding.apply {
                tvTitle.text = alarm.title
                tvTime.text = alarm.getFormattedTime()
                tvDays.text = alarm.getFormattedDays()
                switchAlarm.isChecked = alarm.isEnabled

                // Handle toggle switch change
                switchAlarm.setOnCheckedChangeListener { _, isChecked ->
                    onAlarmToggled(alarm, isChecked)
                }

                // Set delete button listener
                btnDelete.setOnClickListener {
                    onAlarmDeleted(alarm) // Trigger delete callback
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = CardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(alarms[position])
    }

    override fun getItemCount() = alarms.size

    // Function to remove an alarm from the list
    fun removeAlarm(alarm: Alarm) {
        val position = alarms.indexOf(alarm)
        if (position != -1) {
            alarms.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}