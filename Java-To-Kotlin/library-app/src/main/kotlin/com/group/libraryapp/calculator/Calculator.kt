package com.group.libraryapp.calculator

/**
 * 요구사항
 * 1. 계산기는 정수만을 취급한다.
 * 2. 계산기가 생성될 때 숫자를 1개 받는다.
 * 3. 최초 숫자가 기록된 이후에는 연산자 함수를 통해 숫자를 받아 지속적으로 계산한다.
 */
class Calculator (
    private var _number: Int
) {

    val number: Int
        get() = this._number

    fun add(operand: Int) {
        this._number += operand
    }

    fun minus(operand: Int) {
        this._number -= operand
    }

    fun multiply(operand: Int) {
        this._number *= operand
    }

    fun divide(operand: Int) {
        if (operand == 0) {
            throw IllegalArgumentException("0으로 나눌 수 없습니다")
        }
        this._number /= operand
    }

}