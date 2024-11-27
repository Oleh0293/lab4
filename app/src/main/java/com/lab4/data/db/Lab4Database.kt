package com.lab4.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lab4.data.dao.SubjectDao
import com.lab4.data.dao.SubjectLabsDao
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Lab4Database - the main database class
 * - extends on RoomDatabase()
 * - marked with @Database annotation for generating communication interfaces
 * - in annotation are added all your entities (tables)
 * - includes abstract properties of all DAO interfaces for each entity (table)
 */
@Database(entities = [SubjectEntity::class, SubjectLabEntity::class], version = 1)
abstract class Lab4Database : RoomDatabase() {
    //DAO properties for each entity (table)
    // must be abstract (because Room will generate instances by itself)
    abstract val subjectsDao: SubjectDao
    abstract val subjectLabsDao: SubjectLabsDao
}

/**
 * DatabaseStorage - custom class where you initialize and store Lab4Database single instance
 *
 */
object DatabaseStorage {
    // ! Important - all operations with DB must be done from non-UI thread!
    // coroutineScope: CoroutineScope - is the scope which allows to run asynchronous operations
    // > we will learn it soon! For now just put it here
    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        },
    )

    // single instance of Lab4Database
    private var _database: Lab4Database? = null

    /**
        Function of initializing and getting Lab4Database instance
        - is invoked from place where DB should be used (from Compose screens)
        [context] - context from Compose screen to init DB
    */
    fun getDatabase(context: Context): Lab4Database {
        // if _database already contains Lab4Database instance, return this instance
        if (_database != null) return _database as Lab4Database
        // if not, create instance, preload some data and return this instance
        else {
            // creating Lab4Database instance by builder
            _database = Room.databaseBuilder(
                context,
                Lab4Database::class.java, "lab4Database"
            ).build()

            // preloading some data to DB
            preloadData()

            return _database as Lab4Database
        }
    }

    /**
        Function for preloading some initial data to DB
     */
    private fun preloadData() {
        // List of subjects
        val listOfSubject = listOf(
            SubjectEntity(id = 1, title = "Основи штучного інтелекту"),
            SubjectEntity(id = 2, title = "Хмарні технології та сервіси"),
            SubjectEntity(id = 3, title = "Кібербезпека і захист даних"),
            SubjectEntity(id = 4, title = "Розробка веб-додатків"),
        )
        // List of labs
        val listOfSubjectLabs = listOf(
            // Основи штучного інтелекту
            SubjectLabEntity(
                id = 1,
                subjectId = 1,
                title = "Вступ до машинного навчання",
                description = "Вивчити основи машинного навчання та побудувати першу модель класифікації.",
                comment = "Дедлайн 10.01",
            ),
            SubjectLabEntity(
                id = 2,
                subjectId = 1,
                title = "Реалізація нейронної мережі",
                description = "Побудувати просту нейронну мережу для розпізнавання рукописних цифр.",
                comment = "Захист у четвер",
                isCompleted = true
            ),
            // Хмарні технології та сервіси
            SubjectLabEntity(
                id = 3,
                subjectId = 2,
                title = "Налаштування хмарної інфраструктури",
                description = "Розгорнути сервер у хмарі AWS і налаштувати базовий стек веб-додатка.",
                comment = "",
                isCompleted = true
            ),
            SubjectLabEntity(
                id = 4,
                subjectId = 2,
                title = "Контейнеризація з Docker",
                description = "Створити Docker-контейнер для розроблюваного веб-додатка.",
                comment = "Тестування в п’ятницю",
                inProgress = true
            ),
            // Кібербезпека і захист даних
            SubjectLabEntity(
                id = 5,
                subjectId = 3,
                title = "Шифрування даних",
                description = "Реалізувати алгоритм шифрування AES для захисту файлів.",
                comment = "",
                isCompleted = true
            ),
            SubjectLabEntity(
                id = 6,
                subjectId = 3,
                title = "Пошук вразливостей",
                description = "Сканувати сервер на вразливості за допомогою OWASP ZAP.",
                comment = "Захист у понеділок",
                isCompleted = true
            ),
            // Розробка веб-додатків
            SubjectLabEntity(
                id = 7,
                subjectId = 4,
                title = "Розробка REST API",
                description = "Створити REST API для блогу з використанням Spring Boot.",
                comment = "Протестувати ендпоінти",
                isCompleted = true
            ),
            SubjectLabEntity(
                id = 8,
                subjectId = 4,
                title = "Інтеграція фронтенду та бекенду",
                description = "Інтегрувати фронтенд (React) із бекендом через API.",
                comment = "Завершити до середи",
                inProgress = true
            ),
        )

        // Request to add all Subjects from the list to DB
        listOfSubject.forEach { subject ->
            coroutineScope.launch {
                _database?.subjectsDao?.addSubject(subject)
            }
        }
        // Request to add all Labs from the list to DB
        listOfSubjectLabs.forEach { lab ->
            coroutineScope.launch {
                _database?.subjectLabsDao?.addSubjectLab(lab)
            }
        }
    }
}