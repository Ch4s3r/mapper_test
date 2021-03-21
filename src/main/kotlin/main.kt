import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
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

@Mapper
interface UserMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(UserMapper::class.java)
    }

    @Mappings(
        Mapping(target = "name", expression = "java(user.getFirstName() + \" \" + user.getLastName())"),
        Mapping(target = "address", expression = "java(user.getStreet() + \" \" + user.getHouseNumber())"),
        Mapping(target = "telephone", source = "phone")
    )
    fun toDTOWithMapper(user: User): UserDTO
}