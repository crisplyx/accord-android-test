package id.rasyiid.accord_android_test.data

import androidx.room.Database
import androidx.room.RoomDatabase
import id.rasyiid.accord_android_test.domain.product.CartDao
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = true
)
abstract class MyDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
}