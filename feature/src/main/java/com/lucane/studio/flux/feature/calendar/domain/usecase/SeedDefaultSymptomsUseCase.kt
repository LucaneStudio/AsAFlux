package com.lucane.studio.flux.feature.calendar.domain.usecase

import android.content.Context
import com.lucane.studio.flux.data.model.Symptom
import com.lucane.studio.flux.data.model.SymptomCategory
import com.lucane.studio.flux.data.repository.SymptomRepository
import com.lucane.studio.flux.core.R as CoreRes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Seeds the symptom catalog with a starter set of common physical and
 * psychological symptoms, once, so the tracking screen isn't empty on
 * first use. Custom symptoms the user adds afterward live alongside these
 * ([Symptom.isCustom]).
 *
 * Names are resolved from string resources at seed time (not re-resolved
 * later), same tradeoff as any other data written in the user's current
 * locale — matches how the rest of the local-only data model works.
 */
class SeedDefaultSymptomsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: SymptomRepository,
) {
    suspend operator fun invoke() {
        if (repository.getAllSymptoms().first().isNotEmpty()) return

        val presets = listOf(
            CoreRes.string.symptom_cramps to SymptomCategory.PHYSICAL,
            CoreRes.string.symptom_headache to SymptomCategory.PHYSICAL,
            CoreRes.string.symptom_fatigue to SymptomCategory.PHYSICAL,
            CoreRes.string.symptom_bloating to SymptomCategory.PHYSICAL,
            CoreRes.string.symptom_back_pain to SymptomCategory.PHYSICAL,
            CoreRes.string.symptom_tender_breasts to SymptomCategory.PHYSICAL,
            CoreRes.string.symptom_irritability to SymptomCategory.EMOTIONAL,
            CoreRes.string.symptom_anxiety to SymptomCategory.EMOTIONAL,
            CoreRes.string.symptom_sadness to SymptomCategory.EMOTIONAL,
            CoreRes.string.symptom_wellbeing to SymptomCategory.EMOTIONAL,
        )

        presets.forEach { (nameRes, category) ->
            repository.upsertSymptom(
                Symptom(
                    name = context.getString(nameRes),
                    category = category,
                    isCustom = false,
                )
            )
        }
    }
}
