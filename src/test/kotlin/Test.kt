import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlin.system.measureTimeMillis

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

val USER_LIST = (1..1_000_000).map { USER.copy(age = it) }

class Test : FreeSpec({

    "extension benchmark" {
        println(
            measureTimeMillis {
                USER_LIST.map { it.toDTO() }
            }
        )
    }

    "reflection benchmark" {
        println(
            measureTimeMillis {
                USER_LIST.map { it.toDTOWithReflection() }
            }
        )
    }

    "mapstruct benchmark" {
        println(
            measureTimeMillis {
                UserMapper.INSTANCE.toDTOWithMapper(USER)
            }
        )
    }


    "extension mapper" {
        USER.toDTO() shouldBe USER_DTO
    }

    "reflection mapping" {
        USER.toDTOWithReflection() shouldBe USER_DTO
    }

    "mapstruct mapping" {
        UserMapper.INSTANCE.toDTOWithMapper(USER) shouldBe USER_DTO
    }
})
