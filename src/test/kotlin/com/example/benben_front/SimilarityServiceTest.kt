package com.example.benben_front

import com.example.benben_front.service.SimilarityService
import com.example.benben_front.service.getPrompt
import org.junit.jupiter.api.Test

/**
 * SimilarityServiceTest
 * @author aldebran
 * @since 2023-08-04
 */
class SimilarityServiceTest {

    @Test
    fun getPrompt() {
        SimilarityService.getPrompt("介绍木卫二",
                mutableListOf("wiki_interesting_lib"));
    }
}