package ie.wit.caloriepal.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ie.wit.caloriepal.helpers.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

const val JSON_USERS_FILE = "user.json"
val userListType = object : TypeToken<UserModel>() {}.type


class UserJSONStore(val context: Context) : Store<UserModel>, AnkoLogger {

    var users = mutableListOf<UserModel>()
    var user = UserModel()

    init{
        if(exists(context, JSON_USERS_FILE)){
            deserializeSingleUser()
        }
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        setUserDetails(user)
        //users.add(user)
    }

    override fun update(user: UserModel) {
        //val foundUser = getById(user.id)
        //if (foundUser != null) {
       setUserDetails(user)
       // }
    }

    fun createOrUpdate(user: UserModel, edit: Boolean){
        if(edit){
            update(user)
        }else{
            create(user)
        }
        serializeSingleUser()
    }

    override fun delete(user: UserModel) {
//        val foundUser = getById(user.id)
//        if (foundUser != null) {
//            users.remove(foundUser)
//        }
        val blankUser = UserModel()
        setUserDetails(blankUser)
        serializeSingleUser()
    }

    override fun findAll(): List<UserModel> {
        return users
    }

    override fun getById(id:Long): UserModel? {
        return users.find { p -> p.id == id }
    }

    override fun serialize(){
        val jsonString = gsonBuilder.toJson(users, userListType)
        write(context, JSON_USERS_FILE, jsonString)
    }

    private fun serializeSingleUser(){
        info{ "ATTEMPTING SERIALIZE SINGLE USER"}
        val jsonString = gsonBuilder.toJson(this.user)
        write(context, JSON_USERS_FILE, jsonString)
    }

    override fun deserialize() {
        val jsonString = read(context, JSON_USERS_FILE)
        users = Gson().fromJson(jsonString, userListType)
    }

    private fun deserializeSingleUser(){
        info{ "ATTEMPTING TO DESERIALIZE SINGLE USER"}
        val jsonString = read(context, JSON_USERS_FILE)
        this.user = Gson().fromJson(jsonString, userListType)
        info {"USER: ${this.user}"}
    }

    private fun setUserDetails(user:UserModel){
        this.user.startWeight = user.startWeight
        this.user.goalWeight = user.goalWeight
        this.user.name = user.name
        this.user.deadline = user.deadline
        this.user.deficit = user.deficit
        this.user.id = user.id
        info {"User Details after setUserDetails: ${this.user}"}

    }

}


