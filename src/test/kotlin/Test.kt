import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlin.system.measureTimeMillis

val USER_LIST = (1..10_000_000).map { USER.copy(age = it) }

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

    "extension mapper" {
        USER.toDTO() shouldBe USER_DTO
    }

    "reflection mapping" {
        USER.toDTOWithReflection() shouldBe USER_DTO
    }
})