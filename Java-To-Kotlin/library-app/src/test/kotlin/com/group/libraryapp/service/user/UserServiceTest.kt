package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    val userService: UserService,
    val userRepository: UserRepository,
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 등록이 정상 동작한다")
    fun saveUserTest() {
        // given
        val request = UserCreateRequest("김준영", null)

        // when
        userService.saveUser(request)

        // then
        val result = userRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("김준영")
        assertThat(result[0].age).isNull()
    }

    @Test
    @DisplayName("유저 조회가 정상 동작한다")
    fun getUsersTest() {
        // given
        userRepository.saveAll(
            listOf(
                User("A", 20),
                User("B", null),
            )
        )

        // when
        val results = userService.getUsers()

        // then
        assertThat(results).hasSize(2)          // [ UserResponse(), UserResponse() ]
        assertThat(results).extracting("name")  // [ "A", "B" ]
            .containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age")  // [ 20, null ]
            .containsExactlyInAnyOrder(20, null)
    }

    @Test
    @DisplayName("유저 업데이트가 정상 동작한다")
    fun updateUserNameTest() {
        // given
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id!!, "B")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제가 정상 동작한다")
    fun deleteUserTest() {
        // given
        userRepository.save(User("A", null))

        // when
        userService.deleteUser("A")

        // then
        assertThat(userRepository.findAll()).isEmpty()
    }

}