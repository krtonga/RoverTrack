package krtonga.github.io.rovertrack.core.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Single
import krtonga.github.io.rovertrack.core.db.entity.RoverEntity
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert

@Dao
interface RoverDao {

    @Query("SELECT * FROM rovers")
    fun getAll(): Single<List<RoverEntity>>

    @Query("SELECT * FROM rovers WHERE id = :id")
    fun getUserById(id: Int): Single<RoverEntity>

    @Query("SELECT * FROM rovers WHERE phoneNumber = :phoneNumber")
//    fun getRoverByPhone(phoneNumber: String): Single<Rover>
    fun getRoverByPhone(phoneNumber: String): RoverEntity?

    @Insert
    fun insert(user: RoverEntity): Long

    @Delete
    fun delete(user: RoverEntity)
}