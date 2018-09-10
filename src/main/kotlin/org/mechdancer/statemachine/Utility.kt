package org.mechdancer.statemachine

internal fun Boolean.then(block: () -> Unit) = also { if (it) block() }

internal fun Boolean.otherwise(block: () -> Unit) = also { if (!it) block() }
