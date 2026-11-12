package com.example.navigationlab

import com.example.navigationlab.data.model.Student
import com.example.navigationlab.data.repository.StudentRepository
import com.example.navigationlab.ui.home.HomeViewModel
import org.junit.Assert.*
import org.junit.Test

class HomeViewModelTest {
    private class FakeRepository : StudentRepository {
        val queries = mutableListOf<String>()
        val callbacks = mutableListOf<(Result<List<Student>>) -> Unit>()
        override fun getStudents(callback: (Result<List<Student>>) -> Unit) {
            queries.add("")
            callbacks.add(callback)
        }
        override fun searchStudents(query: String, callback: (Result<List<Student>>) -> Unit) {
            queries.add(query)
            callbacks.add(callback)
        }
        override fun getStudent(id: Int, callback: (Result<Student>) -> Unit) {
            error("Not used by Home")
        }
    }

    @Test fun typingDoesNotSearchAndSubmissionKeepsQueryAfterSuccess() {
        val repository = FakeRepository()
        val vm = HomeViewModel(repository)
        repository.callbacks[0](Result.success(emptyList()))
        vm.onSearchQueryChange("John")
        assertEquals(listOf(""), repository.queries)
        vm.searchStudents()
        assertTrue(vm.uiState.isLoading)
        assertEquals(listOf("", "John"), repository.queries)
        val john = Student(1, "John Doe", "john@example.com", "University")
        repository.callbacks.last()(Result.success(listOf(john)))
        assertFalse(vm.uiState.isLoading)
        assertEquals("John", vm.uiState.searchQuery)
        assertEquals(listOf(john), vm.uiState.students)
    }

    @Test fun blankQueryRestoresInitialList() {
        val repository = FakeRepository()
        val vm = HomeViewModel(repository)
        vm.onSearchQueryChange("John")
        vm.searchStudents()
        vm.onSearchQueryChange("   ")
        vm.searchStudents()
        assertEquals(listOf("", "John", ""), repository.queries)
    }

    @Test fun failureUsesErrorStateAndRetryRepeatsSubmittedQuery() {
        val repository = FakeRepository()
        val vm = HomeViewModel(repository)
        vm.onSearchQueryChange("John")
        vm.searchStudents()
        repository.callbacks.last()(Result.failure(Exception("HTTP 500")))
        assertEquals("HTTP 500", vm.uiState.errorMessage)
        assertFalse(vm.uiState.isLoading)
        vm.onSearchQueryChange("Jane")
        vm.retry()
        assertEquals("John", repository.queries.last())
        assertTrue(vm.uiState.isLoading)
        assertNull(vm.uiState.errorMessage)
    }

    @Test fun olderResponseCannotOverwriteLatestSearch() {
        val repository = FakeRepository()
        val vm = HomeViewModel(repository)
        vm.onSearchQueryChange("John")
        vm.searchStudents()
        repository.callbacks[1](Result.success(emptyList()))
        repository.callbacks[0](Result.failure(Exception("Old request")))
        assertNull(vm.uiState.errorMessage)
        assertFalse(vm.uiState.isLoading)
    }
}
