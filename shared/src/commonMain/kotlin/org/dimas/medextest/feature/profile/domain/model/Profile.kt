package org.dimas.medextest.feature.profile.domain.model

data class Profile(
    val firstName: String,
    val lastName: String,
    val avatarSeed: Int,
) {
    val shortName: String get() = "$firstName ${lastName.first()}."
    val initials: String get() = "${firstName.first()}${lastName.first()}"
}
