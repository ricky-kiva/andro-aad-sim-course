package com.dicoding.courseschedule.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

// XTODO 2 : Define data access object (DAO)
@Dao
interface CourseDao {

    @RawQuery(observedEntities=[Course::class])
    fun getNearestSchedule(query: SupportSQLiteQuery): LiveData<Course?>

    @RawQuery(observedEntities=[Course::class])
    fun getAll(query: SupportSQLiteQuery): DataSource.Factory<Int, Course>

    @Query("SELECT * FROM course WHERE id = :id")
    fun getCourse(id: Int): LiveData<Course>

    @Query("SELECT * FROM course WHERE day = :day")
    fun getTodaySchedule(day: Int): LiveData<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(course: Course)

    @Delete
    fun delete(course: Course)
}