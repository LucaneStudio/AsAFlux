package com.lucane.studio.flux.data.model

/**
 * Represents the declared flow intensity for a given day.
 *
 * ## Values
 * - [NOT_DECLARED] : The day is part of a known period (e.g. pre-filled after a
 *   start declaration) but the user has not yet specified an intensity.
 *   Distinct from [NONE]: we know bleeding is occurring, we just don't know how much.
 *   Used instead of assuming a default intensity, which could produce false medical data
 *   when exporting to a healthcare professional.
 * - [NONE]         : The user has explicitly declared no bleeding on this day.
 * - [SPOTTING]     : Declared by the user — very light, trace amounts.
 * - [LIGHT]        : Declared by the user — light flow.
 * - [MEDIUM]       : Declared by the user — moderate flow.
 * - [HEAVY]        : Declared by the user — heavy flow.
 *
 * ## Storage
 * Persisted as [Enum.name] strings in Room. Never reorder or rename values
 * without a corresponding Room migration that updates stored strings via UPDATE.
 *
 * ## Medical note
 * [NOT_DECLARED] must never be treated as equivalent to any specific intensity
 * in exports or statistics. It must be surfaced to the user as "not specified"
 * rather than silently defaulting to a value.
 */
enum class FlowIntensity {
    NOT_DECLARED,
    NONE,
    SPOTTING,
    LIGHT,
    MEDIUM,
    HEAVY,
}