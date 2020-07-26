package com.example.todolist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Todo(    //코틀린에서 Realm을 사용할때는 open키워드를 붙인다.
                    @PrimaryKey var id: Long=0, //@PrimaryKey 는 기본키 제약이다. 중복을 허용하지 않고, 데이터를 식별하는 유일한 키 값
                    var title: String = "",
                    var date: Long = 0
) : RealmObject()