package org.mechdancer.statemachine

/** bool = true  则执行代码块 */
internal fun Boolean.then(block: () -> Unit) = also { if (it) block() }

/** bool = false 则执行代码块 */
internal fun Boolean.otherwise(block: () -> Unit) = also { if (!it) block() }
