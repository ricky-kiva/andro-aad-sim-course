package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType
import java.util.Calendar

class HomeViewModel(repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData<QueryType>()

    private val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    val todayCourses: LiveData<List<Course>> = repository.getTodaySchedule(dayOfWeek)

    init {
        _queryType.value = QueryType.CURRENT_DAY
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }
}