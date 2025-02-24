package com.group.libraryapp.domain.user.loanhistory

import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository : JpaRepository<UserLoanHistory, Long> {

    fun findByBookNameAndStatus(name: String, status: UserLoanStatus): UserLoanHistory?

    fun findAllByStatus(loaned: UserLoanStatus): List<UserLoanHistory>

}
