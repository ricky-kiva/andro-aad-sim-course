package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.ViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel

    private lateinit var courseNameEditText: TextInputEditText
    private lateinit var daySpinner: Spinner
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView
    private lateinit var lecturerEditText: TextInputEditText
    private lateinit var noteEditText: TextInputEditText
    private lateinit var startTimeButton: ImageButton
    private lateinit var endTimeButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        courseNameEditText = findViewById(R.id.ed_course_name)
        daySpinner = findViewById(R.id.spinner_day)
        startTimeTextView = findViewById(R.id.tv_start_time)
        endTimeTextView = findViewById(R.id.tv_end_time)
        lecturerEditText = findViewById(R.id.ed_lecturer)
        noteEditText = findViewById(R.id.ed_note)
        startTimeButton = findViewById(R.id.ib_start_time)
        endTimeButton = findViewById(R.id.ib_end_time)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        val dayAdapter = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item)
        daySpinner.adapter = dayAdapter

        startTimeButton.setOnClickListener {
            showTimePickerDialog("start_time")
        }
        endTimeButton.setOnClickListener {
            showTimePickerDialog("end_time")
        }

    }

    private fun showTimePickerDialog(tag: String) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, tag)
    }

    private fun saveCourse() {
        val courseName = courseNameEditText.text.toString().trim()
        val day = daySpinner.selectedItemPosition
        val startTime = startTimeTextView.text.toString()
        val endTime = endTimeTextView.text.toString()
        val lecturer = lecturerEditText.textAlignment.toString().trim()
        var note = noteEditText.text.toString().trim()

        if (courseName.isEmpty()) {
            Toast.makeText(this, "Please enter course name", Toast.LENGTH_SHORT).show()
        } else if (day == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Please set course day", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(startTime)) {
            Toast.makeText(this, "Please set start time", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(this, "Please set end time", Toast.LENGTH_SHORT).show()
        } else if (lecturer.isEmpty()) {
            Toast.makeText(this, "Please enter lecturer", Toast.LENGTH_SHORT).show()
        } else if (note.isEmpty()) {
            note = "-"
            viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
        } else {
            viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val formatHour = String.format("%02d", hour)
        val formatMinute = String.format("%02d", minute)

        return "$formatHour:$formatMinute"
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        if (tag == "start_time") {
            startTimeTextView.text = formatTime(hour, minute)
        } else if (tag == "end_time") {
            endTimeTextView.text = formatTime(hour, minute)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_insert -> {
                saveCourse()
                true
            }
            else -> true
        }
    }

}