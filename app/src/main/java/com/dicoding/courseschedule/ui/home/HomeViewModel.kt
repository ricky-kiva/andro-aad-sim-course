package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeViewModel(private val repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData<QueryType>()

    private val _nearestSchedule = MutableLiveData<Course?>()
    val nearestSchedule: LiveData<Course?> = _nearestSchedule

    init {
        _queryType.value = QueryType.CURRENT_DAY
        fetchSchedule()
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }

    private fun fetchSchedule() {
        viewModelScope.launch {
            val todaySchedule = withContext(Dispatchers.IO) {
                repository.getTodaySchedule()
            }

            if (todaySchedule.isNotEmpty()) {
                val currentTime = Calendar.getInstance().time
                var nearestSchedule: Course? = null
                var minTimeDifference = Long.MAX_VALUE

                for (schedule in todaySchedule) {
                    val startTime = schedule.startTime
                    val scheduleTime = getDateFromString(startTime)

                    if (currentTime.before(scheduleTime)) {
                        val timeDifference = scheduleTime.time - currentTime.time

                        if (timeDifference < minTimeDifference) {
                            minTimeDifference = timeDifference
                            nearestSchedule = schedule
                        }
                    }
                }

                _nearestSchedule.value = nearestSchedule
            } else {
                checkQueryType(nearestSchedule.value)
                val nearestScheduleLiveData = Transformations.switchMap(_queryType) {
                    repository.getNearestSchedule(it)
                }
                val observer = object : Observer<Course?> {
                    override fun onChanged(course: Course?) {
                        _nearestSchedule.value = course
                        nearestScheduleLiveData.removeObserver(this)
                    }
                }
                nearestScheduleLiveData.observeForever(observer)
            }
        }
    }
    private fun getDateFromString(timeString: String): Date {
        val currentTime = Calendar.getInstance()
        val currentDate = currentTime.time
        val timeParts = timeString.split(":")
        currentTime.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
        currentTime.set(Calendar.MINUTE, timeParts[1].toInt())
        currentTime.set(Calendar.SECOND, 0)
        val scheduleDate = currentTime.time

        if (scheduleDate.before(currentDate)) {
            currentTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        return currentTime.time
    }

    private fun checkQueryType(course: Course?) {
        if (course == null) {
            val newQueryType: QueryType = when (_queryType.value) {
                QueryType.CURRENT_DAY -> QueryType.NEXT_DAY
                QueryType.NEXT_DAY -> QueryType.PAST_DAY
                else -> QueryType.CURRENT_DAY
            }
            setQueryType(newQueryType)
        }
    }
}