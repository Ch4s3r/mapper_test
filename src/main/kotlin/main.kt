import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

fun main(args: Array<String>) {
    println("Hello World!")
}

data class User(
    val firstName: String,
    val lastName: String,
    val street: String,
    val houseNumber: String,
    val phone: String,
    val age: Int,
    val password: String
)

data class UserDTO(
    val name: String,
    val address: String,
    val telephone: String,
    val age: Int
)

fun User.toDTO() = UserDTO(
    name = "$firstName $lastName",
    address = "$street $houseNumber",
    telephone = phone,
    age = age
)

val USER = User(
    firstName = "Java",
    lastName = "Duke",
    street = "Javastreet",
    houseNumber = "42",
    phone = "1234567",
    age = 30,
    password = "s3cr37"
)

val USER_DTO = UserDTO(
    name = "Java Duke",
    address = "Javastreet 42",
    telephone = "1234567",
    age = 30
)

fun User.toDTOWithReflection(): UserDTO {
    val propertiesByName = User::class.memberProperties.associateBy { it.name }
    return UserDTO::class.primaryConstructor!!.callBy(
        args = UserDTO::class.primaryConstructor!!.parameters.associateWith { parameter ->
            when (parameter.name) {
                UserDTO::name.name -> "$firstName $lastName"
                UserDTO::address.name -> "$street $houseNumber"
                UserDTO::telephone.name -> phone
                else -> propertiesByName[parameter.name]?.get(this)
            }
        })
}
