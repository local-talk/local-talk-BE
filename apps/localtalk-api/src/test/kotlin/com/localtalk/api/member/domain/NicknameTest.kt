package com.localtalk.api.member.domain

import org.assertj.core.api.Assertions.assertThatNoException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class NicknameTest {

    @Nested
    inner class `Nickname을 생성할 때` {

        @ParameterizedTest
        @ValueSource(
            strings = [
                "ab", "가나", "12", // 2자 경계값
                "홍길동", "johnDoe", "123456", "user_name", // 일반 케이스
                "abcdefghijkl", "가나다라마바사아자차카타", "user_name_12", // 12자 경계값
                "홍길동_123a", "hello world", "test_case", // 특수 조합 (앞뒤 문자/숫자)
            ],
        )
        fun `유효한 닉네임은 검증을 통과한다`(nickname: String) {
            assertThatNoException().isThrownBy {
                Nickname(nickname)
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["가", "a", "1", "가나다라마바사아자차카타파", "abcdefghijklm", "1234567890123"])
        fun `닉네임 길이가 규칙에 위반되는 경우 정확한 메시지와 함께 예외를 발생시킨다`(nickname: String) {
            assertThatThrownBy { Nickname(nickname) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("닉네임은 2자 이상 12자 이하여야 합니다")
        }

        @ParameterizedTest
        @ValueSource(strings = ["닉네임@", "user@", "test-user", "user.name", "user#123", "테스트!", "hello\$world"])
        fun `허용되지 않는 문자가 포함된 경우 정확한 메시지와 함께 예외를 발생시킨다`(nickname: String) {
            assertThatThrownBy { Nickname(nickname) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("닉네임은 한글, 영문, 숫자, 공백, _만 사용 가능합니다")
        }

        @ParameterizedTest
        @ValueSource(strings = [" hello", "_test", " 테스트", "_닉네임"])
        fun `공백이나 언더스코어로 시작하는 경우 정확한 메시지와 함께 예외를 발생시킨다`(nickname: String) {
            assertThatThrownBy { Nickname(nickname) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("닉네임은 공백이나 _로 시작할 수 없습니다")
        }

        @ParameterizedTest
        @ValueSource(strings = ["hello ", "test_", "테스트 ", "닉네임_"])
        fun `공백이나 언더스코어로 끝나는 경우 정확한 메시지와 함께 예외를 발생시킨다`(nickname: String) {
            assertThatThrownBy { Nickname(nickname) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("닉네임은 공백이나 _로 끝날 수 없습니다")
        }

        @ParameterizedTest
        @ValueSource(strings = ["hello  world", "test__case", "a  b", "x__y"])
        fun `공백이나 언더스코어가 연속으로 사용된 경우 정확한 메시지와 함께 예외를 발생시킨다`(nickname: String) {
            assertThatThrownBy { Nickname(nickname) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("닉네임에 공백이나 _를 연속으로 사용할 수 없습니다")
        }
    }
}