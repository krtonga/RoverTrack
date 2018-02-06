package krtonga.github.io.rovertrack.core.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = "rovers",
        indices = [
            Index("phoneNumber", unique = true)
        ])
data class RoverEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,

        val phoneNumber: String,

        val name: String? = null,

        val team: Int? = null
)