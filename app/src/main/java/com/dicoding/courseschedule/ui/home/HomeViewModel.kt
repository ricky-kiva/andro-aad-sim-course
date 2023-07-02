package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData<QueryType>()
    private val _todayCourses = MutableLiveData<List<Course>>()
    val todayCourses: LiveData<List<Course>> get() = _todayCourses

    init {
        _queryType.value = QueryType.CURRENT_DAY
        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        viewModelScope.launch {
            val courses = repository.getTodaySchedule(dayOfWeek)
            _todayCourses.value = courses
        }

    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }
}